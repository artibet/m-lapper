package gr.artibet.lapper.api;

import gr.artibet.lapper.models.response.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    // Login POST request
    @FormUrlEncoded
    @POST("api/auth-token/")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );


}
