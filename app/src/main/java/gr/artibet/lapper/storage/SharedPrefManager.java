package gr.artibet.lapper.storage;

import android.content.Context;
import android.content.SharedPreferences;

import gr.artibet.lapper.models.response.LoginResponse;

// Shared preferences  client SINGLETON class
public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "lapper_shared_prefs";

    private static SharedPrefManager mInstance;
    private Context mContext;

    // private constructor - not to be called from outside
    private SharedPrefManager(Context context) {
        this.mContext = context;
    }

    // Synchronized method to get instance
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    // Store logged in user
    public void saveLoggedInUser(LoginResponse loginUser) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("token", loginUser.getToken());
        editor.putInt("id", loginUser.getId());
        editor.putString("username", loginUser.getUsername());
        editor.putString("firstName", loginUser.getFirstName());
        editor.putString("lastName", loginUser.getLastName());
        editor.putString("email", loginUser.getEmail());
        editor.putBoolean("isSuperuser", loginUser.isSuperuser());
        editor.putBoolean("isStaff", loginUser.isSuperuser());

        editor.apply();
    }

    // Check if user is logged in - If yes, id exists and is greater than zero.
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;
    }

    // Check if user is admin
    public boolean isAdmin() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isSuperuser", false);
    }

    // Get logged in user - It's an instance of LoginResponse
    public LoginResponse getLoggedInUser() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new LoginResponse(
                sharedPreferences.getString("token", null),
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("firstName", null),
                sharedPreferences.getString("lastName", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getBoolean("isSuperuser", false),
                sharedPreferences.getBoolean("isStaff", false)
        );
    }

    // Logout - Clear shared prefs
    public void clearLoggedInUser() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("token");
        editor.remove("id");
        editor.remove("username");
        editor.remove("firstName");
        editor.remove("lastName");
        editor.remove("email");
        editor.remove("isSuperuser");
        editor.remove("isStaff");

        editor.apply();
    }
}
