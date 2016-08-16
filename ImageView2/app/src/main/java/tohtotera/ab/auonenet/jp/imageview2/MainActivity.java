package tohtotera.ab.auonenet.jp.imageview2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.*;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        setContentView(new ImageView(this));
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
