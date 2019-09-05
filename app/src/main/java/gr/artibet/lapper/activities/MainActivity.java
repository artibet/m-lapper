package gr.artibet.lapper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private Fragment mCurrentFragment;

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

        // Set menu items visibility for admin/user
        setMenuItemsVisible();

        // Set dashboard fragment if no fragment exist
        if (savedInstanceState == null) {
            mCurrentFragment = new DashboardFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
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

            case R.id.action_refresh:
                actionRefresh();
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

    // Send refresh to current fragment
    private void actionRefresh() {
        if (mCurrentFragment == null || mCurrentFragment instanceof DashboardFragment) {
            mCurrentFragment = new DashboardFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
            getSupportActionBar().setTitle(R.string.dashboard);
        }
        else if (mCurrentFragment instanceof PendingRacesFragment) {
            mCurrentFragment = new PendingRacesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        }
        else if (mCurrentFragment instanceof ActiveRacesFragment) {
            mCurrentFragment = new ActiveRacesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        }
        else if (mCurrentFragment instanceof InProgressRacesFragment) {
            mCurrentFragment = new InProgressRacesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        }
        else if (mCurrentFragment instanceof CompletedRacesFragment) {
            mCurrentFragment = new CompletedRacesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        }
        else if (mCurrentFragment instanceof CanceledRacesFragment) {
            mCurrentFragment = new CanceledRacesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        }
        else if (mCurrentFragment instanceof VehiclesFragment) {
            mCurrentFragment = new VehiclesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        }
        else if (mCurrentFragment instanceof SensorsFragment) {
            mCurrentFragment = new SensorsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        }
        else if (mCurrentFragment instanceof UsersFragment) {
            mCurrentFragment = new UsersFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        }

    }

    // Navigation drawer items selected listener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {

            case R.id.dashboardFragment:
                if (!(mCurrentFragment instanceof DashboardFragment)) {
                    mCurrentFragment = new DashboardFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.dashboard);
                }
                break;

            case R.id.pendingRacesFragment:
                if (!(mCurrentFragment instanceof PendingRacesFragment)) {
                    mCurrentFragment = new PendingRacesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.pending_races);
                }
                break;

            case R.id.activeRacesFragment:
                if (!(mCurrentFragment instanceof ActiveRacesFragment)) {
                    mCurrentFragment = new ActiveRacesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.active_races);
                }
                break;

            case R.id.inProgressRacesFragment:
                if (!(mCurrentFragment instanceof InProgressRacesFragment)) {
                    mCurrentFragment = new InProgressRacesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.inprogress_races);
                }
                break;

            case R.id.completedRacesFragment:
                if (!(mCurrentFragment instanceof CompletedRacesFragment)) {
                    mCurrentFragment = new CompletedRacesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.completed_races);
                }
                break;

            case R.id.canceledRacesFragment:
                if (!(mCurrentFragment instanceof CanceledRacesFragment)) {
                    mCurrentFragment = new CanceledRacesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.canceled_races);
                }
                break;

            case R.id.vehiclesFragment:
                if (!(mCurrentFragment instanceof VehiclesFragment)) {
                    mCurrentFragment = new VehiclesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.vehicles);
                }
                break;

            case R.id.sensorsFragment:
                if (!(mCurrentFragment instanceof SensorsFragment)) {
                    mCurrentFragment = new SensorsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.sensors);
                }
                break;

            case R.id.usersFragment:
                if (!(mCurrentFragment instanceof UsersFragment)) {
                    mCurrentFragment = new UsersFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.users);
                }
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Create - Recreate fragments

    // Hide menu items if user is not admin
    private void setMenuItemsVisible() {
        if (!SharedPrefManager.getInstance(this).isAdmin()) {
            NavigationView navView = findViewById(R.id.nav_view);
            Menu menu = navView.getMenu();
            menu.findItem(R.id.sensorsFragment).setVisible(false);
            menu.findItem(R.id.usersFragment).setVisible(false);
        }
    }

}
