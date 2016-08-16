package tohtotera.ab.auonenet.jp.servicesample1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class ServiceSample1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new StartButtonClickListener());
    }

    class StartButtonClickListener implements View.OnClickListener{
        public void onClick(View v){
            Intent intent = new Intent(ServiceSample1Activity.this, ServiceSample1Service.class);
            EditText stopcount = (EditText)findViewById(R.id.stopcount);
            intent.putExtra("STOPTIME", stopcount.getText().toString());
            //サービスの開始
            startService(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_sample1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
