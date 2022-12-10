package gr.artibet.lapper.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Retrofit client SINGLETON class
public class RetrofitClient {

    private static final String BASE_URL = "https://lapper-api.artibet.gr/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    // private constructor - not to be called from outside
    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    // Synchronized method to get instance
    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    // Create and return API
    public ApiInterface getApi() {
        return retrofit.create(ApiInterface.class);
    }
}
