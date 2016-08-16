package tohtotera.ab.auonenet.jp.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;

/**
 * Created by Weider on 2015/12/03.
 */
public class AlphaAnimationExample extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alpha_animation_example);
    }

    public void onClick(View v){
        float fromAlpha = Float.parseFloat(((EditText)findViewById(R.id.fromAlpha)).getText().toString());
        float toAlpha = Float.parseFloat(((EditText)findViewById(R.id.toAlpha)).getText().toString());

        AlphaAnimation animation = new AlphaAnimation(fromAlpha, toAlpha);
        animation.setDuration(3000);
        v.startAnimation(animation);
    }
}
