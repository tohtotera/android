package tohtotera.ab.auonenet.jp.animation;



/**
 * Created by Weider on 2015/12/03.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class XmlExample extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_animation_example);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("ABSOLUTE");
        adapter.add("RELATIVE_TO_SELF");
        adapter.add("RELATIVE_TO_PARENT");
        ((Spinner)findViewById(R.id.fromXType)).setAdapter(adapter);
        ((Spinner)findViewById(R.id.toXType)).setAdapter(adapter);
        ((Spinner)findViewById(R.id.fromYType)).setAdapter(adapter);
        ((Spinner)findViewById(R.id.toYType)).setAdapter(adapter);
    }

    public void onClick(View v) {
        Object xFromType = ((Spinner)findViewById(R.id.fromXType)).getSelectedItem();
        Object xToType = ((Spinner)findViewById(R.id.toXType)).getSelectedItem();
        Object yFromType = ((Spinner)findViewById(R.id.fromYType)).getSelectedItem();
        Object yToType = ((Spinner)findViewById(R.id.toYType)).getSelectedItem();
        float fromXValue = Float.parseFloat(((EditText)findViewById(R.id.fromXValue)).getText().toString());
        int fromXType = xFromType.equals("ABSOLUTE") ? Animation.ABSOLUTE : xFromType.equals("RELATIVE_TO_SELF") ? Animation.RELATIVE_TO_SELF : Animation.RELATIVE_TO_PARENT;
        float toXValue = Float.parseFloat(((EditText)findViewById(R.id.toXValue)).getText().toString());
        int toXType = xToType.equals("ABSOLUTE") ? Animation.ABSOLUTE : xToType.equals("RELATIVE_TO_SELF") ? Animation.RELATIVE_TO_SELF : Animation.RELATIVE_TO_PARENT;
        float fromYValue = Float.parseFloat(((EditText)findViewById(R.id.fromYValue)).getText().toString());
        int fromYType = yFromType.equals("ABSOLUTE") ? Animation.ABSOLUTE : yFromType.equals("RELATIVE_TO_SELF") ? Animation.RELATIVE_TO_SELF : Animation.RELATIVE_TO_PARENT;
        float toYValue = Float.parseFloat(((EditText)findViewById(R.id.toYValue)).getText().toString());
        int toYType = yToType.equals("ABSOLUTE") ? Animation.ABSOLUTE : yToType.equals("RELATIVE_TO_SELF") ? Animation.RELATIVE_TO_SELF : Animation.RELATIVE_TO_PARENT;

        TranslateAnimation animation = new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue);
        animation.setDuration(3000);
        v.startAnimation(animation);
    }
}
