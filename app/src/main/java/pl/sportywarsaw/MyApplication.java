package pl.sportywarsaw;

import android.app.Application;
import android.content.Context;

import pl.sportywarsaw.infrastructure.ApplicationComponent;
import pl.sportywarsaw.infrastructure.ApplicationModule;
import pl.sportywarsaw.infrastructure.DaggerApplicationComponent;
import pl.sportywarsaw.infrastructure.RestServicesModule;

public class MyApplication extends Application {

    private static String baseUrl = "https://sportywarsaw.pl/api/";

    private ApplicationComponent servicesComponent;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        servicesComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .restServicesModule(new RestServicesModule(baseUrl))
                .build();
    }

    public ApplicationComponent getServicesComponent() {
        return servicesComponent;
    }

    public static Context getContext() {
        return MyApplication.context;
    }

}