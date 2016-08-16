package com.example.kanehiro_acer.parsexml;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv01 = (TextView)findViewById(R.id.txt01);
        //tv01.setText(parseNonoichi());
        tv01.setText(parseSabae());
    }
    public String parseSabae() {
        StringBuilder strBuild = new StringBuilder();
        String type ="";
        String name ="";
        String lat ="";
        String lng ="";

        // AssetManagerの呼び出し
        AssetManager assetManager = getResources().getAssets();
        try {

            // XMLファイルのストリーム情報を取得
            InputStream is = assetManager.open("refuge_sabae.xml");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStreamReader);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if ("type".equals(tag)) {
                            type = parser.nextText();
                        } else if ("name".equals(tag)) {
                            name = parser.nextText();
                        } else if ("latitude".equals(tag)) {
                            lat = parser.nextText();
                        } else if ("longitude".equals(tag)) {
                            lng = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if ("refuge".equals(endTag)) {
                            strBuild.append(name);
                            strBuild.append(" ");
                            strBuild.append(type);
                            strBuild.append(" ");
                            strBuild.append(lat);
                            strBuild.append(" ");
                            strBuild.append(lng);
                            strBuild.append("\n");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strBuild.toString();

    }

    public String parseNonoichi() {
        StringBuilder strBuild = new StringBuilder();

        // AssetManagerの呼び出し
        AssetManager assetManager = getResources().getAssets();
        try {

            // XMLファイルのストリーム情報を取得
            InputStream is = assetManager.open("refuge_nonoichi.xml");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStreamReader);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if ("marker".equals(tag)) {
                            strBuild.append(parser.getAttributeValue(null,"title"));
                            strBuild.append(",");
                            strBuild.append(parser.getAttributeValue(null,"lat"));
                            strBuild.append(",");
                            strBuild.append(parser.getAttributeValue(null,"lng"));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if ("marker".equals(endTag)) {
                            strBuild.append("\n");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strBuild.toString();

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
