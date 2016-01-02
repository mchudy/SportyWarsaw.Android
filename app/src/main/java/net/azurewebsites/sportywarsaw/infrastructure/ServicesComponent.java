package net.azurewebsites.sportywarsaw.infrastructure;

import net.azurewebsites.sportywarsaw.activities.LoginActivity;
import net.azurewebsites.sportywarsaw.activities.MainActivity;
import net.azurewebsites.sportywarsaw.activities.RegisterActivity;
import net.azurewebsites.sportywarsaw.fragments.StartupFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RestServicesModule.class, ApplicationModule.class})
public interface ServicesComponent {
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(RegisterActivity activity);
    void inject(StartupFragment startupFragment);
}
