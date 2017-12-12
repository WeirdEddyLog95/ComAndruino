package com.example.edgar_rangel.btled_mvp.dagger;

/**
 * Created by Edgar_Rangel on 12/9/2017.
 */
import com.example.edgar_rangel.btled_mvp.PresenterModule;
import com.example.edgar_rangel.btled_mvp.MainActivity;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class})
public interface AppComponent {
    void inject(MainActivity target);
}