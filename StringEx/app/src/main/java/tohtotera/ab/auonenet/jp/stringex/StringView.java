package tohtotera.ab.auonenet.jp.stringex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Weider on 2015/11/23.
 */
public class StringView extends View {
    //コンストラクタ
    public StringView(Context context){
        super(context);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //描画オブジェクトの生成
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //文字サイズと文字色の指定
        paint.setTextSize(48);
        paint.setColor(Color.rgb(0, 0, 0));
        //画面サイズの取得
        canvas.drawText("画面サイズ：" + getWidth() + "x" + getHeight(), 0, 60, paint);
        //文字幅の取得
        canvas.drawText("文字幅：" + (int)paint.measureText("A"), 0, 60*2, paint);
        //アセント・ディセントの取得
        canvas.drawText("アセント：" + (int)paint.ascent(), 0 ,60*3, paint);
        canvas.drawText("ディセント：" + (int)paint.ascent(), 0, 60*4, paint);
        //24ドットの文字列の表示
        paint.setTextSize(24);
        paint.setColor(Color.rgb(255, 0, 0));
        canvas.drawText("２４ドット", 0, 60*5, paint);
        //32ドットの文字列の表示
        paint.setTextSize(32);
        paint.setColor(Color.rgb(0, 255, 0));
        canvas.drawText("３２ドット", 0, 60*6, paint);
        //48ドットの文字列の表示
        paint.setTextSize(48);
        paint.setColor(Color.rgb(0, 0, 255));
        canvas.drawText("４８ドット", 0, 60*7, paint);
    }
}
