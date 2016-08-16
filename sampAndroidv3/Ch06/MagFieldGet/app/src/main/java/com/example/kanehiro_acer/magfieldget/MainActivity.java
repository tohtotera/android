package com.example.kanehiro_acer.magfieldget;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity
        implements SensorEventListener {
    // センサーマネージャ
    private SensorManager mSensorManager;
    private Sensor mMagField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // センサーマネージャのインスタンスを取得
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mMagField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mMagField,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO 自動生成されたメソッド・スタブ
        StringBuilder StrBuild = new StringBuilder();

        StrBuild.append("X方向:" + event.values[0] + "μＴ\n");
        StrBuild.append("Y方向:" + event.values[1] + "μＴ\n");
        StrBuild.append("Z方向:" + event.values[2] + "μＴ\n");
        TextView txt01 = (TextView)findViewById(R.id.txt01);
        txt01.setText(StrBuild.toString());

    }

}
