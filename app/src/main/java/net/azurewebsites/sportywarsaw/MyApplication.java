package net.azurewebsites.sportywarsaw;

import android.app.Application;

import net.azurewebsites.sportywarsaw.infrastructure.ApplicationComponent;
import net.azurewebsites.sportywarsaw.infrastructure.ApplicationModule;
import net.azurewebsites.sportywarsaw.infrastructure.DaggerApplicationComponent;
import net.azurewebsites.sportywarsaw.infrastructure.RestServicesModule;

public class MyApplication extends Application {

    private static String baseUrl = "http://10.0.2.2:81/api/";

    private ApplicationComponent servicesComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        servicesComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .restServicesModule(new RestServicesModule(baseUrl))
                .build();
    }

    public ApplicationComponent getServicesComponent() {
        return servicesComponent;
    }

}