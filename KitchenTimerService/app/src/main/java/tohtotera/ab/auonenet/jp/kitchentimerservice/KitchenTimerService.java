package tohtotera.ab.auonenet.jp.kitchentimerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Weider on 2015/11/29.
 */
public class KitchenTimerService extends Service{

    class KitchenTimerBinder extends Binder {
        KitchenTimerService getService() {
            return KitchenTimerService.this;
        }
    }
    public static final String ACTION  = "Kitchen Timer Service";
    private Timer timer;

    @Override
    public void onCreate(){
        super.onCreate();
        Toast toast = Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT);
        toast.show();
        System.out.println("####### service onCreate() process:" + android.os.Process.myPid() + " task:" + android.os.Process.myTid());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast toast = Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT);
        toast.show();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        Toast toast = Toast.makeText(getApplicationContext(), "onBind()", Toast.LENGTH_SHORT);
        toast.show();
        return new KitchenTimerBinder();
    }

    @Override
    public boolean onUnbind(Intent intent){
        Toast toast = Toast.makeText(getApplicationContext(), "onUnbind()", Toast.LENGTH_SHORT);
        toast.show();
        return true; //再度クライアントから接続された際にonRebindを呼び出させる場合はtrueを返す
    }

    //クライアントから呼び出されるメソッド
    public void schedule(long delay){
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendBroadcast(new Intent(ACTION));
            }
        };
        timer.schedule(timerTask,delay);
    }
}
