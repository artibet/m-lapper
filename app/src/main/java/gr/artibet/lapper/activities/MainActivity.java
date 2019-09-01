package gr.artibet.lapper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import gr.artibet.lapper.R;
import gr.artibet.lapper.storage.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private NavController mNavController;
    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Setup navigationUI
        mNavController = Navigation.findNavController(this, R.id.fragment);
        mNavView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(mNavView, mNavController);

        // Setup action bar
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mDrawerLayout);

    }

    // Navigation drawer
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mDrawerLayout);
    }

    // Option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    // If user not logged in, open login activity
    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
