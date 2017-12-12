package com.example.edgar_rangel.btled_mvp.dagger;

/**
 * Created by Edgar_Rangel on 12/9/2017.
 */
import android.app.Application;
import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return application;
    }
}