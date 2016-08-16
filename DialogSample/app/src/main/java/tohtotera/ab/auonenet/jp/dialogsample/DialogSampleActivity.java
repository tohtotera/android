package tohtotera.ab.auonenet.jp.dialogsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogSampleActivity extends AppCompatActivity {

    TextView label = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        label = (TextView)findViewById(R.id.label_dialogtext);

        //通常ダイアログボタンのリスナの設定
        Button dialogBtn = (Button)findViewById(R.id.normalDialog);
        dialogBtn.setTag("normal");
        dialogBtn.setOnClickListener(new ButtonClickListener());

        //単一選択ダイアログボタンのリスなの設定
        Button singleSelectDialogBtn = (Button)findViewById(R.id.singleSelectDialogButton);
        singleSelectDialogBtn.setTag("select");
        singleSelectDialogBtn.setOnClickListener(new ButtonClickListener());
    }

    //クリックリスナの定義
    class ButtonClickListener implements View.OnClickListener{
        public void onClick(View v){
            String tag = (String)v.getTag();
            if (tag.equals("normal")){
                showDialog();
            }else if (tag.equals("select")){
                showSingleSelectDialog();
            }
        }
    }

    //通常ダイアログの表示
    private void showDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(DialogSampleActivity.this);
        dialog.setTitle("通常ダイアログ");
        dialog.setMessage("いずれかを選択してください");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                label.setText("OKが選択されました");
            }
        });
        dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                label.setText("キャンセルが選択されました");
            }
        });
        dialog.show();
    }

    //選択ダイアログの表示
    final String[] items = new String[]{"ビール","日本酒","ワイン"};
    int which = 0;

    public void showSingleSelectDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(DialogSampleActivity.this);
        dialog.setTitle("選択ダイアログ");
        dialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                which = whichButton;
            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                String selected = items[which];
                label.setText("選択ダイアログ：" + selected + "が選択されました。");
            }
        });
        dialog.show();
    }
}
