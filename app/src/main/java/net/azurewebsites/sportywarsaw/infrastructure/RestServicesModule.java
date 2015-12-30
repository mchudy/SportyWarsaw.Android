package net.azurewebsites.sportywarsaw.infrastructure;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import net.azurewebsites.sportywarsaw.services.AccountService;
import net.azurewebsites.sportywarsaw.services.SportsFacilitiesService;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

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
                String token = preferences.getString("accessToken", null);
                if(!TextUtils.isEmpty(token)) {
                    Request.Builder builder = chain.request().newBuilder();
                    builder.addHeader("Authorization", "Bearer " + token);
                    return chain.proceed(builder.build());
                }
                return chain.proceed(chain.request());
            }
        });
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
