package com.example.kanehiro_acer.twotouchmail;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class NoDinnerActivity extends ActionBarActivity
        implements View.OnClickListener,View.OnLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_dinner);
        Button btnSend = (Button) this.findViewById(R.id.button);
        btnSend.setOnClickListener(this);
        btnSend.setOnLongClickListener(this);
    }
    @Override
    public void onClick(View v) {
        EditText edit01 = (EditText)findViewById(R.id.editText);
        String title = edit01.getText().toString();
        Resources res = getResources();
        Uri uri = Uri.parse("mailto:" + res.getString(R.string.mail_to).toString());
        Intent intent=new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, "遅くなるのでめしいらない");
        startActivity(intent);
    }
    @Override
    public boolean onLongClick(View v) {
        EditText edit01 = (EditText)findViewById(R.id.editText);
        String title = edit01.getText().toString();
        Resources res = getResources();
        Uri uri = Uri.parse("mailto:" + res.getString(R.string.mail_to).toString());
        Intent intent=new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, "遅くなるので食事いりません。" +
                " 連絡が遅くなってごめんなさい。" +
                "いつもありがとう");
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_no_dinner, menu);
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

