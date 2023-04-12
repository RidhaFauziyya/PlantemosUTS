package com.example.semester4uts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RecomDetail extends AppCompatActivity implements SensorEventListener {

    //Initialize variable
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView recyclerView;

    private SensorManager mSensorManager;
    private Sensor mSensorLight;
    private TextView mTextSensorLight;

    TextView type, desc, resultRange, plant1, plant2, plant3, resultState;
    ImageView image1 , image2, image3;

    float currentValue;

    ImageView imageStyle;
    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recom_detail);

        ImageView btn_lang = findViewById(R.id.change_language);
        btn_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(languageIntent);
            }
        });

        //Assign variable;
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.bt_menu);
        recyclerView = findViewById(R.id.recycle_view);

        type = findViewById(R.id.result_type);
        desc = findViewById(R.id.result_desc);
        resultRange = findViewById(R.id.result_light_range);
        plant1 = findViewById(R.id.name_full_1);
        plant2 = findViewById(R.id.name_full_2);
        plant3 = findViewById(R.id.name_full_3);

        image1 = findViewById(R.id.image_full_1);
        image2 = findViewById(R.id.image_full_2);
        image3 = findViewById(R.id.image_full_3);

        resultState = findViewById(R.id.result_location);
        Intent intent = getIntent();
        String states = intent.getStringExtra("stateValue");

        resultState.setText(states);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mTextSensorLight = findViewById(R.id.result_light);
        String sensor_error = "No Sensor";
        if (mSensorLight == null){
            mTextSensorLight.setText(sensor_error);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainAdapter(this, MainActivity.arrayList));

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
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

    public void onStart() {
        super.onStart();
        //Untuk memberi informasi bahwa sensor di update
        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        currentValue = event.values[0];

        if (currentValue >= 400 ){
            type.setText(R.string.full_light_type);
            desc.setText(R.string.desc_full);
            resultRange.setText(R.string.range_full);
            plant1.setText(R.string.full_1);
            plant2.setText(R.string.full_2);
            plant3.setText(R.string.full_3);
            image1.setImageResource(R.drawable.palem_kuning);
            image2.setImageResource(R.drawable.lili_paris);
            image3.setImageResource(R.drawable.rumput_payung);

        }else if(currentValue >= 200 && currentValue < 400){
            type.setText(R.string.spring_light_type);
            desc.setText(R.string.desc_spring);
            resultRange.setText(R.string.range_spring);
            plant1.setText(R.string.spring_1);
            plant2.setText(R.string.spring_2);
            plant3.setText(R.string.spring_3);
            image1.setImageResource(R.drawable.africa_violet);
            image2.setImageResource(R.drawable.adam_hawa);
            image3.setImageResource(R.drawable.keladi_hias);

        }else if(currentValue < 200){
            type.setText(R.string.low_light_type);
            desc.setText(R.string.desc_low);
            resultRange.setText(R.string.range_low);
            plant1.setText(R.string.low_1);
            plant2.setText(R.string.low_2);
            plant3.setText(R.string.low_3);
            image1.setImageResource(R.drawable.lidah_mertua);
            image2.setImageResource(R.drawable.pakis_kelabang);
            image3.setImageResource(R.drawable.pakis_kriting);
        }else{

        }

        switch (sensorType){
            case Sensor.TYPE_LIGHT:
                mTextSensorLight.setText(
                        String.format("%1$.2f", currentValue));
                break;
            default:
        }

    }




    /**
     * Called when the accuracy of the registered sensor has changed.  Unlike
     * onSensorChanged(), this is only called when this accuracy value changes.
     *
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void onStop(){
        super.onStop();
        //Menghentikan event, dengan mengunregister listenernya
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}