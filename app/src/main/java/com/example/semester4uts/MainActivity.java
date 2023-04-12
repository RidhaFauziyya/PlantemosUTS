package com.example.semester4uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;

    Boolean isFragmentDisplayed = false;

    Button btn_full, btn_spring, btn_low;

    ImageView imageStyle;
    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences = null;

    static ArrayList<String> arrayList = new ArrayList<>();
    MainAdapter adapter;

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable;
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.bt_menu);
        recyclerView = findViewById(R.id.recycle_view);

        btn_full = findViewById(R.id.type_full);
        btn_spring = findViewById(R.id.type_spring);
        btn_low = findViewById(R.id.type_low);

        ImageView btn_lang = findViewById(R.id.change_language);
        btn_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(languageIntent);
            }
        });

        //Clear array list
        arrayList.clear();

        //Ad menu item in array list
        arrayList.add("Home");
        arrayList.add("Recomendation");
        arrayList.add("Logout");

        //Initialize adapter
        adapter = new MainAdapter(this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        btn_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentDisplayed){
                    displayFragmentFull();
                } else{
                    closeFragmentFull();
                }
            }
        });

        btn_spring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentDisplayed){
                    displayFragmentSpring();
                } else{
                    closeFragmentSpring();
                }
            }
        });

        btn_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentDisplayed){
                    displayFragmentLow();
                } else{
                    closeFragmentLow();
                }
            }
        });

        switchCompat = findViewById(R.id.switchCompat);
        imageStyle = findViewById(R.id.image_style);
        sharedPreferences = getSharedPreferences("night", 0);
        Boolean booleanValue = sharedPreferences.getBoolean("night_mode",false);


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                    imageStyle.setImageResource(R.drawable.night);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",true);
                    editor.commit();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                    imageStyle.setImageResource(R.drawable.day);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",false);
                    editor.commit();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public void displayFragmentFull(){
        FragmentFull showMoreFragment = FragmentFull.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_full, showMoreFragment).addToBackStack(null).commit();
        btn_full.setText(R.string.show_less);
        isFragmentDisplayed = true;
    }

    public void closeFragmentFull(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentFull showMoreFragment = (FragmentFull) fragmentManager
                .findFragmentById(R.id.fragment_container_full);
        if (showMoreFragment != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(showMoreFragment).commit();
        }
        btn_full.setText(R.string.full_light_type);
        isFragmentDisplayed = false;
    }

    public void displayFragmentSpring(){
        FragmentSpring showMoreFragment = FragmentSpring.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_spring, showMoreFragment).addToBackStack(null).commit();
        btn_spring.setText(R.string.show_less);
        isFragmentDisplayed = true;
    }

    public void closeFragmentSpring(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentSpring showMoreFragment = (FragmentSpring) fragmentManager
                .findFragmentById(R.id.fragment_container_spring);
        if (showMoreFragment != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(showMoreFragment).commit();
        }
        btn_spring.setText(R.string.spring_light_type);
        isFragmentDisplayed = false;
    }

    public void displayFragmentLow(){
        FragmentLow showMoreFragment = FragmentLow.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_low, showMoreFragment).addToBackStack(null).commit();
        btn_low.setText(R.string.show_less);
        isFragmentDisplayed = true;
    }

    public void closeFragmentLow(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentLow showMoreFragment = (FragmentLow) fragmentManager
                .findFragmentById(R.id.fragment_container_low);
        if (showMoreFragment != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(showMoreFragment).commit();
        }
        btn_low.setText(R.string.low_light_type);
        isFragmentDisplayed = false;
    }




}