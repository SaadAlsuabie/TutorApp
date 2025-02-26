package com.edu.tutorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.edu.tutorapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final String SHARED_PREF_NAME = "MyAppPreferences"; // Name of the preference file
    private static final String KEY_USERNAME = "username"; // Key for storing username
    private static final String KEY_ROLE = "role"; // Key for storing role
    private static final String KEY_ACCESS_TOKEN = "access_token"; // Key for storing access token
    private static final String KEY_REFRESH_TOKEN = "refresh_token"; // Key for storing refresh token
    private static final String KEY_TIMEOUT = "timeout"; // Key for storing timeout
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_s_dashboard, R.id.nav_findtutor, R.id.nav_mysessions,
                R.id.nav_reclectures, R.id.nav_messages, R.id.nav_payments,
                R.id.nav_s_profile
                )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(KEY_ROLE, "none");
                editor.putString(KEY_USERNAME, "none");
                editor.putString(KEY_ACCESS_TOKEN, "none");
                editor.putString(KEY_REFRESH_TOKEN, "none");
                editor.apply();
                startActivity(authIntent);
//                finish();
                return true;
            } else {
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    drawer.closeDrawer(GravityCompat.START); // Close the drawer after navigation
                }
                return handled;
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}