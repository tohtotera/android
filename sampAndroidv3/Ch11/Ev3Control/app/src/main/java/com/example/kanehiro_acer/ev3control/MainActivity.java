package com.example.kanehiro_acer.ev3control;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private static final int REQUEST_CONNECT_DEVICE = 1000; // 識別用
    private static final int REQUEST_ENABLE_BT = 2000;
    public static final int MENU_TOGGLE_CONNECT = Menu.FIRST;
    public static final int MENU_QUIT = Menu.FIRST + 1;
    public static final int COLOR_SENSOR_PORT = 3 - 1;
    public static final int TOUCH_SENSOR_PORT = 4 - 1;
    public static final int USONIC_SENSOR_PORT = 2 - 1;



    boolean newDevice;
    private ProgressDialog connectingProgressDialog;
    private boolean connected = false;
    private boolean bt_error_pending = false;
    private Handler btcHandler;
    private BTCommunicator myBTCommunicator = null;

    private Toast mLongToast;
    private Toast mShortToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mLongToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        mShortToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        Button motorACon = (Button)findViewById(R.id.motorACon);
        motorACon.setOnClickListener(this);
        Button motorACback = (Button)findViewById(R.id.motorACback);
        motorACback.setOnClickListener(this);
        Button motorACoff = (Button)findViewById(R.id.motorACoff);
        motorACoff.setOnClickListener(this);

        Button motorAon = (Button)findViewById(R.id.motorAon);
        motorAon.setOnClickListener(this);
        Button motorAoff = (Button)findViewById(R.id.motorAoff);
        motorAoff.setOnClickListener(this);

        Button motorCon = (Button)findViewById(R.id.motorCon);
        motorCon.setOnClickListener(this);
        Button motorCoff = (Button)findViewById(R.id.motorCoff);
        motorCoff.setOnClickListener(this);

        Button readColorSensor = (Button)findViewById(R.id.readColorSensor);
        readColorSensor.setOnClickListener(this);
        Button readTouchSensor = (Button)findViewById(R.id.readTouchSensor);
        readTouchSensor.setOnClickListener(this);
        Button readUSonicSensor = (Button)findViewById(R.id.readUltrasonicSensor);
        readUSonicSensor.setOnClickListener(this);

        Button rotateByDegree = (Button)findViewById(R.id.rotatebydegree);
        rotateByDegree.setOnClickListener(this);
        Button rotateByTime = (Button)findViewById(R.id.rotatebytime);
        rotateByTime.setOnClickListener(this);

        NumberPicker speedPicker = (NumberPicker)findViewById(R.id.speedPicker);
        speedPicker.setMaxValue(100);
        speedPicker.setMinValue(0);
        speedPicker.setValue(50);
        NumberPicker degreePicker = (NumberPicker)findViewById(R.id.degreePicker);
        degreePicker.setMaxValue(10);
        degreePicker.setMinValue(0);
        degreePicker.setValue(3);
        NumberPicker timePicker = (NumberPicker)findViewById(R.id.timePicker);
        timePicker.setMaxValue(10);
        timePicker.setMinValue(0);
        timePicker.setValue(3);
    }
    @Override
    protected void onStart() {
        super.onStart();

        if(!BluetoothAdapter.getDefaultAdapter().equals(null)){
            //Bluetooth対応端末の場合の処理
            Log.v("Bluetooth", "Bluetooth is supported");
        }else{
            //Bluetooth非対応端末の場合の処理
            Log.v("Bluetooth","Bluetooth is not supported");
            finish();
        }
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            showToastShort(getResources().getString(R.string.wait_till_bt_on));
            //  Bluetooth有効化ダイアログを表示
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        } else {
            Log.v("Bluetooth","Bluetooth is On");
            selectEV3();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:

                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    newDevice = data.getExtras().getBoolean(DeviceListActivity.PAIRING);
                    if (newDevice==true) {
                        enDiscoverable();
                    }
                    startBTCommunicator(address);
                }
                break;
            case REQUEST_ENABLE_BT:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        selectEV3();
                        break;
                    case Activity.RESULT_CANCELED:
                        showToastShort(getResources().getString(R.string.bt_needs_to_be_enabled));
                        finish();
                        break;
                    default:
                        showToastShort(getResources().getString(R.string.problem_at_connecting));
                        finish();
                        break;
                }
        }
    }
    public void startBTCommunicator(String mac_address) {

        connectingProgressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.connecting_please_wait), true);

        if (myBTCommunicator == null) {
            createBTCommunicator();
        }

        switch (((Thread) myBTCommunicator).getState()) {
        	/*
        	 * Thread.getState() Therad.isAlive()
        	 * スレッドの状態を返します。
        	 * 		NEW				false
        	 * 		RUNNABLE		true
        	 */
            case NEW:
                myBTCommunicator.setMACAddress(mac_address);
                myBTCommunicator.start();
                break;
            default:
                connected=false;
                myBTCommunicator = null;
                createBTCommunicator();
                myBTCommunicator.setMACAddress(mac_address);
                myBTCommunicator.start();
                break;
        }
        // optionMenu
        updateButtonsAndMenu();
    }
    // receive messages from the BTCommunicator
    final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message myMessage) {
            switch (myMessage.getData().getInt("message")) {
                case BTCommunicator.STATE_CONNECTED:
                    connected = true;
                    connectingProgressDialog.dismiss();
                    // optionMenu
                    updateButtonsAndMenu();
                    // 接続の音を鳴らす
                    startTone();
                    // 接続した
                    showToastLong(getResources().getString(R.string.connected));
                    showPicture();
                    break;
                case BTCommunicator.STATE_CONNECTERROR:
                    connectingProgressDialog.dismiss();
                    break;
                case BTCommunicator.READED_COLOR:
                    int light = myMessage.getData().getInt("value1");
                    TextView textColor = (TextView)findViewById(R.id.textColor);
                    textColor.setText(""+ light);
                    break;
                case BTCommunicator.READED_TOUCH:
                    float touch = myMessage.getData().getFloat("value1");
                    TextView textTouch = (TextView)findViewById(R.id.textTouch);
                    textTouch.setText(String.format("%.1f", touch));
                    break;
                case BTCommunicator.READED_USONIC:
                    float ultrasonic = myMessage.getData().getFloat("value1");
                    TextView textUSonic = (TextView)findViewById(R.id.textUSonic);
                    textUSonic.setText(String.format("%.1f", ultrasonic));
                    break;

                case BTCommunicator.STATE_RECEIVEERROR:
                case BTCommunicator.STATE_SENDERROR:
                    destroyBTCommunicator();

                    if (bt_error_pending == false) {
                        bt_error_pending = true;
                        // inform the user of the error with an AlertDialog
                        DialogFragment newFragment = MyAlertDialogFragment.newInstance(
                                R.string.bt_error_dialog_title, R.string.bt_error_dialog_message);
                        newFragment.show(getFragmentManager(), "dialog");
                    }

                    break;
            }
        }
    };
    public void doPositiveClick() {
        bt_error_pending = false;
        selectEV3();
    }

    public void createBTCommunicator() {
        // interestingly BT adapter needs to be obtained by the UI thread - so we pass it in in the constructor
        myBTCommunicator = new BTCommunicator(this, myHandler, BluetoothAdapter.getDefaultAdapter());
        btcHandler = myBTCommunicator.getHandler();
    }

    private void enDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
        startActivity(discoverableIntent);
    }
    public void destroyBTCommunicator() {
        if (myBTCommunicator != null) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT);
            myBTCommunicator = null;
        }

        connected = false;
    }
    public void sendBTCmessage(int delay, int message) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        Message myMessage = myHandler.obtainMessage();
        myMessage.setData(myBundle);

        if (delay == 0)
            btcHandler.sendMessage(myMessage);

        else
            btcHandler.sendMessageDelayed(myMessage, delay);
    }
    public void sendBTCmessage(int delay, int message, int value1) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putInt("value1", value1);
        Message myMessage = myHandler.obtainMessage();
        myMessage.setData(myBundle);

        if (delay == 0)
            btcHandler.sendMessage(myMessage);

        else
            btcHandler.sendMessageDelayed(myMessage, delay);
    }

    public void sendBTCmessage(int delay, int message, int value1, int value2) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putInt("value1", value1);
        myBundle.putInt("value2", value2);
        Message myMessage = myHandler.obtainMessage();
        myMessage.setData(myBundle);

        if (delay == 0)
            btcHandler.sendMessage(myMessage);

        else
            btcHandler.sendMessageDelayed(myMessage, delay);
    }

    private void selectEV3() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    @Override
    public void onClick(View v) {
        NumberPicker speedPicker = (NumberPicker)findViewById(R.id.speedPicker);
        int speed = speedPicker.getValue();
        NumberPicker degreePicker = (NumberPicker)findViewById(R.id.degreePicker);
        int degree = degreePicker.getValue();
        NumberPicker timePicker = (NumberPicker)findViewById(R.id.timePicker);
        int time = timePicker.getValue();

        switch (v.getId()) {
            case R.id.motorACon:
                goForward(speed);
                break;
            case R.id.motorACback:
                goBackward(speed);
                break;
            case R.id.motorACoff:
                stopMove();
                break;
            case R.id.motorAon:
                motorAon(speed);
                break;
            case R.id.motorAoff:
                motorAoff();
                break;
            case R.id.motorCon:
                motorCon(speed);
                break;
            case R.id.motorCoff:
                motorCoff();
                break;
            case R.id.rotatebydegree:
                rotateByDegree(speed, degree);
                break;
            case R.id.rotatebytime:
                rotateByTime(speed,time);
                break;
            case R.id.readColorSensor:
                readColorSensor(COLOR_SENSOR_PORT);
                break;
            case R.id.readTouchSensor:
                readTouchSensor(TOUCH_SENSOR_PORT);
                break;
            case R.id.readUltrasonicSensor:
                readUltrasonicSensor(USONIC_SENSOR_PORT);
                break;
        }

    }

    private Menu myMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu = menu;
        MenuItem actionItem = myMenu.add(0,MENU_TOGGLE_CONNECT,1,getResources().getString(R.string.connect));
        myMenu.add(0,MENU_QUIT,2,getResources().getString(R.string.quit));

        updateButtonsAndMenu();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 切断し忘れの対処
        destroyBTCommunicator();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_TOGGLE_CONNECT:

                if (myBTCommunicator == null || connected == false) {
                    selectEV3();

                } else {
                    endTone();
                    destroyBTCommunicator();
                    updateButtonsAndMenu();
                }

                return true;
            case MENU_QUIT:
                endTone();
                destroyBTCommunicator();
                finish();


                return true;
        }

        return false;
    }
    private void updateButtonsAndMenu()	{
        if (myMenu == null) return;
        myMenu.removeItem(MENU_TOGGLE_CONNECT);

        if (connected) {
            myMenu.add(0, MENU_TOGGLE_CONNECT, 1, getResources().getString(R.string.disconnect));
        } else {
            myMenu.add(0, MENU_TOGGLE_CONNECT, 1, getResources().getString(R.string.connect));
        }
    }
    private void startTone() {
        playTone(1319, 500);    // E
        playTone(1568, 500);    // G
        playTone(1760, 500);    // A
    }
    private void endTone() {
        if (connected) {
            playTone(1568, 1000);
            playTone(1319, 500);
        }
    }
    private void playTone(int frequency, int duration){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.PLAYTONE,frequency, duration);
    }
    private void showPicture(){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.SHOWPICTURE);
    }
    private void stopMove(){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.STOP_MOVE);
    }
    private void goForward(int speed){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GO_FORWARD,speed);
    }
    private void goBackward(int speed){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GO_BACKWARD,speed);
    }
    private void motorAon(int speed){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.ROTATE_MOTOR_A,speed);
    }
    private void motorAoff(){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.STOP_MOTOR_A);
    }
    private void motorCon(int speed){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.ROTATE_MOTOR_C,speed);
    }
    private void motorCoff(){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.STOP_MOTOR_C);
    }
    private void rotateByDegree(int speed,int degree){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GO_FORWARD_DEGREE,speed,degree);
    }
    private void rotateByTime(int speed,int time){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GO_FORWARD_TIME,speed,time);
    }
    private void readColorSensor(int port){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.READ_COLOR_SENSOR,port);
    }
    private void readTouchSensor(int port){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.READ_TOUCH_SENSOR,port);
    }
    private void readUltrasonicSensor(int port){
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.READ_USONIC_SENSOR,port);
    }
    private void showToastShort(String textToShow) {
        mShortToast.setText(textToShow);
        mShortToast.show();
    }
    private void showToastLong(String textToShow) {
        mLongToast.setText(textToShow);
        mLongToast.show();
    }

}
