package com.example.sensorreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensorLight;
    private Sensor mSensorProximity;
    private Sensor mSensorAccelerometer;
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;
    private TextView mTextSensorAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorText = new StringBuilder();

        for (Sensor currentSensor : sensorList) {
            sensorText.append(currentSensor.getName()).
                    append(System.getProperty("line.separator"));
        }

        TextView sensorTextView = findViewById(R.id.sensor_list);
        sensorTextView.setText(sensorText);

        mTextSensorLight = findViewById(R.id.label_light);
        mTextSensorProximity = findViewById(R.id.label_proximity);
        mTextSensorAccelerometer = findViewById(R.id.label_accelerometer);

        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        String sensor_error = "No sensor";
        if (mSensorLight == null) {
            mTextSensorLight.setText(sensor_error);
        }
        if (mSensorProximity == null) {
            mTextSensorProximity.setText(sensor_error);
        }
        if (mSensorAccelerometer == null) {
            mTextSensorAccelerometer.setText(sensor_error);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSensorProximity != null){
            mSensorManager.registerListener(this, mSensorProximity,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorLight != null){
            mSensorManager.registerListener(this, mSensorLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorAccelerometer !=null){
            mSensorManager.registerListener(this, mSensorAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                mTextSensorLight.setText(
                        String.format("Light sensor : %1$.2f", currentValue));
                changedBackgroundColorLight(currentValue);
                break;
            case Sensor.TYPE_PROXIMITY:
                mTextSensorProximity.setText(
                        String.format("Proximity sensor : %1$.2f", currentValue));
                changedBackgroundColorProximity(currentValue);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                mTextSensorAccelerometer.setText(
                        String.format("Accelerometer sensor : %1.2f", currentValue));
            default:
        }
    }

    private void changedBackgroundColorLight (float currentValue) {
        ConstraintLayout layout = findViewById(R.id.constraint_layout);
        if (currentValue >= 5000 && currentValue <= 10000) {
            layout.setBackgroundColor(Color.RED);
        } else if (currentValue > 0 && currentValue < 5000){
            layout.setBackgroundColor(Color.YELLOW);
        }
    }

    private void changedBackgroundColorProximity (float currentValue) {
        ConstraintLayout layout = findViewById(R.id.constraint_layout);
        if (currentValue >= 5 && currentValue <= 10) {
            layout.setBackgroundColor(Color.GREEN);
        } else if (currentValue > 0 && currentValue < 5){
            layout.setBackgroundColor(Color.MAGENTA);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}