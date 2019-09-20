package gr.artibet.lapper.api;

import java.util.List;

import gr.artibet.lapper.models.LiveData;
import gr.artibet.lapper.models.LoginUser;
import gr.artibet.lapper.models.Race;
import gr.artibet.lapper.models.RaceResponse;
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.models.User;
import gr.artibet.lapper.models.Vehicle;
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
import retrofit2.http.Query;

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

    // --------------------------------------------------------------------------
    // VEHICLES
    // --------------------------------------------------------------------------

    // Vehicle List
    @GET("api/vehicles/")
    Call<List<Vehicle>> getVehicles(@Header("Authorization") String token);

    // Vehicle POST (create new)
    @POST("api/vehicles/")
    @FormUrlEncoded
    Call<Vehicle> createVehicle(
            @Header("Authorization") String token,
            @Field("tag") String tag,
            @Field("owner") Long owner_id,
            @Field("driver") String driver,
            @Field("isactive") Boolean isActive,
            @Field("descr") String description
    );

    // Vehicle PUT (update)
    @PUT("api/vehicles/{id}/")
    @FormUrlEncoded
    Call<Vehicle> updateVehicle(
            @Header("Authorization") String token,
            @Path("id") long id,
            @Field("tag") String tag,
            @Field("owner") Long owner_id,
            @Field("driver") String driver,
            @Field("isactive") Boolean isActive,
            @Field("descr") String description
    );

    // Sensor DELETE
    @DELETE("api/vehicles/{id}/")
    Call<Void> deleteVehicle(@Header("Authorization") String token, @Path("id") long id);


    // --------------------------------------------------------------------------
    // RACES
    // --------------------------------------------------------------------------

    // Race GET
    @GET("api/races/")
    Call<RaceResponse> getRaces(
            @Header("Authorization") String token,
            @Query("state") int stateId
    );

    // Race DELETE
    @DELETE("api/races/{id}/")
    Call<Void> deleteRace(@Header("Authorization") String token, @Path("id") int id);

    // Activate race
    @POST("cmd/activate-race/")
    @FormUrlEncoded
    Call<Race> activateRace(
            @Header("Authorization") String token,
            @Field("race_id") int race_id
    );

    // Race POST (create new)
    @POST("api/races/")
    @FormUrlEncoded
    Call<Race> createRace(
            @Header("Authorization") String token,
            @Field("tag") String tag,
            @Field("laps") int laps,
            @Field("start") int start_method,
            @Field("mode") int mode,
            @Field("ispublic") Boolean isPublic,
            @Field("descr") String description
    );

    // Race PUT (update)
    @PUT("api/races/{id}/")
    @FormUrlEncoded
    Call<Race> updateRace(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Field("tag") String tag,
            @Field("laps") int laps,
            @Field("start") int start_method,
            @Field("mode") int mode,
            @Field("ispublic") Boolean isPublic,
            @Field("descr") String description
    );




    // --------------------------------------------------------------------------
    // RACES VEHICLES
    // --------------------------------------------------------------------------

    // Race vehicle GET (for specific race
    @GET("api/race-vehicles/")
    Call<List<RaceVehicle>> getRaceVehiclesForRace(
            @Header("Authorization") String token,
            @Query("race") int raceId
    );

    // Delete Race Vehicle
    @DELETE("api/race-vehicles/{id}/")
    Call<Void> deleteRaceVehicle(@Header("Authorization") String token, @Path("id") long rvId);


}
