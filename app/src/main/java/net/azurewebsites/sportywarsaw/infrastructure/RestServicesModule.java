package net.azurewebsites.sportywarsaw.infrastructure;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import net.azurewebsites.sportywarsaw.services.AccountService;
import net.azurewebsites.sportywarsaw.services.MeetingsService;
import net.azurewebsites.sportywarsaw.services.SportsFacilitiesService;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Module registering REST API services
 *
 * @author Marcin Chudy
 */
@Module(
        includes = {ApplicationModule.class}
)
public class RestServicesModule {

    private String baseUrl;

    public RestServicesModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(final SharedPreferences preferences) {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Accept-Language", Locale.getDefault().getLanguage());
                String token = preferences.getString("accessToken", null);
                if(!TextUtils.isEmpty(token)) {
                    builder.addHeader("Authorization", "Bearer " + token);
                }
                return chain.proceed(builder.build());
            }
        });
        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();
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


    @Provides
    @Singleton
    MeetingsService provideMeetingsService(Retrofit retrofit) {
        MeetingsService service = retrofit.create(MeetingsService.class);
        return service;
    }

}
