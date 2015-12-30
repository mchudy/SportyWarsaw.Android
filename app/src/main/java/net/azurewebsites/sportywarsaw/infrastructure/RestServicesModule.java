package net.azurewebsites.sportywarsaw.infrastructure;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import net.azurewebsites.sportywarsaw.services.AccountService;
import net.azurewebsites.sportywarsaw.services.SportsFacilitiesService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

@Module
public class RestServicesModule {

    private String baseUrl;

    public RestServicesModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    SportsFacilitiesService provideSportsFacilitiesService(Retrofit retrofit) {
        SportsFacilitiesService service = retrofit.create(SportsFacilitiesService.class);
        return service;
    }

    @Provides
    @Singleton
    AccountService provideAccountService(Retrofit retrofit) {
        AccountService service = retrofit.create(AccountService.class);
        return service;
    }

}
