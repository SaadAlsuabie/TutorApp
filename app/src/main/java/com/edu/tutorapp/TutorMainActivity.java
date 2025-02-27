package com.edu.tutorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.edu.tutorapp.databinding.ActivityMainBinding;
import com.edu.tutorapp.databinding.ActivityTutorMainBinding;
import com.edu.tutorapp.databinding.AppBarTutorMainBinding;
import com.edu.tutorapp.utils.SharedPreferencesUtils;
import com.google.android.material.navigation.NavigationView;

public class TutorMainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private AppBarTutorMainBinding tAppBarConfiguratiom;
    private ActivityTutorMainBinding binding;

    private SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTutorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarTutorMain.toolbar);
        DrawerLayout drawer = binding.drawerLayoutTutor;
        NavigationView navigationView = binding.navTutorView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tutor_dashboard, R.id.nav_tutor_requests, R.id.nav_tutor_sessions,
                R.id.nav_tutor_records, R.id.nav_tutor_messages, R.id.nav_tutor_earnings,
                R.id.nav_tutor_profile
                )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_tutor_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this,navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_tutor_logout) {
                Intent authIntent = new Intent(TutorMainActivity.this, AuthActivity.class);

                sharedPreferencesUtils = new SharedPreferencesUtils(this);
                sharedPreferencesUtils.saveRole("none");
                sharedPreferencesUtils.saveUsername("none");
                sharedPreferencesUtils.saveAccessToken("none");
                sharedPreferencesUtils.saveRefreshToken("none");
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.tutormain, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_tutor_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}