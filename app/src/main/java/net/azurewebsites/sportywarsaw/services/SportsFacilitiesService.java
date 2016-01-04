package net.azurewebsites.sportywarsaw.services;

import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.models.AddMeetingModel;
import net.azurewebsites.sportywarsaw.models.ChangeMeetingModel;
import net.azurewebsites.sportywarsaw.models.SportFacilityPlusModel;
import net.azurewebsites.sportywarsaw.models.SportsFacilityModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * @author Marcin Chudy
 */
public interface SportsFacilitiesService {

    @GET("sportsFacilities/{id}")
    Call<SportsFacilityModel> getSportsFacility(@Path("id") int id);

    @GET("sportsFacilities/{id}/Details")
    Call<SportFacilityPlusModel> getDetails(@Path("id") int id);

    @GET("sportFacilities/Page")
    Call<List<SportsFacilityModel>> page();

    @GET("sportFacilities/All")
    Call<List<SportsFacilityModel>> getAll();

    @POST("sportFacilities")
    Call<ResponseBody> post(@Body SportFacilityPlusModel sportFacilityPlusModel);

    @PUT("sportFacilities")
    Call<ResponseBody> put(@Body SportFacilityPlusModel sportFacilityPlusModel);

    @DELETE("sportFacilities/{id}")
    Call<ResponseBody> delete(@Path("id") int id);

}
