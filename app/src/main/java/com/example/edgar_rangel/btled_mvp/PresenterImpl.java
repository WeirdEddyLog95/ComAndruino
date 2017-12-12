package com.example.edgar_rangel.btled_mvp;

import android.content.Intent;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

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
    public void rotaImagen(String RX, ImageView IV) {
        String captura = RX;
        ImageView capturaIV = IV;
        if(captura == "Auto hacia el Frente") {
            capturaIV.setRotation(90f);
        }
        if(captura == "Auto hacia la Izquierda") {
            capturaIV.setRotation(180f);
        }
        if(captura == "Auto hacia la Derecha") {
            capturaIV.setRotation(0f);
        }
        if(captura == "Auto hacia Atras") {
            capturaIV.setRotation(270f);
        }
    }

}
