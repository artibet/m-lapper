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
import gr.artibet.lapper.api.SocketIO;
import gr.artibet.lapper.fragments.ActiveRacesFragment;
import gr.artibet.lapper.fragments.CanceledRacesFragment;
import gr.artibet.lapper.fragments.CompletedRacesFragment;
import gr.artibet.lapper.fragments.LiveDataFragment;
import gr.artibet.lapper.fragments.InProgressRacesFragment;
import gr.artibet.lapper.fragments.PendingRacesFragment;
import gr.artibet.lapper.fragments.SensorsFragment;
import gr.artibet.lapper.fragments.UsersFragment;
import gr.artibet.lapper.fragments.VehiclesFragment;
import gr.artibet.lapper.storage.SharedPrefManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    // TODO: Inherent new abstract fragment
    private Fragment mCurrentFragment;
    NavigationView mNavigationView;

    // fragment constants
    public static final int LIVEDATA = 1;
    public static final int PENDING_RACES = 2;
    public static final int ACTIVE_RACES = 3;
    public static final int INPROGRESS_RACES = 4;
    public static final int COMPLETED_RACES = 5;
    public static final int CANCELED_RACES = 6;
    public static final int VEHICLES = 7;
    public static final int SENSORS = 8;
    public static final int USERS = 9;

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
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Set menu items visibility for admin/user
        setMenuItemsVisible();

        // Set dashboard fragment if no fragment exist
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            int fragment = intent.getIntExtra("fragment", -1);
            createFragment(fragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        }

        // If mCurrentFragment is not null fetch data


        // TEST WEBSOCKETS
        //SocketIO.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: Add fragment fetch calls here
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
        if (mCurrentFragment == null || mCurrentFragment instanceof LiveDataFragment) {
            mCurrentFragment = new LiveDataFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
            getSupportActionBar().setTitle(R.string.live_data);
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
                if (!(mCurrentFragment instanceof LiveDataFragment)) {
                    mCurrentFragment = new LiveDataFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
                    getSupportActionBar().setTitle(R.string.live_data);
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

    // Create fragment
    private void createFragment(Integer fragment) {
        switch (fragment) {
            case LIVEDATA:
                mCurrentFragment = new LiveDataFragment();
                getSupportActionBar().setTitle(R.string.live_data);
                mNavigationView.setCheckedItem(R.id.dashboardFragment);
                break;
            case PENDING_RACES:
                mCurrentFragment = new PendingRacesFragment();
                getSupportActionBar().setTitle(R.string.pending_races);
                mNavigationView.setCheckedItem(R.id.pendingRacesFragment);
                break;
            case ACTIVE_RACES:
                mCurrentFragment = new ActiveRacesFragment();
                getSupportActionBar().setTitle(R.string.active_races);
                mNavigationView.setCheckedItem(R.id.activeRacesFragment);
                break;
            case INPROGRESS_RACES:
                mCurrentFragment = new InProgressRacesFragment();
                getSupportActionBar().setTitle(R.string.inprogress_races);
                mNavigationView.setCheckedItem(R.id.inProgressRacesFragment);
                break;
            case COMPLETED_RACES:
                mCurrentFragment = new CompletedRacesFragment();
                getSupportActionBar().setTitle(R.string.completed_races);
                mNavigationView.setCheckedItem(R.id.completedRacesFragment);
                break;
            case CANCELED_RACES:
                mCurrentFragment = new CanceledRacesFragment();
                getSupportActionBar().setTitle(R.string.canceled_races);
                mNavigationView.setCheckedItem(R.id.canceledRacesFragment);
                break;
            case VEHICLES:
                mCurrentFragment = new VehiclesFragment();
                getSupportActionBar().setTitle(R.string.vehicles);
                mNavigationView.setCheckedItem(R.id.vehiclesFragment);
                break;
            case SENSORS:
                mCurrentFragment = new SensorsFragment();
                getSupportActionBar().setTitle(R.string.sensors);
                mNavigationView.setCheckedItem(R.id.sensorsFragment);
                break;
            case USERS:
                mCurrentFragment = new UsersFragment();
                getSupportActionBar().setTitle(R.string.users);
                mNavigationView.setCheckedItem(R.id.usersFragment);
                break;
            default:
                mCurrentFragment = new LiveDataFragment();
                getSupportActionBar().setTitle(R.string.live_data);
                mNavigationView.setCheckedItem(R.id.dashboardFragment);
                break;
        }
    }
}
