package tohtotera.ab.auonenet.jp.touchex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Weider on 2015/11/24.
 */
public class TouchView extends View {
    //HashMap<key, Value>
    private HashMap<String, PointF> points = new HashMap<String, PointF>();
    //コンストラクタ
    public TouchView(Context context){
        super(context);
        setBackgroundColor(Color.WHITE);
    }

    //描画時に呼ばれる
    @Override
    protected void onDraw(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(48);

        //タッチした画面のXY座標
        canvas.drawText("タップした座標は", 0, 60 * 1, paint);
        Object[] keys = points.keySet().toArray();//keySet()はMapに格納されているキーの一覧を返す

        for (int i = 0; i < keys.length; i++){
            PointF pos = points.get(keys[i]);
            canvas.drawText((int)pos.x + " ,  " + (int)pos.y , 0, 120+60*i, paint);
        }
    }

    //タップ時に呼ばれる
    @Override
    public boolean onTouchEvent(MotionEvent event){
        //アクションの種別とタップ数の取得
        int action = event.getAction();
        int count  = event.getPointerCount();
        //アクションインデックスの取得
        int index = event.getActionIndex();
        //アクションインデックスを使ってポインタIDを取得
        //ポインタIDは指ごとに割り当てられている識別番号
        int pointerID = event.getPointerId(index);
        //タッチ位置の取得
        switch (action & MotionEvent.ACTION_MASK){//アクション種別とアクション定数の比較の仕方
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                points.put(""+pointerID, new PointF(event.getX(), event.getY()));
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < count; i++){
                    PointF pos = points.get(""+event.getPointerId(i));//pointsはHashMapオブジェクト
                    pos.x = event.getX();
                    pos.y = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                points.remove(""+pointerID);//pointsはHashMapオブジェクト
                break;
        }
        invalidate();
        return true;
    }
}
