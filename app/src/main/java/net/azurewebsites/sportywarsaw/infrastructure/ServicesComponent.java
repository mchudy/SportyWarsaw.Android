package net.azurewebsites.sportywarsaw.infrastructure;

import net.azurewebsites.sportywarsaw.activities.LoginActivity;
import net.azurewebsites.sportywarsaw.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RestServicesModule.class, ApplicationModule.class})
public interface ServicesComponent {
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
}
