package net.azurewebsites.sportywarsaw.services;

import net.azurewebsites.sportywarsaw.models.MeetingModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface MeetingsService {
    @GET("meeting/{id}")
    Call<MeetingModel> getMeeting(@Path("id") int id);
}
