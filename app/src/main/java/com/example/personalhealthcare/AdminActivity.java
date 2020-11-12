package com.example.personalhealthcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

public class AdminActivity extends AppCompatActivity {

    private Integer UserID = new Integer(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_food, R.id.navigation_admin_settings, R.id.navigation_feedback)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }
    }

}
