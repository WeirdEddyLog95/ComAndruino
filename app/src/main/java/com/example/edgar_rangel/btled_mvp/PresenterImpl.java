package com.example.edgar_rangel.btled_mvp;

import android.widget.ImageView;

/**
 * Created by Edgar_Rangel on 12/9/2017.
 */

public class PresenterImpl implements Presenter {
    private Model model;
    private View view;

    public PresenterImpl(Model model){
        this.model = model;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void rotarImagen(String mensaje, ImageView image) {
        if(mensaje == "Auto hacia el Frente"){
            image.setRotation(90f);
        }
        if(mensaje == "Auto hacia la Izquierda"){
            image.setRotation(180f);
        }
        if(mensaje == "Auto hacia la Derecha"){
            image.setRotation(0f);
        }
        if(mensaje == "Auto hacia Atras"){
            image.setRotation(270f);
        }
    }

}
