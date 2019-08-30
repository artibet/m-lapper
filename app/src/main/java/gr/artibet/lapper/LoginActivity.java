package gr.artibet.lapper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEditTextUsername;
    EditText mEditTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextUsername = findViewById(R.id.editTextUsername);
        mEditTextPassword = findViewById(R.id.editTextPassword);

        // Set click listener for login button
        findViewById(R.id.buttonLogin).setOnClickListener(this);

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


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonLogin:
                login();
                break;
        };

    }


}
