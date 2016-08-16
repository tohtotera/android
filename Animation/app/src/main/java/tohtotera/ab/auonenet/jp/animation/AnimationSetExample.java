package tohtotera.ab.auonenet.jp.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Weider on 2015/12/03.
 */
public class AnimationSetExample extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_set_example);
    }

    public void onClick(View v){
        AnimationSet set = new AnimationSet(true);

        AlphaAnimation alpha = new AlphaAnimation(0.9f, 0.2f);
        RotateAnimation rotate = new RotateAnimation(0, 360, 50, 25);
        ScaleAnimation scale = new ScaleAnimation(0.1f, 1, 0.1f, 1);
        TranslateAnimation translate = new TranslateAnimation(50, 0, 200, 0);

        set.addAnimation(alpha);
        set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(translate);
        set.setDuration(3000);
        v.startAnimation(set);
    }
}
