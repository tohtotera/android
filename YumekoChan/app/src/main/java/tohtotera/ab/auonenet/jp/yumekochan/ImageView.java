package tohtotera.ab.auonenet.jp.yumekochan;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.view.View;

/**
 * Created by Weider on 2015/11/23.
 */
public class ImageView extends View {
    private Bitmap image;

    //コンストラクタ
    public ImageView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
        //画像の読み込み
        Resources r = context.getResources();
        image = BitmapFactory.decodeResource(r, R.drawable.yumekochan);
    }


    //描画時に呼ばれる
    @Override
    protected void onDraw(Canvas canvas) {
        //イメージの描画
        canvas.drawBitmap(image, 0, 0, null);
        //イメージの拡大縮小描画
        int w = image.getWidth();
        int h = image.getHeight();
        Rect src = new Rect(0, 0, w, h);
        Rect dst = new Rect(0, 300, w * 2, 300 + h * 2);
        canvas.drawBitmap(image, src, dst, null);
    }
}
