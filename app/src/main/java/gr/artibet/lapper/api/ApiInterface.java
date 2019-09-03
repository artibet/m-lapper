package gr.artibet.lapper.api;

import java.util.List;

import gr.artibet.lapper.models.LiveData;
import gr.artibet.lapper.models.LoginUser;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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


}
