package net.azurewebsites.sportywarsaw.services;

import net.azurewebsites.sportywarsaw.models.AccessTokenModel;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface AccountService {
    @POST("token")
    @FormUrlEncoded
    Call<AccessTokenModel> getToken(@Field("username") String username,
                                    @Field("password") String password,
                                    @Field("grant_type") String grantType);
}
