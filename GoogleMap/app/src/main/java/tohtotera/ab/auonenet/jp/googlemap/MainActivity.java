package tohtotera.ab.auonenet.jp.googlemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

    private Object[] activities = {
            "Hello Android Maps",	Hello.class,
            "Use Layout",			    UseLayout.class,
            "Zoom",					Zoom.class,
            "Settings",				Settings.class,
            "Controller",			    ControllerSample.class,
            "Overlay",				    OverlaySample.class,
            "Landmarks",			    Landmarks.class,
            "Tracking",				Tracking.class,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.Activity_Main);

        CharSequence[] list = new CharSequence[activities.length / 2];
        for (int i = 0; i < list.length; i++) {
            list[i] = (String) activities[i * 2];
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView)findViewById(R.id.ListView01);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, (Class<?>)activities[position * 2 + 1]);
                startActivity(intent);
            }
        });
    }
}
