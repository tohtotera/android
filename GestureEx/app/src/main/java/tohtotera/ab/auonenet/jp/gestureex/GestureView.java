package tohtotera.ab.auonenet.jp.gestureex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Weider on 2015/11/24.
 */
public class GestureView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private ArrayList<String> info;
    private GestureDetector gestureDetector;

    //コンストラクタ
    public GestureView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
        info = new ArrayList<String>();
        info.add("<<GestureEx>>");
        //ジェスチャーディテクターの生成
        gestureDetector = new GestureDetector(context, this);
    }

    //描画時に呼ばれる
    @Override
    protected void onDraw(Canvas canvas) {
        //描画オブジェクトの生成
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(48);
        //情報の描画
        for (int i = 0; i < info.size(); i++) {
            canvas.drawText((String) info.get(i), 0, 60 + 60 * i, paint);
        }
    }

    //情報の追加
    private void addInfo(String str) {
        info.add(1, str);
        while (info.size() > 30) {
            info.remove(info.size() - 1);
        }
    }
    //タッチ時に呼ばれる
    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        return true;
    }
    //ダウン時に呼ばれる
    public boolean onDown(MotionEvent e){
        addInfo("Down");
        return false;
    }
    //プレス後ムーブなしの時に呼ばれる
    public void onShowPress(MotionEvent e){
        addInfo("ShowPress");
    }
    public boolean onSingleTapUp(MotionEvent e){
        addInfo("Up");
        return false;
    }
    //長押し時に呼ばれる
    public void onLongPress(MotionEvent e){
        addInfo("LongPress");
    }
    //フリック時に呼ばれる
    public boolean onFling(MotionEvent e0, MotionEvent e1, float velocityX, float velocityY){
        addInfo("Fling(" + (int)velocityX + "," + (int)velocityY + ")");
        return false;
    }
    //スクロール時に呼ばれる
    public boolean onScroll(MotionEvent e0, MotionEvent e1, float distanceX, float distanceY){
        addInfo("Scroll(" + (int)distanceX + "," + (int)distanceY + ")");
        return false;
    }
    //シングルタップ時に呼ばれる
    public boolean onSingleTapConfirmed(MotionEvent e){
        addInfo("SingleTap");
        return false;
    }
    //ダブルタップ時に呼ばれる
    public boolean onDoubleTap(MotionEvent e){
        addInfo("DoubleTap");
        return false;
    }
    //ダブルタップイベント発生時に呼ばれる
    public boolean onDoubleTapEvent(MotionEvent e){
        addInfo("DoubleTapEvent");
        return false;
    }
}
