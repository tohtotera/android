package tohtotera.ab.auonenet.jp.animation;



/**
 * Created by Weider on 2015/12/03.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ScaleAnimationExample extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scale_animation_example);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("ABSOLUTE");
        adapter.add("RELATIVE_TO_SELF");
        adapter.add("RELATIVE_TO_PARENT");

        ((Spinner)findViewById(R.id.pivotXType)).setAdapter(adapter);
        ((Spinner)findViewById(R.id.pivotYType)).setAdapter(adapter);
    }

    public void onClick(View v) {
        Object xType = ((Spinner)findViewById(R.id.pivotXType)).getSelectedItem();
        Object yType = ((Spinner)findViewById(R.id.pivotYType)).getSelectedItem();
        float fromX = Float.parseFloat(((EditText)findViewById(R.id.fromX)).getText().toString());
        float toX = Float.parseFloat(((EditText)findViewById(R.id.toX)).getText().toString());
        float fromY = Float.parseFloat(((EditText)findViewById(R.id.fromY)).getText().toString());
        float toY = Float.parseFloat(((EditText)findViewById(R.id.toY)).getText().toString());

        int pivotXType = xType.equals("ABSOLUTE") ? Animation.ABSOLUTE : xType.equals("RELATIVE_TO_SELF") ? Animation.RELATIVE_TO_SELF : Animation.RELATIVE_TO_PARENT;
        float pivotXValue = Float.parseFloat(((EditText)findViewById(R.id.pivotXValue)).getText().toString());
        int pivotYType = yType.equals("ABSOLUTE") ? Animation.ABSOLUTE : yType.equals("RELATIVE_TO_SELF") ? Animation.RELATIVE_TO_SELF : Animation.RELATIVE_TO_PARENT;
        float pivotYValue = Float.parseFloat(((EditText)findViewById(R.id.pivotYValue)).getText().toString());

        ScaleAnimation animation = new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);
        animation.setDuration(3000);
        v.startAnimation(animation);
    }
}
