package com.example.kanehiro_acer.maprefuge;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Xml;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    // 避難所リスト
    private List<Refuge> mRefugeList = new ArrayList<Refuge>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        UiSettings settings = mMap.getUiSettings();

        settings.setCompassEnabled(true);
        // ズームイン・アウトボタンの有効化
        settings.setZoomControlsEnabled(true);
        // 回転ジェスチャーの有効化
        settings.setRotateGesturesEnabled(true);
        // スクロールジェスチャーの有効化
        settings.setScrollGesturesEnabled(true);
        // Tlitジェスチャー(立体表示)の有効化
        settings.setTiltGesturesEnabled(true);
        // ズームジェスチャー(ピンチイン・アウト)の有効化
        settings.setZoomGesturesEnabled(true);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mRefugeList.clear();
        parseXML();
        addMaker();

        Refuge refuge = mRefugeList.get(0);
        if (refuge != null) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(refuge.getLat(), refuge.getLng()), 15);
            mMap.moveCamera(cu);
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                calcDistance(latLng);
                sortRefugeList();
                updateMaker();
                addLine(latLng);
            }
        });
    }
    private void addLine(LatLng point){

        CircleOptions circleOptions = new CircleOptions()
                .center(point)
                        //.fillColor(Color.LTGRAY)
                .radius(3);
        mMap.addCircle(circleOptions);
        for (Refuge refuge : mRefugeList) {
            if (refuge != null) {
                if (refuge.isNear()) {
                    PolylineOptions polyOptions = new PolylineOptions();
                    polyOptions.add(point);
                    polyOptions.add(new LatLng(refuge.getLat(), refuge.getLng()));
                    polyOptions.color(Color.GRAY);
                    polyOptions.width(3);
                    polyOptions.geodesic(true); //true:大圏コース,false:直線
                    mMap.addPolyline(polyOptions);
                }
            }
        }
    }

    private void updateMaker() {
        int i = 0;
        mMap.clear();
        for (Refuge refuge : mRefugeList) {
            if (refuge != null) {
                MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(refuge.getLat(), refuge.getLng()));
                options.title(refuge.getName() + " " + refuge.getDistance() + "m");
                options.snippet(refuge.getAddress());
                BitmapDescriptor icon;
                if (i > 2) {
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
                    refuge.setNear(false);
                } else {
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                    refuge.setNear(true);
                }
                options.icon(icon);
                Marker marker = mMap.addMarker(options);
                if (i == 0) {
                    marker.showInfoWindow();
                }
                i++;
            }
        }
    }
    private void sortRefugeList(){
        Collections.sort(mRefugeList, new Comparator<Refuge>() {
            @Override
            public int compare(Refuge lhs, Refuge rhs) {
                return lhs.getDistance() - rhs.getDistance();
            }
        });
    }
    private void calcDistance(LatLng point){
        // タッチした場所と避難所の距離を求める
        double startLat = point.latitude;
        double startLng = point.longitude;
        // 結果を格納するための配列
        float[] results = new float[3];
        for (Refuge refuge : mRefugeList) {
            if (refuge != null) {
                Location.distanceBetween(startLat, startLng, refuge.getLat(), refuge.getLng(), results);
                refuge.setDistance(results[0]);
            }
        }


    }
    private void parseXML() {
        // AssetManagerの呼び出し
        AssetManager assetManager = getResources().getAssets();
        try {

            // XMLファイルのストリーム情報を取得
            InputStream is = assetManager.open("refuge_nonoichi.xml");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStreamReader);
            String title="";
            String address="";
            String lat = "";
            String lon = "";

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if ("marker".equals(tag)) {
                            title = parser.getAttributeValue(null,"title");
                            address = parser.getAttributeValue(null,"adress");
                            lat = parser.getAttributeValue(null,"lat");
                            lon = parser.getAttributeValue(null,"lng");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if ("marker".equals(endTag)) {
                            newRefuge(title, address,Double.valueOf(lat), Double.valueOf(lon));
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

    }
    private void newRefuge(String title,String address,double lat,double lon) {
        Refuge refuge;
        refuge = new Refuge(title,address,lat,lon);
        mRefugeList.add(refuge);

    }
    private void addMaker() {
        for (Refuge refuge : mRefugeList) {
            if (refuge != null) {
                MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(refuge.getLat(),refuge.getLng()));
                options.title(refuge.getName());
                options.snippet(refuge.getAddress());
                mMap.addMarker(options);
            }
        }
    }
}
