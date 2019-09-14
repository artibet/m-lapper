package gr.artibet.lapper.api;

import java.util.List;

import gr.artibet.lapper.models.LiveData;
import gr.artibet.lapper.models.LoginUser;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    // --------------------------------------------------------------------------
    // SENSORS
    // --------------------------------------------------------------------------

    // Sensor List
    @GET("api/sensors/")
    Call<List<Sensor>> getSensors(@Header("Authorization") String token);

    // Sensor POST (create new)
    @POST("api/sensors/")
    Call<Sensor> createSensor(@Header("Authorization") String token, @Body Sensor sensor);

    // Sensor PUT (update)
    @PUT("api/sensors/{id}/")
    Call<Sensor> updateSensor(@Header("Authorization") String token, @Path("id") long id, @Body Sensor sensor);

    // Sensor DELETE
    @DELETE("api/sensors/{id}/")
    Call<Void> deleteSensor(@Header("Authorization") String token, @Path("id") long id);

    // --------------------------------------------------------------------------
    // USERS
    // --------------------------------------------------------------------------

    // User List
    @GET("api/users/")
    Call<List<User>> getUsers(@Header("Authorization") String token);

    // User POST (create new)
    @POST("api/users/")
    Call<User> createUser(@Header("Authorization") String token, @Body User user);

    // User PUT (update)
    @PUT("api/users/{id}/")
    Call<User> updateUser(@Header("Authorization") String token, @Path("id") long id, @Body User user);

    // User DELETE
    @DELETE("api/users/{id}/")
    Call<Void> deleteUser(@Header("Authorization") String token, @Path("id") long id);

    // Reset password
    @POST("api/users/reset-password/")
    @FormUrlEncoded
    Call<Void> resetPassword(
            @Header("Authorization") String token,
            @Field("username") String username,
            @Field("password") String password
    );
}
