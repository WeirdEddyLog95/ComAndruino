package com.example.edgar_rangel.btled_mvp;

/**
 * Created by Edgar_Rangel on 12/9/2017.
 */

public interface View {
    /*Que metodos se puede iniciar para aplicar en la vista*/
    /*Los botones de direcciones*/
    void writeUp();
    void writeLeft();
    void writeRight();
    void writeDown();
    /*Para encender los LEDs*/
    void ledON();
    void ledOFF();
    /*Para identificar los dispositivos emparejados*/
    void dispoEmparejado();
}
