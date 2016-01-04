package net.azurewebsites.sportywarsaw.services;

import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.models.CommentModel;
import net.azurewebsites.sportywarsaw.models.UserModel;
import net.azurewebsites.sportywarsaw.models.UserPlusModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public interface UserService {
    @GET("Users/{username}")
    Call<UserModel> get(@Path("username") String username);

    @GET("Users/{username}/Details")
    Call<UserPlusModel> getDetails(@Path("username") String username);

    @GET("Users/MyFriends")
    Call<List<UserModel>> getMyFriends();

    @GET("Users/MyPendingFriendsRequests")
    Call<List<UserModel>> getMyPendingFriendRequests();

    @GET("Users/MySentFriendsRequests")
    Call<List<UserModel>> getMySentFriendRequests();

    @POST("Users/SendFriendRequest/{username}")
    Call<ResponseBody> sendFriendRequest(@Path("username") String username);

    @POST("Users/AcceptFriendRequest/{username}")
    Call<ResponseBody> acceptFriendRequest(@Path("username") String username);

    @POST("Users/RemoveFriend/{username}")
    Call<ResponseBody> rejectFriendRequest(@Path("username") String username);

    //// TODO: w serwerze jest post, nie powinien byc put? 
    @POST("Users/UpdateProfile")
    Call<ResponseBody> put(@Body UserPlusModel userPlusModel);

    @DELETE("Users/{id}")
    Call<ResponseBody> delete(@Path("id") int id);





}
