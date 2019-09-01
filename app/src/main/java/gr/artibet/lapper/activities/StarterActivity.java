package gr.artibet.lapper.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import gr.artibet.lapper.storage.SharedPrefManager;

public class StarterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent nextActivity;
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            // Start MainActivity
            nextActivity = new Intent(this, MainActivity.class);
        }
        else {
            // Start LoginActivity
            nextActivity = new Intent(this, LoginActivity.class);
        }

        // Start nextActivity and finish
        nextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(nextActivity);
        finish();
    }
}
