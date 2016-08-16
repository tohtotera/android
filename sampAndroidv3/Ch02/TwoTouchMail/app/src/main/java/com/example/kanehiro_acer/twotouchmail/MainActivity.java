package com.example.kanehiro_acer.twotouchmail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnPickUp = (Button) this.findViewById(R.id.button1);
        btnPickUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent =
                        new Intent(MainActivity.this, PickUpActivity.class);
                startActivity(intent);
            }
        });
        Button btnNoDinner = (Button) this.findViewById(R.id.button2);
        btnNoDinner.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent =
                        new Intent(MainActivity.this, NoDinnerActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
