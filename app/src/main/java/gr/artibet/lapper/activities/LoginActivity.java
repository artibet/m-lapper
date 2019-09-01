package gr.artibet.lapper.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import gr.artibet.lapper.R;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.response.LoginResponse;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextUsername;
    private EditText mEditTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextUsername = findViewById(R.id.editTextUsername);
        mEditTextPassword = findViewById(R.id.editTextPassword);

        // Set click listener for login button
        findViewById(R.id.buttonLogin).setOnClickListener(this);

    }

    // If users is already logged in start main activity
    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startMainActivity();
            finish();
        }

    }


    // Login method
    public void login() {
        String username = mEditTextUsername.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();

        // username validation
        if (username.isEmpty()) {
            mEditTextUsername.setError(getString(R.string.username_required));
            mEditTextUsername.requestFocus();
            return;
        }

        // password validation
        if (password.isEmpty()) {
            mEditTextPassword.setError(getString(R.string.password_required));
            mEditTextPassword.requestFocus();
            return;
        }

        // Do user login through api
        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .login(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (!response.isSuccessful()) {
                    String errorMessage = getString(R.string.invalid_credentials);
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    LoginResponse resp = response.body();

                    // Store logged in user into shared preferences
                    SharedPrefManager.getInstance(LoginActivity.this).saveLoggedInUser(resp);

                    // Open Main Activity - Close all others
                    startMainActivity();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }

    // Open main activity
    // Called after login or if user is already logged in
    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // View.OnClickListener Implementation
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonLogin:
                login();
                break;
        };

    }


}
