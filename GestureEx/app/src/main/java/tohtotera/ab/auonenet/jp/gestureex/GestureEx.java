package tohtotera.ab.auonenet.jp.gestureex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GestureEx extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_ex);
        setContentView(new GestureView(this));
    }
}
