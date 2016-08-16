package tohtotera.ab.auonenet.jp.animation;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Weider on 2015/12/03.
 */
public class OptionExample extends Activity {
    private AnimationSet set = new AnimationSet(true);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_example);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("AccelerateDecelerateInterpolator");
        adapter.add("AccelerateInterpolator");
        adapter.add("AnticipateInterpolator");
        adapter.add("AnticipateOvershootInterpolator");
        adapter.add("BounceInterpolator");
        adapter.add("CycleInterpolator");
        adapter.add("DecelerateInterpolator");
        adapter.add("LinearInterpolator");
        adapter.add("OvershootInterpolator");
        ((Spinner)findViewById(R.id.interpolator)).setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("RESTART");
        adapter.add("REVERSE");
        ((Spinner)findViewById(R.id.repeatMode)).setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("ZORDER_NORMAL");
        adapter.add("ZORDER_TOP");
        adapter.add("ZORDER_BOTTOM");
        ((Spinner)findViewById(R.id.zAdjustment)).setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("fillBefore");
        adapter.add("fillAfter");
        adapter.add("fillDisable");
        ((Spinner)findViewById(R.id.fill)).setAdapter(adapter);

        AlphaAnimation alpha = new AlphaAnimation(0.9f, 0.2f);
        RotateAnimation rotate = new RotateAnimation(0, 360, 50, 25);
        ScaleAnimation scale = new ScaleAnimation(0.1f, 1, 0.1f, 1);
        TranslateAnimation translate = new TranslateAnimation(50, 0, 200, 0);
        set.addAnimation(alpha);
        set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(translate);

        set.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Toast.makeText(OptionExample.this, "Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Toast.makeText(OptionExample.this, "Repeat", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(OptionExample.this, "End", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View v) {
        int dulaton = Integer.parseInt(((EditText)findViewById(R.id.duration)).getText().toString());
        Object selected = ((Spinner)findViewById(R.id.interpolator)).getSelectedItem();
        float input = Float.parseFloat((((EditText)findViewById(R.id.input)).getText().toString()));
        Interpolator interpolator = null;

        if (selected.equals("AccelerateDecelerateInterpolator")) {
            interpolator = new AccelerateDecelerateInterpolator();
        } else if (selected.equals("AccelerateInterpolator")) {
            interpolator = new AccelerateInterpolator(input);
        } else if (selected.equals("AnticipateInterpolator")) {
            interpolator = new AnticipateInterpolator(input);
        } else if (selected.equals("AnticipateOvershootInterpolator")) {
            interpolator = new AnticipateOvershootInterpolator(input);
        } else if (selected.equals("BounceInterpolator")) {
            interpolator = new BounceInterpolator();
        } else if (selected.equals("CycleInterpolator")) {
            interpolator = new CycleInterpolator(input);
        } else if (selected.equals("DecelerateInterpolator")) {
            interpolator = new DecelerateInterpolator(input);
        } else if (selected.equals("LinearInterpolator")) {
            interpolator = new LinearInterpolator();
        } else if (selected.equals("OvershootInterpolator")) {
            interpolator = new OvershootInterpolator(input);
        }

        int repeatCount = Integer.parseInt(((EditText)findViewById(R.id.repeatCount)).getText().toString());

        selected = ((Spinner)findViewById(R.id.repeatMode)).getSelectedItem();
        int repeatMode = Animation.RESTART;
        if (selected.equals("RESTART")) {
            repeatMode = Animation.RESTART;
        } else if (selected.equals("REVERSE")) {
            repeatMode = Animation.REVERSE;
        }

        selected = ((Spinner)findViewById(R.id.zAdjustment)).getSelectedItem();
        int zAdjustment = Animation.ZORDER_NORMAL;
        if (selected.equals("ZORDER_NORMAL")) {
            repeatMode = Animation.ZORDER_NORMAL;
        } else if (selected.equals("ZORDER_TOP")) {
            repeatMode = Animation.ZORDER_TOP;
        } else if (selected.equals("ZORDER_BOTTOM")) {
            repeatMode = Animation.ZORDER_BOTTOM;
        }

        Object fill = ((Spinner)findViewById(R.id.fill)).getSelectedItem();
        if (selected.equals("fillBefore")) {
            set.setFillEnabled(true);
            set.setFillBefore(true);
        } else if (selected.equals("fillAfter")) {
            set.setFillEnabled(true);
            set.setFillAfter(true);
        } else if (selected.equals("fillDisable")) {
            set.setFillEnabled(false);
        }

        long startOffset = Long.parseLong(((EditText)findViewById(R.id.startOffset)).getText().toString());

        set.setDuration(dulaton);
        set.setInterpolator(interpolator);

        for (Animation a : set.getAnimations()) {
            a.setRepeatCount(repeatCount);
            a.setRepeatMode(repeatMode);
            a.setZAdjustment(zAdjustment);

            if (fill.equals("fillBefore")) {
                a.setFillEnabled(true);
                a.setFillBefore(true);
                a.setFillAfter(false);
            } else if (fill.equals("fillAfter")) {
                a.setFillEnabled(true);
                a.setFillBefore(false);
                a.setFillAfter(true);
            } else if (fill.equals("fillDisable")) {
                a.setFillEnabled(false);
                a.setFillBefore(false);
                a.setFillAfter(false);
            }
        }
        set.setRepeatCount(repeatCount);
        set.setRepeatMode(repeatMode);
        set.setZAdjustment(zAdjustment);
        if (fill.equals("fillBefore")) {
            set.setFillEnabled(true);
            set.setFillBefore(true);
            set.setFillAfter(false);
        } else if (fill.equals("fillAfter")) {
            set.setFillEnabled(true);
            set.setFillBefore(false);
            set.setFillAfter(true);
        } else if (fill.equals("fillDisable")) {
            set.setFillEnabled(false);
            set.setFillBefore(false);
            set.setFillAfter(false);
        }
        set.setStartOffset(startOffset);

        v.startAnimation(set);
    }

    public void onCancel(View v) {
        for (Animation a : set.getAnimations()) {
            a.cancel();
        }
        set.cancel();
    }
}
