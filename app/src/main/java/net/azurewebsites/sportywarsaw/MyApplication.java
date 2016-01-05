package net.azurewebsites.sportywarsaw;

import android.app.Application;
import android.content.Context;

import net.azurewebsites.sportywarsaw.infrastructure.ApplicationComponent;
import net.azurewebsites.sportywarsaw.infrastructure.ApplicationModule;
import net.azurewebsites.sportywarsaw.infrastructure.DaggerApplicationComponent;
import net.azurewebsites.sportywarsaw.infrastructure.RestServicesModule;

public class MyApplication extends Application {

    private static String baseUrl = "https://sportywarsaw.azurewebsites.net/api/";

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