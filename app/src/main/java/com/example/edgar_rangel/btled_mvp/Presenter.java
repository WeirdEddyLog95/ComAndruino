package com.example.edgar_rangel.btled_mvp;

import android.widget.ImageView;

/**
 * Created by Edgar_Rangel on 12/9/2017.
 */

public interface Presenter {
    /*Que metodos se puede crear para ser implementados y usados en Main_Activity?*/
    void setView(View view);
    void rotarImagen(String mensaje, ImageView image);
}
