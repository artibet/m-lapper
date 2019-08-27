package gr.artibet.lapper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText mTextUsername;
    EditText mTextpassword;
    Button mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTextUsername = findViewById(R.id.edittext_username);
        mTextpassword = findViewById(R.id.edittext_password);
        mButtonLogin = findViewById(R.id.button_login);

    }
}
