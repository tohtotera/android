package com.example.kanehiro_acer.alarmnoti;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends Activity
        implements View.OnClickListener {

    private InputMethodManager mInputMethodManager;
    private RelativeLayout mLayout;
    private int notificationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edText = (EditText) findViewById(R.id.editText);
        //画面全体のレイアウト
        mLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        //キーボード表示を制御するためのオブジェクト
        mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //buttonを取得
        Button btnSet = (Button)findViewById(R.id.set);
        btnSet.setOnClickListener(this);
        Button btnCancel = (Button)findViewById(R.id.cancel);
        btnCancel.setOnClickListener(this);

    }

    // EditText編集時に背景をタップしたらキーボードを閉じるようにするタッチイベントの処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //キーボードを隠す
        // 予測変換表示もソフトキーボードも非表示にする
        mInputMethodManager.hideSoftInputFromWindow(mLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // 予測変換表示のみ非表示
        //mInputMethodManager.hideSoftInputFromWindow(mLayout.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        //背景にフォーカスを移す
        mLayout.requestFocus();

        return false;
    }

    @Override
    public void onClick(View v) {

        EditText edText = (EditText) findViewById(R.id.editText);
        // AlarmReceiverを呼び出すインテント
        Intent bootIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        // Notificationの識別子を渡す
        bootIntent.putExtra("notificationId", notificationId);
        // 追加データとして、やることを渡す
        bootIntent.putExtra("todo", edText.getText());

        // androidのALARMサービスからブロードキャストしてもらうため、
        // PendingIntent.getBroadcastでPendingIntentオブジェクトを取得しています。
        // PendingIntentはインテントをタイミングをみはからって発行します。
        // ブロードキャストを投げるPendingIntentを作成。
        // ActivityからでなくAndroidのALARMサービスからブロードキャストしてもらうため、PendingIntent.getBroadcast
        // でPendingIntentオブジェクトを取得しています
        PendingIntent alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, bootIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        TimePicker tPicker  =  (TimePicker)findViewById(R.id.timePicker);
        // tPicker.setIs24HourView(true);

        switch (v.getId()) {
            case R.id.set:

                int hour = tPicker.getCurrentHour();
                int minute = tPicker.getCurrentMinute();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                long alarmStartTime = startTime.getTimeInMillis();

                alarm.set(
                        //デバイスがスリープ状態の場合に alarm が動作した場合、alarm はデバイスを起こす
                        AlarmManager.RTC_WAKEUP,
                        alarmStartTime,
                        alarmIntent         // PendingIntent
                );
                Toast.makeText(MainActivity.this, "通知をセットしました！", Toast.LENGTH_SHORT).show();
                notificationId++;

                break;
            case R.id.cancel:
                alarm.cancel(alarmIntent);
                Toast.makeText(MainActivity.this, "通知をキャンセルしました!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
