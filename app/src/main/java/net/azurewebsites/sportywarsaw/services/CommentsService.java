package net.azurewebsites.sportywarsaw.services;

import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.models.AddCommentModel;
import net.azurewebsites.sportywarsaw.models.AddMeetingModel;
import net.azurewebsites.sportywarsaw.models.ChangeMeetingModel;
import net.azurewebsites.sportywarsaw.models.CommentModel;
import net.azurewebsites.sportywarsaw.models.MeetingModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public interface CommentsService {
    @GET("comments/{id}")
    Call<CommentModel> get(@Path("id") int id);

    @GET("comments/meetings/{meetingid}")
    Call<List<CommentModel>> getAll(@Path("meetingid") int meetingid);

    @POST("comments")
    Call<ResponseBody> post(@Body AddCommentModel addCommentModel);

    @PUT("comments")
    Call<ResponseBody> put(@Body CommentModel commentModel);

    @DELETE("comments/{id}")
    Call<ResponseBody> delete(@Path("id") int id);


}
