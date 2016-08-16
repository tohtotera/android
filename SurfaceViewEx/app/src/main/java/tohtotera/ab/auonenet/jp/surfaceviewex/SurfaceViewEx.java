package tohtotera.ab.auonenet.jp.surfaceviewex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SurfaceViewEx extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view_ex);
        setContentView(new SurfaceViewView(this));
    }
}
