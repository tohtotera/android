package tohtotera.ab.auonenet.jp.servicesample1;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

/**
 * Created by Weider on 2015/12/06.
 */
public class ServiceSample1Service extends Service {
    private Timer timer;
    private int countTime;
    private int stopTime;

    public IBinder onBind(Intent arg0){
        return null;
    }
    //サービスの起動
    public void onCreate(){
        super.onCreate();
        Toast.makeText(this, "サービスを起動します。", Toast.LENGTH_SHORT).show();
        timer = new Timer();
        countTime = 0;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "サービスを開始します。", Toast.LENGTH_SHORT).show();

        //タイマーを10秒ごとにrunメソッドが実行されるように設定
        MyTimerTask task = new MyTimerTask();
        timer.schedule(task, 10000, 10000);

        //終了時間取得
        Bundle bundle = intent.getExtras();
        stopTime = Integer.parseInt(bundle.getString("STOPTIME"));

        return START_STICKY;
    }

    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "サービスを終了します。", Toast.LENGTH_SHORT).show();
        timer.cancel();
        timer.purge();
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg){
            Toast.makeText(ServiceSample1Service.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    class MyTimerTask extends TimerTask{
        public void run(){
            countTime += 10;
            if (countTime / 60 == stopTime){
                //サービス終了
                stopSelf();
            }else{
                handler.sendMessage(Message.obtain(handler, 0, countTime/60 + "分" + countTime%60 + "秒経過"));
            }
        }
    }
}

