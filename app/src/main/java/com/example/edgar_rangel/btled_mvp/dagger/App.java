package com.example.edgar_rangel.btled_mvp.dagger;

/**
 * Created by Edgar_Rangel on 12/9/2017.
 */
import android.app.Application;
import com.example.edgar_rangel.btled_mvp.PresenterModule;

public class App extends Application {
    private AppComponent appComponent;
    @Override
    public void onCreate(){
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .presenterModule(new PresenterModule())
                .build();
    }

    public AppComponent getComponent(){
        return appComponent;
    }
}
