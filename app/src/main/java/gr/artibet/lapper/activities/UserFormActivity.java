package gr.artibet.lapper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.User;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFormActivity extends AppCompatActivity {

    // Form views
    private TextInputEditText mUsername;
    private TextInputLayout mPasswordLayout;
    private TextInputEditText mPassword;
    private TextInputEditText mLastName;
    private TextInputEditText mFirstName;
    private TextInputEditText mEmail;
    private CheckBox mIsAdmin;
    private Switch mIsActive;

    private boolean mCreate = false;
    User mUser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        // If user extra exist edit, else create new
        Intent intent = getIntent();
        String json = intent.getStringExtra("user");
        if (json != null) {
            mUser = new Gson().fromJson(json, User.class);
            mCreate = false;
        }
        else {
            mUser = new User();
            mCreate = true;
        }


        // Get views
        mUsername = findViewById(R.id.editTextUsername);
        mPasswordLayout = findViewById(R.id.password_layout);
        mPassword = findViewById(R.id.editTextPassword);
        mLastName = findViewById(R.id.editTextLastName);
        mFirstName = findViewById(R.id.editTextFirstName);
        mEmail = findViewById(R.id.editTextEmail);
        mIsAdmin = findViewById(R.id.checkBoxIsAdmin);
        mIsActive = findViewById(R.id.switchIsActive);

        // Setup action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivOk = toolbar.findViewById(R.id.ivOk);
        ImageView ivCancel = toolbar.findViewById(R.id.ivCancel);
        TextView ivTitle = toolbar.findViewById(R.id.ivTitle);

        // Set actionbar and data if editing
        if (mCreate) {
            ivTitle.setText(getString(R.string.add_user));
        }
        else {
            ivTitle.setText(getString(R.string.edit_user));
            mUsername.setText(mUser.getUsername());
            ((LinearLayout)mPasswordLayout.getParent()).removeView(mPasswordLayout);  // hide password field
            mLastName.setText(mUser.getLastName());
            mFirstName.setText(mUser.getFirstName());
            mEmail.setText(mUser.getEmail());
            mIsAdmin.setChecked(mUser.isSuperUser());
            mIsActive.setChecked(mUser.isActive());
        }

        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    // Go back
    private void cancel() {
        onBackPressed();
    }

    // Save User
    private void saveUser() {

        // Get field values
        String username = mUsername.getText().toString().trim();
        String password = null;
        if (mCreate) password = mPassword.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String firstName = mFirstName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        Boolean isAdmin = mIsAdmin.isChecked();
        Boolean isActive = mIsActive.isChecked();

        // Username validation
        if (username.isEmpty()) {
            mUsername.setError(getString(R.string.required_field));
            mUsername.requestFocus();
            return;
        }

        // Password validation
        if (mCreate && password.isEmpty()) {
            mPassword.setError(getString(R.string.required_field));
            mPassword.requestFocus();
            return;
        }

        // Update sensor fields
        mUser.setUsername(username);
        mUser.setPassword(password);
        mUser.setLastName(lastName);
        mUser.setFirstName(firstName);
        mUser.setEmail(email);
        mUser.setActive(isActive);
        mUser.setSuperUser(isAdmin);

        if (mCreate) {
            createUser();
        }
        else {
            updateUser();
        }

    }

    // Send post request to create new user
    private void createUser() {
        String token = SharedPrefManager.getInstance(this).getToken();
        Call<User> call = RetrofitClient.getInstance().getApi().createUser(token, mUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                    Util.errorToast(UserFormActivity.this, response.message());
                }
                else {
                    //Util.successToast(SensorFormActivity.this, getString(R.string.sensor_create_success));

                    // Open MainActivity and set fragment to sensors
                    Intent intent = new Intent(UserFormActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("fragment", MainActivity.USERS);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Util.errorToast(UserFormActivity.this, t.getMessage());
            }
        });

    }

    // Send put request to update existing user
    private void updateUser() {
        String token = SharedPrefManager.getInstance(this).getToken();
        Call<User> call = RetrofitClient.getInstance().getApi().updateUser(token, mUser.getId(), mUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                    Util.errorToast(UserFormActivity.this, response.message());
                }
                else {
                    //Util.successToast(SensorFormActivity.this, getString(R.string.sensor_create_success));

                    // Open MainActivity and set fragment to sensors
                    Intent intent = new Intent(UserFormActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("fragment", MainActivity.USERS);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Util.errorToast(UserFormActivity.this, t.getMessage());
            }
        });

    }
}
