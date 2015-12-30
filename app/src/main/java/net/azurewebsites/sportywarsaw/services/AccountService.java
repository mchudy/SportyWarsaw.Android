package net.azurewebsites.sportywarsaw.services;

import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.models.AccessTokenModel;
import net.azurewebsites.sportywarsaw.models.RegisterAccountModel;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface AccountService {
    @POST("token")
    @FormUrlEncoded
    Call<AccessTokenModel> getToken(@Field("username") String username,
                                    @Field("password") String password,
                                    @Field("grant_type") String grantType);

    @POST("account/register")
    Call<ResponseBody> registerAccount(@Body RegisterAccountModel registerAccountModel);
}
