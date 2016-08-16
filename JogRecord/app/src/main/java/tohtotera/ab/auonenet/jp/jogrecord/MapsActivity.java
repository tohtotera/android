package tohtotera.ab.auonenet.jp.jogrecord;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
        LoaderManager.LoaderCallbacks<Address> {

    private static final int ADDRESSLOADER_ID = 0;
    // INTERVAL:500 ,FASTESTINTERVAL:16 できれいな線が描けた
    private static final int INTERVAL = 500;
    private static final int FASTESTINTERVAL = 16;


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(INTERVAL)              // 位置情報の更新間隔をミリ秒で設定
            .setFastestInterval(FASTESTINTERVAL)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);   //位置情報取得要求の優先順位
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private List<LatLng>  mRunList = new ArrayList<LatLng>();
    private WifiManager mWifi;
    private boolean mWifiOff = false;
    private long mStartTimeMillis;
    private double mMeter = 0.0;           // メートル
    private double elapsedTime =0.0;        // ミリ秒
    private double mSpeed = 0.0;
    private DatabaseHelper mDbHelper;
    private boolean mStart = false;
    private boolean mFirst = false;
    private boolean mStop = false;
    private boolean mAsked =false;
    private Chronometer mChronometer;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // メンバー変数が初期化されることへの対処
        outState.putBoolean("ASKED",mAsked);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAsked = savedInstanceState.getBoolean("ASKED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // 画面をスリープにしない
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    Intent intent = new Intent(MapsActivity.this,JogView.class);
                    startActivity(intent);
                }
            });
        }
        mDbHelper = new DatabaseHelper(this);
        ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);
        tb.setChecked(false);       //OFFへ変更

        //ToggleのCheckが変更したタイミングで呼び出されるリスナー
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //トグルキーが変更された際に呼び出される
                if (isChecked) {
                    startChronometer();
                    mStart=true;
                    mFirst=true;
                    mStop=false;
                    mMeter=0.0;
                    mRunList.clear();
                } else {
                    stopChronometer();
                    mStop=true;
                    calcSpeed();
                    saveConfirmDialog();
                    mStart=false;
                }
            }
        });
    }
    private void startChronometer() {
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        // 電源ON時からの経過時間の値をベースに
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        mStartTimeMillis=System.currentTimeMillis();
    }
    private void stopChronometer() {
        mChronometer.stop();
        // ミリ秒
        elapsedTime=SystemClock.elapsedRealtime() - mChronometer.getBase();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!mAsked) {
            //Log.v("exec mAsked","" + mAsked);
            wifiConfirm();
            mAsked = !mAsked;
        }

        mGoogleApiClient.connect();
    }

    private void wifiConfirm(){
        mWifi = (WifiManager)getSystemService(WIFI_SERVICE);

        if(mWifi.isWifiEnabled()) {
            wifiConfirmDialog();
        }
    }
    private void wifiConfirmDialog() {
        DialogFragment newFragment = WifiConfirmDialogFragment.newInstance(
                R.string.wifi_confirm_dialog_title, R.string.wifi_confirm_dialog_message);

        newFragment.show(getFragmentManager(), "dialog");

    }

    public void wifiOff() {
        mWifi.setWifiEnabled(false);
        mWifiOff=true;
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, REQUEST, this);
    }
    @Override
    public void onLocationChanged(Location location){
        // stop後は動かさない
        if (mStop) {
            return;
        }
        CameraPosition cameraPos = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(19)
                .bearing(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));

        //マーカー設定
        mMap.clear();
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions();
        options.position(latlng);
        // ランチャーアイコン
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        options.icon(icon);
        mMap.addMarker(options);

        if (mStart) {
            if (mFirst) {
                Bundle args = new Bundle();
                args.putDouble("lat", location.getLatitude());
                args.putDouble("lon", location.getLongitude());

                getLoaderManager().restartLoader(ADDRESSLOADER_ID, args, this);
                mFirst = !mFirst;
            } else {
                // 移動線を描画
                drawTrace(latlng);
                // 走行距離を累積
                sumDistance();
            }
        }

    }
    private void drawTrace(LatLng latlng) {
        mRunList.add(latlng);
        if (mRunList.size() > 2) {
            PolylineOptions polyOptions = new PolylineOptions();
            for (LatLng polyLatLng : mRunList) {
                polyOptions.add(polyLatLng);
            }
            polyOptions.color(Color.BLUE);
            polyOptions.width(3);
            polyOptions.geodesic(false);
            mMap.addPolyline(polyOptions);
        }
    }

    private void sumDistance() {

        if (mRunList.size() < 2) {
            return;
        }
        mMeter=0;
        float[] results = new float[3];
        int i = 1;
        while (i<mRunList.size()){
            results[0]=0;
            Location.distanceBetween(mRunList.get(i-1).latitude, mRunList.get(i-1).longitude,
                    mRunList.get(i).latitude, mRunList.get(i).longitude, results);
            mMeter += results[0];
            i++;
        }
        // distanceBetweenの距離はメートル単位
        double disMeter = mMeter / 1000;
        TextView disText = (TextView) findViewById(R.id.disText);
        disText.setText(String.format("%.2f"+" km", disMeter));
    }
    private void calcSpeed() {
        sumDistance();
        mSpeed = (mMeter/1000) / (elapsedTime/1000) * 60 * 60;
    }
    private void saveConfirmDialog() {
        String message ="時間:";
        TextView disText = (TextView) findViewById(R.id.disText);

        message = message + mChronometer.getText().toString() + " " +
                "距離" + disText.getText() + "\n" +
                "時速" + String.format("%.2f"+" km", mSpeed);

        DialogFragment newFragment = SaveConfirmDialogFragment.newInstance(
                R.string.save_confirm_dialog_title, message);

        newFragment.show(getFragmentManager(), "dialog");

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected() ) {
            stopLocationUpdates();
        }
        mGoogleApiClient.disconnect();

    }
    @Override
    protected void onStop() {
        super.onStop();
        // 自プログラムがオフにした場合はWIFIをオンにする処理
        if (mWifiOff) {
            mWifi.setWifiEnabled(true);
        }
    }
    protected void stopLocationUpdates() {
        fusedLocationProviderApi.removeLocationUpdates(mGoogleApiClient, this);
    }
    @Override
    public void onConnectionSuspended(int cause) {
        // Do nothing
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do nothing
    }

    @Override
    public Loader<Address> onCreateLoader(int id, Bundle args) {
        double lat = args.getDouble("lat");
        double lon = args.getDouble("lon");
        return new AddressTaskLoader(this, lat,lon);
    }

    @Override
    public void onLoadFinished(Loader<Address> loader, Address result) {
        if (result != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < result.getMaxAddressLineIndex() + 1; i++) {
                String item = result.getAddressLine(i);
                if (item == null) {
                    break;
                }

                sb.append(item);
            }
            TextView address = (TextView) findViewById(R.id.address);

            address.setText(sb.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<Address> loader) {

    }
    public void saveJogViaCTP(){

        String strDate = new SimpleDateFormat("yyyy/MM/dd").format(mStartTimeMillis);

        TextView txtAddress = (TextView)findViewById(R.id.address);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DATE, strDate);
        values.put(DatabaseHelper.COLUMN_ELAPSEDTIME,mChronometer.getText().toString());
        values.put(DatabaseHelper.COLUMN_DISTANCE, mMeter);
        values.put(DatabaseHelper.COLUMN_SPEED, mSpeed);
        values.put(DatabaseHelper.COLUMN_ADDRESS, txtAddress.getText().toString());
        Uri uri = getContentResolver().insert(JogRecordContentProvider.CONTENT_URI, values);
        Toast.makeText(this, "データを保存しました", Toast.LENGTH_SHORT).show();
    }

    public void saveJog(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String strDate = new SimpleDateFormat("yyyy/MM/dd").format(mStartTimeMillis);

        TextView txtAddress = (TextView)findViewById(R.id.address);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DATE, strDate);
        values.put(DatabaseHelper.COLUMN_ELAPSEDTIME,mChronometer.getText().toString());
        values.put(DatabaseHelper.COLUMN_DISTANCE, mMeter);
        values.put(DatabaseHelper.COLUMN_SPEED, mSpeed);
        values.put(DatabaseHelper.COLUMN_ADDRESS, txtAddress.getText().toString());
        try {
            db.insert(DatabaseHelper.TABLE_JOGRECORD, null, values);
        } catch (Exception e) {
            Toast.makeText(this, "データの保存に失敗しました", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}