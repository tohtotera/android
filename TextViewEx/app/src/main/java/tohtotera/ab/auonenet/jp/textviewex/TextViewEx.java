package tohtotera.ab.auonenet.jp.textviewex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextViewEx extends AppCompatActivity {
    private final static int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view_ex);

        //レイアウトの生成
        LinearLayout layout  = new LinearLayout(this);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        //テキストビューの生成
        TextView textView = new TextView(this);
        textView.setText("これはテストです。");
        textView.setTextSize(16.0f);
        textView.setTextColor(Color.rgb(0, 0, 0));

        //コンポーネントのサイズの指定
        textView.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));

        //テキストビューコンポーネントをレイアウトへ追加
        layout.addView(textView);

        //画像の読み込み
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample);

        //イメージビューの生成
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
        layout.addView(imageView);

    }
}
