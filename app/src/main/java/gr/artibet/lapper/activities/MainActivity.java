package gr.artibet.lapper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import gr.artibet.lapper.R;
import gr.artibet.lapper.fragments.ActiveRacesFragment;
import gr.artibet.lapper.fragments.CanceledRacesFragment;
import gr.artibet.lapper.fragments.CompletedRacesFragment;
import gr.artibet.lapper.fragments.DashboardFragment;
import gr.artibet.lapper.fragments.InProgressRacesFragment;
import gr.artibet.lapper.fragments.PendingRacesFragment;
import gr.artibet.lapper.fragments.SensorsFragment;
import gr.artibet.lapper.fragments.UsersFragment;
import gr.artibet.lapper.fragments.VehiclesFragment;
import gr.artibet.lapper.storage.SharedPrefManager;

import static androidx.navigation.ui.AppBarConfiguration.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup navigation drawer hamburger icon
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
               this,
               mDrawerLayout,
               toolbar,
               R.string.navigation_drawer_open,
               R.string.navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation view listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set dashboard fragment if no fragment exist
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
            getSupportActionBar().setTitle(R.string.dashboard);
            navigationView.setCheckedItem(R.id.dashboardFragment);
        }

    }

    // If back button pressed and drawer is open close it
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            actionExit();
        }
    }

    // Option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    // Option menu item actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                actionLogout();
                return true;

            case R.id.action_exit:
                actionExit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


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

    // Logout action
    private void actionLogout() {
        SharedPrefManager.getInstance(this).clearLoggedInUser();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Exit action with confirmation
    private void actionExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.exit_application);

        // Exit button
        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Cancel button
        builder.setNegativeButton(R.string.cancel_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Show confirmation dialog
        builder.show();
    }

    // Navigation drawer items selected listener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {

            case R.id.dashboardFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
                getSupportActionBar().setTitle(R.string.dashboard);
                break;

            case R.id.pendingRacesFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PendingRacesFragment()).commit();
                getSupportActionBar().setTitle(R.string.pending_races);
                break;

            case R.id.activeRacesFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ActiveRacesFragment()).commit();
                getSupportActionBar().setTitle(R.string.active_races);
                break;

            case R.id.inProgressRacesFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InProgressRacesFragment()).commit();
                getSupportActionBar().setTitle(R.string.inprogress_races);
                break;

            case R.id.completedRacesFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CompletedRacesFragment()).commit();
                getSupportActionBar().setTitle(R.string.completed_races);
                break;

            case R.id.canceledRacesFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CanceledRacesFragment()).commit();
                getSupportActionBar().setTitle(R.string.canceled_races);
                break;

            case R.id.vehiclesFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VehiclesFragment()).commit();
                getSupportActionBar().setTitle(R.string.vehicles);
                break;

            case R.id.sensorsFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SensorsFragment()).commit();
                getSupportActionBar().setTitle(R.string.sensors);
                break;

            case R.id.usersFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UsersFragment()).commit();
                getSupportActionBar().setTitle(R.string.users);
                break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
