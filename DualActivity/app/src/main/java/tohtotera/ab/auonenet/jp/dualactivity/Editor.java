package tohtotera.ab.auonenet.jp.dualactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Weider on 2015/11/26.
 */
public class Editor extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);

        Button button = (Button)findViewById(R.id.Button01);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            EditText edittext = (EditText)findViewById(R.id.EditText01);
            edittext.setText(extras.getCharSequence("TEXT"));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EditText editText = (EditText)findViewById(R.id.EditText01);
                CharSequence text = editText.getText();
                intent.putExtra("TEXT", text);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
