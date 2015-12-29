package net.azurewebsites.sportywarsaw.services;

import net.azurewebsites.sportywarsaw.models.SportsFacilityModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * @author Marcin Chudy
 */
public interface SportsFacilitiesService {

    @GET("sportsFacilities/{id}")
    Call<SportsFacilityModel> getSportsFacility(@Path("id") int id);
}
