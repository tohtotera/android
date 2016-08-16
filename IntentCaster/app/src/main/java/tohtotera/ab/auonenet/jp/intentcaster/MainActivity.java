package tohtotera.ab.auonenet.jp.intentcaster;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.actions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner)findViewById(R.id.Spinner01);
        spinner.setAdapter(adapter);
        Button button = (Button)findViewById(R.id.Button01);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    EditText editText = (EditText)findViewById(R.id.EditText01);
                    Spinner spinner = (Spinner)findViewById(R.id.Spinner01);
                    Intent intent = new Intent(spinner.getSelectedItem().toString(), Uri.parse(editText.getText().toString()));
                    startActivity(intent);
                }catch (Exception e){
                    TextView textView = new TextView(MainActivity.this);
                    textView.setText(e.getMessage());
                    Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setTitle(e.getClass().getName());
                    dialog.setContentView(textView);
                    dialog.show();
                }
            }
        });
    }
}
