package tohtotera.ab.auonenet.jp.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.os.Bundle;
/**
 * Created by Weider on 2015/11/26.
 */
public class Main2Activity implements View.OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Buttonの生成
        Button myButton = new Button(this);
        // Buttonのラベルの設定
        myButton.setText("Main1Activityへ");
        // ButtonにonClickイベントリスナーを追加
        myButton.setOnClickListener(this);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        setContentView(myButton, params);
    }

    public void onClick(View v) {

        // インテントのインスタンス生成
        Intent intent = new Intent(this, MainActivity.class);
        // 次画面のアクティビティ起動
        startActivity(intent);
    }

}
