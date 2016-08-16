package tohtotera.ab.auonenet.jp.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Weider on 2015/12/04.
 */
public class Test1Activity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView textView = new TextView(this);
        textView.setText(R.string.hello);
        setContentView(textView);
    }
}
