package net.azurewebsites.sportywarsaw.services;

import android.text.style.ReplacementSpan;

import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.models.AddMeetingModel;
import net.azurewebsites.sportywarsaw.models.ChangeMeetingModel;
import net.azurewebsites.sportywarsaw.models.MeetingModel;
import net.azurewebsites.sportywarsaw.models.MeetingPlusModel;
import net.azurewebsites.sportywarsaw.models.RegisterAccountModel;
import net.azurewebsites.sportywarsaw.models.UserModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface MeetingsService {
    @GET("meetings/{id}")
    Call<MeetingModel> getMeeting(@Path("id") int id);

    @GET("meetings/{id}/Details")
    Call<MeetingPlusModel> getMeetingDetails(@Path("id") int id);

    @GET("meetings/{id}/Participants")
    Call<List<UserModel>> getMeetingParticipants(@Path("id") int id);

    @GET("meetings/MyMeetings")
    Call<List<MeetingModel>> getMyMeetings();

    @POST("meetings/Join/{id}")
    Call<ResponseBody> joinMeeting(@Path("id") int id);

    @POST("meetings/Leave/{id}")
    Call<ResponseBody> leaveMeeting(@Path("id") int id);

    @POST("meetings")
    Call<ResponseBody> post(@Body AddMeetingModel addMeetingModel);

    @PUT("meetings")
    Call<ResponseBody> put(@Body ChangeMeetingModel changeMeetingModel);

    @DELETE("meetings/{id}")
    Call<ResponseBody> delete(@Path("id") int id);

}
