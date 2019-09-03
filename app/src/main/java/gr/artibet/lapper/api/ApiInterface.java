package gr.artibet.lapper.api;

import gr.artibet.lapper.models.LoginUser;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    // Login POST request
    @FormUrlEncoded
    @POST("api/auth-token/")
    Call<LoginUser> login(
            @Field("username") String username,
            @Field("password") String password
    );


}
