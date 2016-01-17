package pl.sportywarsaw.services;

import com.squareup.okhttp.ResponseBody;

import java.util.List;

import pl.sportywarsaw.models.AddMeetingModel;
import pl.sportywarsaw.models.ChangeMeetingModel;
import pl.sportywarsaw.models.MeetingModel;
import pl.sportywarsaw.models.MeetingPlusModel;
import pl.sportywarsaw.models.UserModel;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MeetingsService {
    @GET("meetings/{id}")
    Call<MeetingModel> getMeeting(@Path("id") int id);

    @GET("meetings/{id}/Details")
    Call<MeetingPlusModel> getMeetingDetails(@Path("id") int id);

    @GET("meetings/{id}/Participants")
    Call<List<UserModel>> getMeetingParticipants(@Path("id") int id);

    @GET("meetings/MyMeetings")
    Call<List<MeetingModel>> getMyMeetings();

    @GET("meetings/NotMyMeetings")
    Call<List<MeetingModel>> getNotMyMeetings();

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

    @GET("meetings/isParticipant")
    Call<Boolean> isParticipant(@Query("username") String username,
                                @Query("meetingId") int meetingId);

}
