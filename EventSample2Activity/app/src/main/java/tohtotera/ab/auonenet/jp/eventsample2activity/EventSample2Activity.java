package tohtotera.ab.auonenet.jp.eventsample2activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class EventSample2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listview = (ListView)findViewById(R.id.listView);
        listview.setOnItemClickListener(new ListItemClickListener());
    }

    class ListItemClickListener implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view, int potion, long id){
            ListView listView = (ListView)parent;
            String item = (String)listView.getItemAtPosition(potion);
            Toast.makeText(EventSample2Activity.this, item, Toast.LENGTH_SHORT).show();
        }
    }
}
