package net.azurewebsites.sportywarsaw;

import android.app.Application;

import net.azurewebsites.sportywarsaw.infrastructure.ApplicationModule;
import net.azurewebsites.sportywarsaw.infrastructure.DaggerServicesComponent;
import net.azurewebsites.sportywarsaw.infrastructure.RestServicesModule;
import net.azurewebsites.sportywarsaw.infrastructure.ServicesComponent;

public class MyApplication extends Application {

    private static String baseUrl = "http://10.0.2.2:81/api/";

    private ServicesComponent servicesComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        servicesComponent = DaggerServicesComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .restServicesModule(new RestServicesModule(baseUrl))
                .build();
    }

    public ServicesComponent getServicesComponent() {
        return servicesComponent;
    }

}