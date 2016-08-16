package tohtotera.ab.auonenet.jp.eventsample1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Weider on 2015/12/05.
 */
public class EventSampleActivity extends Activity{
    TextView view;
    Button   button;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        view = (TextView)findViewById(R.id.dateTimeDisplay);
        button = (Button)findViewById(R.id.whatTime);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy.MM.dd '/' hh:mm:ss a zzz");
                Date time = new Date();
                view.setText(sdf.format(time));
                view.setText(time.toString());
            }
        });
    }
}
