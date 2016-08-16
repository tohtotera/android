package tohtotera.ab.auonenet.jp.surfaceviewex;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Weider on 2015/11/24.
 */
public class SurfaceViewView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder holder;
    private Thread        thread;
    private Bitmap image;
    private int px = (new View(getContext()).getWidth())/2;
    private int py = (new View(getContext()).getHeight())/2;
    private int vx = 10;
    private int vy = 10;
    //コンストラクタ
    public SurfaceViewView(Context context){
        super(context);
        //画像の読み込み
        Resources r = getResources();
        image = BitmapFactory.decodeResource(r, R.drawable.sample);

        //サーフェイスホルダの生成
        holder = getHolder();
        holder.addCallback(this);//サーフェイスホルダに変化があった時に通知する宛先を引数で指定(自分自身）
    }
    //サーフェイスの生成
    public void surfaceCreated(SurfaceHolder holder){
        //スレッドの開始
        thread = new Thread(this);
        thread.start();
    }
    //サーフェイスの変更
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
        //do nothing
    }
    //サーフェイスの破棄
    public void surfaceDestroyed(SurfaceHolder holder){
        thread = null;
    }
    //ループ処理
    //threadオブジェクトのstartメソッドが呼ばれると同時に、このrunメソッドが
    //走りだしthreadオブジェクトが破棄されるまで無限ループで実行される。
    public void run(){
        while (thread != null){
            long nextTime = System.currentTimeMillis() + 30;
            onTick();
            try{
                Thread.sleep(nextTime - System.currentTimeMillis());
            }catch (Exception e){
                //do nothing
            }
        }
    }
    //定期処理
    private void onTick(){
        //ダブルバッファリング
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) return;
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(image, px - 120, py - 120, null);
        holder.unlockCanvasAndPost(canvas);

        //移動
        if (px < 0 || getWidth() < px) vx = -vx;
        if (py < 0 || getHeight() < py) vy = -vy;
        px += vx;
        py += vy;
    }
}
