package tohtotera.ab.auonenet.jp.imageview;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.*;
import android.widget.ImageView;

public class ImageEx extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_ex);
        setContentView(new ImageView(this));
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
