package tohtotera.ab.auonenet.jp.kitchentimerservice;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.security.Provider;
import java.util.Timer;

public class MainActivity extends Activity {

    private class KitchenTimerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast toast = Toast.makeText(getApplicationContext(), "Time over!", Toast.LENGTH_LONG);
            toast.show();
            MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.alarm);
            try {
                mp.start();
            } catch (Exception e) {
                // 例外は発生しない
            }
        }
    }
    private KitchenTimerService kitchenTimerService;
    private final KitchenTimerReceiver receiver = new KitchenTimerReceiver();

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            kitchenTimerService = ((KitchenTimerService.KitchenTimerBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            kitchenTimerService = null;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TimePicker timerPicker = (TimePicker)findViewById(R.id.TimePicker01);
        timerPicker.setIs24HourView(true);
        timerPicker.setCurrentHour(0);
        timerPicker.setCurrentMinute(1);

        Button button = (Button)findViewById(R.id.Button01);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long hour = timerPicker.getCurrentHour();
                long min = timerPicker.getCurrentMinute();

                kitchenTimerService.schedule((hour * 60 + min) * 60 * 1000);
                moveTaskToBack(true);
            }
        });
        //サービスを開始
        Intent intent = new Intent(this, KitchenTimerService.class);
        startService(intent);
        IntentFilter filter = new IntentFilter(KitchenTimerService.ACTION);
        registerReceiver(receiver, filter);

        //サービスにバインド
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        //いったんｱﾝバインドしてから再度バインド
        unbindService(serviceConnection);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unbindService(serviceConnection);//バインド解除
        unregisterReceiver(receiver);     //登録解除
        kitchenTimerService.stopSelf(); //サービスは必要ないので終了
    }
}
