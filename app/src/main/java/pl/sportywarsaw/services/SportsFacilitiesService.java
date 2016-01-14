package pl.sportywarsaw.services;

import com.squareup.okhttp.ResponseBody;

import pl.sportywarsaw.models.SportFacilityPlusModel;
import pl.sportywarsaw.models.SportsFacilityModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * @author Marcin Chudy
 */
public interface SportsFacilitiesService {

    @GET("sportsFacilities/{id}")
    Call<SportsFacilityModel> getSportsFacility(@Path("id") int id);

    @GET("sportsFacilities/{id}/Details")
    Call<SportFacilityPlusModel> getDetails(@Path("id") int id);

    @GET("sportsFacilities/Page")
    Call<List<SportsFacilityModel>> getPage( @Query("index") int index,
                                             @Query("size") int size);

    @GET("sportsFacilities/Page")
    Call<List<SportsFacilityModel>> getPageFiltered( @Query("index") int index,
                                                     @Query("size") int size,
                                                     @Query("nameFilter") String filter);

    @GET("sportsFacilities/All")
    Call<List<SportsFacilityModel>> getAll();

    @POST("sportsFacilities")
    Call<ResponseBody> post(@Body SportFacilityPlusModel sportFacilityPlusModel);

    @PUT("sportsFacilities")
    Call<ResponseBody> put(@Body SportFacilityPlusModel sportFacilityPlusModel);

    @DELETE("sportsFacilities/{id}")
    Call<ResponseBody> delete(@Path("id") int id);

}
