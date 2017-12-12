package com.example.edgar_rangel.btled_mvp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edgar_rangel.btled_mvp.dagger.App;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View {

    //Binding de los botones con ButterKnife
    @BindView(R.id.btnUP) ImageButton btnup;
    @BindView(R.id.btnLft) ImageButton btnlft;
    @BindView(R.id.btnRght) ImageButton btnrght;
    @BindView(R.id.btnDwn) ImageButton btndwn;
    @BindView(R.id.btnledON) Button btnON;
    @BindView(R.id.btnledOFF) Button btnOFF;
    @BindView(R.id.btnPairing) Button btnPair;
    @BindView(R.id.btStatus) TextView BTstatus;
    @BindView(R.id.leerBuffer) TextView leaBuffer;
    @BindView(R.id.dispoLV) ListView dispoLista;
    @BindView(R.id.iv_Car) ImageView vistaCoche;
    @BindView(R.id.Vista_LED) TextView vLED;
    @BindView(R.id.textBT) TextView BTtext;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices; //El set para los dispositivos de Bluetooth emparejados
    private ArrayAdapter<String> mBTArrayAdapter; //El arreglo de adaptador de bluetooth en String
    private final String TAG = MainActivity.class.getSimpleName();
    private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread; // EL metodo interno del hilo Bluetooth para enviar y recibir datos
    private BluetoothSocket mBTSocket = null; // El camino de datos bidireccional entre cliente y cliente
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Identificador aletorio unico
    //Se define para identificar tipos de datos compartidos entre la llamada de metodos
    private final static int REQUEST_ENABLE_BT = 1; //Usado para identificar nombres de bluetooths agregados
    private final static int MESSAGE_READ = 2; //Usado en el Handler para identificar mensajes de actualizacicnes
    private final static int CONNECTING_STATUS = 3; //Usado en el Handler de Bluetooth para identificar estados de mensajes
    PresenterImpl implement;
    @Inject
    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App) getApplication()).getComponent().inject(this);//Al aplicar esto se crashea al inicio de la aplicacion...
        //Unir el Bind de los elementos en el activity_main
        ButterKnife.bind(this);
        mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio
        dispoLista.setAdapter(mBTArrayAdapter); // assign model to view
        dispoLista.setOnItemClickListener(mDeviceClickListener);
        //Para cuando se entra a la aplicacion se pregunta para encender el Bluetooth del telefono
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        //El handler que servira para recibir mensajes del Arduino
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    String finalmessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                        finalmessage = readMessage;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    leaBuffer.setText(finalmessage);
                    if(leaBuffer.getText() == "Auto hacia el Frente"){
                        vistaCoche.setRotation((float) 90.0);
                    }
                    if(leaBuffer.getText() == "Auto hacia la Izquierda"){
                        vistaCoche.setRotation((float) 180.0);
                    }
                    if(leaBuffer.getText() == "Auto hacia la Derecha"){
                        vistaCoche.setRotation((float) 0.0);
                    }
                    if(leaBuffer.getText() == "Auto hacia Atras"){
                        vistaCoche.setRotation((float) 270.0);
                    }
                }

                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1)
                        BTstatus.setText("Connected to Device: " + (String) (msg.obj));
                    else
                        BTstatus.setText("Connection Failed");
                }
            }
        };

    }
    //Fuera del Create
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        //Se revisa cual peticion que se aplico al inicio se ha puesto Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            //Se asegura si la peticion fue exitosa
            if (resultCode == RESULT_OK) {
                //El usuario recogio un contacto
                //El dato del Intent identifica cual contacto fue seleccionado
                BTstatus.setText("Enabled");
            } else
                BTstatus.setText("Disabled");
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, android.view.View v, int arg2, long arg3) {

            if (!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }

            BTstatus.setText("Connecting...");
            //Se consigue la direccion MAC, que tiene 17 caracteres en la Vista
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);

            //Aparece un nuevo hilo para evitar el bloqueo del hilo de la Interfaz Grafica del Usuario
            new Thread() {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    //Se establece una conexion con el Socket de Bluetooth
                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            //Se inserta esta linea para avisar lo siguiente
                            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (fail == false) {
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();

                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection", e);
        }
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //Se consigue la entrada y salida, usando objetos temporales, dado que los datos de Stream son finales
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // Bytes regresados del metodo read()
            //Se sigue escuchando el InputStream hasta que surja una Exception
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100); //Se pone pausa y espera por el resto de los datos, se puede ajusta esto depediendo en la velocidad del envio
                        bytes = mmInStream.available(); //Que tantos bytes estan listo para ser leidos
                        bytes = mmInStream.read(buffer, 0, bytes); //Registra cuantos bytes se leyeron en realidad
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); //Se envia los bytes obtenidos hacia la actividad del Usuario
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /*Se llama esto desde el main activity para enviar datos al dispositivo remoto(en este caso se escribe para enviar datos al Arduino por Bluetooth)*/
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /*Se llama esta actividad desde el main activity para apagar la conexion*/
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /*Los metodos para usarse en el Main_Activity del View*/
    @OnClick(R.id.btnUP)
    @Override
    public void writeUp() {
        if (mBTArrayAdapter == null) {
            //Se verifica si el dispositivo no tiene soporte con Bluetooth
            BTstatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            if (mConnectedThread != null) //Se revisa primero si hay un hilo de conexion creado antes de enviar los datos
                mConnectedThread.write("a");
        }
    }

    @OnClick(R.id.btnLft)
    @Override
    public void writeLeft() {
        if (mBTArrayAdapter == null) {
            //Se verifica si el dispositivo no tiene soporte con Bluetooth
            BTstatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            if (mConnectedThread != null) //Se revisa primero si hay un hilo de conexion creado antes de enviar los datos
                mConnectedThread.write("b");
        }
    }

    @OnClick(R.id.btnRght)
    @Override
    public void writeRight() {
        if (mBTArrayAdapter == null) {
            //Se verifica si el dispositivo no tiene soporte con Bluetooth
            BTstatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            if (mConnectedThread != null) //Se revisa primero si hay un hilo de conexion creado antes de enviar los datos
                mConnectedThread.write("c");
        }
    }

    @OnClick(R.id.btnDwn)
    @Override
    public void writeDown() {
        if (mBTArrayAdapter == null) {
            //Se verifica si el dispositivo no tiene soporte con Bluetooth
            BTstatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            if (mConnectedThread != null) //Se revisa primero si hay un hilo de conexion creado antes de enviar los datos
                mConnectedThread.write("d");
        }
    }

    @OnClick(R.id.btnledON)
    @Override
    public void ledON() {
        if (mBTArrayAdapter == null) {
            //Se verifica si el dispositivo no tiene soporte con Bluetooth
            BTstatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            if (mConnectedThread != null) //Se revisa primero si hay un hilo de conexion creado antes de enviar los datos
                mConnectedThread.write("1");
        }
    }

    @OnClick(R.id.btnledOFF)
    @Override
    public void ledOFF() {
        if (mBTArrayAdapter == null) {
            //Se verifica si el dispositivo no tiene soporte con Bluetooth
            BTstatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            if (mConnectedThread != null) //Se revisa primero si hay un hilo de conexion creado antes de enviar los datos
                mConnectedThread.write("2");
        }
    }

    @OnClick(R.id.btnPairing)
    @Override
    public void dispoEmparejado() {
        if (mBTArrayAdapter == null) {
            //Se verifica si el dispositivo no tiene soporte con Bluetooth
            BTstatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            mPairedDevices = mBTAdapter.getBondedDevices();
            if (mBTAdapter.isEnabled()) {
                //Se obtiene el nombre y la direccion de dispositivo que se puede emparejar al telefono
                for (BluetoothDevice device : mPairedDevices)
                    mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

                Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
        }
    }
}
