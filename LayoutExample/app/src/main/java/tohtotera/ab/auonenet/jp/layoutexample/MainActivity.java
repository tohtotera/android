package tohtotera.ab.auonenet.jp.layoutexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] layouts = {
                "RelativeLayout",
                "LinearLayout",
                "TableLayout"
        };

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, layouts);
        ListView listView = (ListView)findViewById(R.id.ListView01);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Intent intent = new Intent(MainActivity.this, Class.forName(getClass().getPackage().getName() + "." + layouts[position] + "Activity"));
                    startActivity(intent);
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
