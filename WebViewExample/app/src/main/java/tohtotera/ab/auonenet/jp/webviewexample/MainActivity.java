package tohtotera.ab.auonenet.jp.webviewexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private Object[] activities = {
            "Yahoo!",				    Example01.class,
            "Google",				    Example01.class,
            "Load URL", 			    Example02.class,
            "Back and Forward", 	    Example03.class,
            "Gesture",				    Example04.class,
            "JS Combo", 			    Example05.class,
            "JS Handle",			    Example06.class,
            "Google Translation",	Example07.class,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CharSequence[] list = new CharSequence[activities.length / 2];
        for (int i = 0; i < list.length; i++){
            list[i] = (String)activities[i * 2];
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView)findViewById(R.id.ListView01);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, (Class<?>)activities[position * 2 + 1]);
                if (position == 0) intent.putExtra("url", "http://www.yahoo.co.jp");
                if (position == 1) intent.putExtra("url", "http://www.google.co.jp");
                if (position == 2) intent.putExtra("url", "http://www.msn.com");
                if (position == 3) intent.putExtra("url", "http://www.atmarkit.co.jp/fjava/rensai4/android09/js1.html");
                if (position == 3) intent.putExtra("url", "http://www.atmarkit.co.jp/fjava/rensai4/android09/js2.html");
                if (position == 3) intent.putExtra("url", "http://www.atmarkit.co.jp/fjava/rensai4/android09/js3.html");
                startActivity(intent);
            }
        });

    }
}
