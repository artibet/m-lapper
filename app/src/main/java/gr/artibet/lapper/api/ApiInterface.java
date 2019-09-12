package gr.artibet.lapper.api;

import java.util.List;

import gr.artibet.lapper.models.LiveData;
import gr.artibet.lapper.models.LoginUser;
import gr.artibet.lapper.models.Sensor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    // Login POST request
    @FormUrlEncoded
    @POST("api/auth-token/")
    Call<LoginUser> login(
            @Field("username") String username,
            @Field("password") String password
    );

    // Live Data
    @GET("api/live-data/")
    Call<List<LiveData>> getLiveData(@Header("Authorization") String token);

    // Sensor List
    @GET("api/sensors/")
    Call<List<Sensor>> getSensors(@Header("Authorization") String token);

    // Sensor POST (create new)
    @POST("api/sensors/")
    Call<Sensor> createSensor(@Header("Authorization") String token, @Body Sensor sensor);


}
