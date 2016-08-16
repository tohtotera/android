package tohtotera.ab.auonenet.jp.dualactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.String;

public class MainActivity extends AppCompatActivity {
    //これから作成するIntentは次の画面の遷移先であるeditor.class
    //で、明示的Intentと呼ばれる。作り方は次のように記述する。
    //Intent intent = new Intent(呼び出し側のActivityクラスのインスタンス、遷移先Activityクラス);
    private static final int SHOW_EDITOR = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.Button01);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Editor.class);
                TextView textView = (TextView)findViewById(R.id.TextView01);
                CharSequence text  = textView.getText();
                //作成したIntentに付加情報として上で取得したTextViewオブジェクト
                //をputExtraメソッドに指定する。書式は次のとおり
                //intent.putExtra("付加するオブジェクトの仮の名前"、付加するContextのid + 初期値);
                intent.putExtra("TEXT", text + "テキスト入力");
                //はじめに表示するActivityを他のActivityからの情報を得るモードのメソッド
                //startActivityResult(Intent, 識別番号):と記述する。識別番号は他のActivityに渡され
                //結果が返ってきたときの照合に使用される。
                startActivityForResult(intent, SHOW_EDITOR);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //返ってきた情報が送ったときのオブジェクトであることを確認する
        if (requestCode == SHOW_EDITOR){
            //返ってきた情報が誤りでない場合の確認
            if (resultCode == RESULT_OK){
                TextView textView = (TextView)findViewById(R.id.TextView01);
                textView.setText(data.getCharSequenceExtra("TEXT"));
            }
        }
    }
}
