package com.example.edgar_rangel.btled_mvp;

/**
 * Created by Edgar_Rangel on 12/9/2017.
 */
import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {
    @Provides
    public Presenter providePresenter(Model model){
        return new PresenterImpl(model);
    }
    @Provides
    public Model provideModel(){
        return new Model();
    }
}