package com.example.kanehiro_acer.ev3control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.UUID;

public class BTCommunicator extends Thread
                            implements EV3Protocol  {
    public static final int ROTATE_MOTOR_A = 0;
    public static final int ROTATE_MOTOR_B = 1;
    public static final int ROTATE_MOTOR_C = 2;
    public static final int ROTATE_MOTOR_D = 3;
    public static final int STOP_MOTOR_A = 5;
    public static final int STOP_MOTOR_B = 6;
    public static final int STOP_MOTOR_C = 7;
    public static final int STOP_MOTOR_D = 8;

    public static final int PLAYTONE = 10;
    public static final int SHOWPICTURE = 20;

    public static final int READ_COLOR_SENSOR = 30;
    public static final int READ_TOUCH_SENSOR = 31;
    public static final int READ_USONIC_SENSOR = 32;

    public static final int GO_FORWARD = 52;
    public static final int GO_BACKWARD = 53;
    public static final int GO_FORWARD_DEGREE = 54;
    public static final int GO_FORWARD_TIME = 55;
    public static final int STOP_MOVE = 60;

    public static final int DISCONNECT = 99;

    public static final int DISPLAY_TOAST = 1000;
    public static final int STATE_CONNECTED = 1001;
    public static final int STATE_CONNECTERROR = 1002;
    public static final int STATE_RECEIVEERROR = 1004;
    public static final int STATE_SENDERROR = 1005;
    public static final int NO_DELAY = 0;

    public static final int READED_COLOR = 1010;
    public static final int READED_TOUCH = 1011;
    public static final int READED_USONIC = 1012;

    private static final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // this is the only OUI registered by LEGO
    public static final String OUI_LEGO = "00:16:53";

    BluetoothAdapter btAdapter;
    private BluetoothSocket ev3BTsocket = null;
    private DataOutputStream ev3Dos = null;
    private DataInputStream ev3Din = null;
    private boolean connected = false;

    private Handler uiHandler;
    private String mMACaddress;
    private MainActivity myMainActivity;

    public BTCommunicator(MainActivity myMainAct, Handler uiHandler, BluetoothAdapter btAdapter) {
        this.myMainActivity = myMainAct;
        this.uiHandler = uiHandler;
        this.btAdapter = btAdapter;
    }

    public Handler getHandler() {
        return myHandler;
    }

    @Override
    public void run() {

        createEV3connection();

    }
    private void createEV3connection() {
        try {

            BluetoothSocket ev3BTsocketTEMPORARY;
            BluetoothDevice ev3Device = null;
            // Get a BluetoothDevice object for the given Bluetooth hardware address.
            ev3Device = btAdapter.getRemoteDevice(mMACaddress);

            if (ev3Device == null) {
                sendToast(myMainActivity.getResources().getString(R.string.no_paired_ev3));
                sendState(STATE_CONNECTERROR);
                return;
            }

            // Create an RFCOMM BluetoothSocket ready to start a secure outgoing connection to this remote device using SDP lookup of uuid.

            ev3BTsocketTEMPORARY = ev3Device.createRfcommSocketToServiceRecord(SERIAL_PORT_SERVICE_CLASS_UUID);
            ev3BTsocketTEMPORARY.connect(); // Attempt to connect to a remote device.
            ev3BTsocket = ev3BTsocketTEMPORARY;

            ev3Din = new DataInputStream(ev3BTsocket.getInputStream());
            ev3Dos = new DataOutputStream(ev3BTsocket.getOutputStream());

            connected = true;

        } catch (IOException e) {
            Log.d("BTCommunicator", "error createEV3Connection()", e);
            if (myMainActivity.newDevice) {
                sendToast(myMainActivity.getResources().getString(R.string.pairing_message));
                sendState(STATE_CONNECTERROR);

            } else {
                sendState(STATE_CONNECTERROR);
            }

            return;
        }

        sendState(STATE_CONNECTED);
    }
    private void destroyEV3connection() {
        try {
            if (ev3BTsocket != null) {
                connected = false;
                ev3BTsocket.close();
                ev3BTsocket = null;
            }
            ev3Din = null;
            ev3Dos = null;

        } catch (IOException e) {
            sendToast(myMainActivity.getResources().getString(R.string.problem_at_closing));
        }
    }
    private void playTone(int frequency,int duration) {
        byte[] message = new byte[13];

        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        // opSound
        message[3] = OP_SOUND;      // 0x94
        // 0x01
        message[4] = 0x01;
        message[5] = (byte)0x81;    // Volumeは1byte
        message[6] = 30;            // Volume
        message[7] = (byte) 0x82;   // frequencyは2byte
        message[8] = (byte) frequency;
        message[9] = (byte) (frequency >> 8);
        message[10] = (byte)0x82;   // durationは2byte
        // duration
        message[11] = (byte) duration;
        message[12] = (byte)(duration >> 8);

        sendMessage(message);
        waitSomeTime(duration);

    }
    private void showPicture() {
        ByteArrayBuffer buffer = new ByteArrayBuffer(42);
        byte[] message = new byte[22];

        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        // opSound
        message[3] = OP_UI_DRAW;      // 0x84
        // 0x01
        message[4] = 0x13;
        message[5] = 0x00;
        message[6] = (byte) 0x82;
        message[7] = 0x00;
        message[8] = 0x00;
        message[9] = (byte) 0x82;
        message[10] = 0x00;
        message[11] = 0x00;
        message[12] = OP_UI_DRAW;
        message[13] = 0x1C;
        message[14] = 0x01;
        message[15] = (byte) 0x82;
        message[16] = 0x00;
        message[17] = 0x00;
        message[18] = (byte) 0x82;
        message[19] = 0x32;
        message[20] = 0x00;
        message[21] = OP_UI_DRAW;

        buffer.append(message,0,message.length);

        byte[] filename = {'u','i','/','m','i','n','d','s','t','o','r','m','s','.','r','g','f'};
        buffer.append(filename,0,filename.length);

        byte[] message2 = new byte[3];

        message2[0] = 0x00;
        message2[1] = OP_UI_DRAW;
        message2[2] = 0x00;
        buffer.append(message2,0,message2.length);

        sendMessage(buffer.toByteArray());

    }
    private void moveMotorAC(int speed) {
        byte[] message = new byte[11];

        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        message[3] = OP_OUTPUT_SPEED;       // 0xA5
        message[4] = LAYER_MASTER;
        message[5] = MOTOR_A+MOTOR_C;
        message[6] = (byte)0x81;
        message[7] = (byte)speed;           // speed 1 to 100
        message[8] = OP_OUTPUT_START;       // 0xA6
        message[9] = LAYER_MASTER;
        message[10] = MOTOR_A+MOTOR_C;
        sendMessage(message);

    }

    private void moveBackwardMotorAC(int speed) {
        byte[] message = new byte[11];
        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        message[3] = OP_OUTPUT_SPEED;       // 0xA5
        message[4] = LAYER_MASTER;
        message[5] = MOTOR_A+MOTOR_C;
        message[6] = (byte)0x81;
        message[7] = (byte)-speed;        // speed -1 to -100
        message[8] = OP_OUTPUT_START;       // 0xA6
        message[9] = LAYER_MASTER;
        message[10] = MOTOR_A+MOTOR_C;
        sendMessage(message);

    }
    private void stopMotorAC() {
        byte[] message = new byte[7];

        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        message[3] = OP_OUTPUT_STOP;
        message[4] = LAYER_MASTER;
        message[5] = MOTOR_A+MOTOR_C;
        message[6] = 1;            // break
        sendMessage(message);

    }
    private void moveMotorA(int speed) {
        byte[] message = new byte[11];
        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        message[3] = OP_OUTPUT_SPEED;       // 0xA5
        message[4] = LAYER_MASTER;
        message[5] = MOTOR_A;
        message[6] = (byte)0x81;
        message[7] = (byte)speed;            // speed
        message[8] = OP_OUTPUT_START;       // 0xA6
        message[9] = LAYER_MASTER;
        message[10] = MOTOR_A;
        sendMessage(message);

    }

    private void stopMotorA() {
        byte[] message = new byte[7];
        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        message[3] = OP_OUTPUT_STOP;
        message[4] = LAYER_MASTER;
        message[5] = MOTOR_A;
        message[6] = 1;            // break
        sendMessage(message);
    }
    private void moveMotorC(int speed) {
        byte[] message = new byte[11];
        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        message[3] = OP_OUTPUT_SPEED;       // 0xA5
        message[4] = LAYER_MASTER;
        message[5] = MOTOR_C;
        message[6] = (byte)0x81;
        message[7] = (byte)speed;            // speed
        message[8] = OP_OUTPUT_START;       // 0xA6
        message[9] = LAYER_MASTER;
        message[10] = MOTOR_C;
        sendMessage(message);

    }

    private void stopMotorC() {
        byte[] message = new byte[7];
        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        message[3] = OP_OUTPUT_STOP;
        message[4] = LAYER_MASTER;
        message[5] = MOTOR_C;
        message[6] = 1;            // break
        sendMessage(message);
    }
    private void moveMotorACbyDegree(int speed,int degree) {
        // TODO 角度指定でモータを回す
        degree *= 360;
        int degree1=0;
        int degree2=0;

        if (degree > 360) {
            degree2 = 180;
            degree1 = degree - degree2;
        } else {
            degree1=degree;
        }
        byte[] message = new byte[16];
        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        message[3] = OP_OUTPUT_STEP_SPEED;      // 0xAE
        message[4] = LAYER_MASTER;
        message[5] = MOTOR_A+MOTOR_C;
        message[6] = (byte)0x81;
        message[7] = (byte)speed;            // speed
        message[8] = 0x00;
        message[9] = (byte)0x82;
        message[10] = (byte)degree1;   // 5回転 1620 + 180
        message[11] = (byte)(degree1 >> 8);
        message[12] = (byte)0x82;
        message[13] = (byte)degree2;
        message[14] = (byte)(degree2 >> 8);
        message[15] = 1;
        sendMessage(message);
    }

    private void moveMotorACbyTime(int speed,int time) {
        // TODO 時間指定でモータを回す
        time *=1000;
        int time1=0;
        int time2=0;
        if (time > 2000) {
            time2 = 1000;
            time1 = time - time2;
        } else {
            time1=time;
        }

        byte[] message = new byte[16];
        message[0] = DIRECT_COMMAND_NOREPLY;
        message[1] = 0x00;
        message[2] = 0x00;
        message[3] = OP_OUTPUT_TIME_SPEED;      // 0xAE
        message[4] = LAYER_MASTER;
        message[5] = MOTOR_A+MOTOR_C;
        message[6] = (byte)0x81;
        message[7] = (byte)speed;            // speed
        message[8] = 0x00;
        message[9] = (byte)0x82;
        message[10] = (byte)time1;   // 8秒 3000 + 1000
        message[11] = (byte)(time1 >> 8);
        message[12] = (byte)0x82;
        message[13] = (byte)time2;
        message[14] = (byte)(time2 >> 8);
        message[15] = 1;
        sendMessage(message);
    }
    private void readColorSensor(int port) {
        byte[] message = new byte[9];
        message[0] = DIRECT_COMMAND_REPLY;  // 00
        message[1] = 0x01;
        message[2] = 0x00;
        message[3] = OP_INPUT_READ;
        message[4] = 0x00;  // Layer
        message[5] = (byte) port;  // port
        message[6] = COLOR;  // type
        message[7] = COL_REFLECT;  // mode
        message[8] = 0x60;  //
        sendMessage(message);

        byte[] reply = readData();

        if (reply[2] == DIRECT_COMMAND_SUCCESS) {
            int percentValue = reply[3];
            Log.v("Color sensor", "value= " + percentValue);
            sendState(READED_COLOR,percentValue);
        }
    }
    private void readTouchSensor(int port) {
        // Touch SensorはSi Unit Valueを取得する
        // 1.0と0.0が返ってくる
        byte[] message = new byte[9];
        message[0] = DIRECT_COMMAND_REPLY;  // 00
        message[1] = 0x04;
        message[2] = 0x00;
        message[3] = OP_INPUT_READSI;
        message[4] = 0x00;          // Layer
        message[5] = (byte)port;    // port
        message[6] = TOUCH;         // type
        message[7] = TOUCH_TOUCH;   // mode
        message[8] = 0x60;
        sendMessage(message);

        byte[] reply = readData();

        if (reply[2] == DIRECT_COMMAND_SUCCESS) {
            // Read the SI unit value in float type
            byte[] data = Arrays.copyOfRange(reply, 3, reply.length);
            float siUnitValue = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            Log.v("Touch sensor", "value= " + siUnitValue);
            sendState(READED_TOUCH,siUnitValue);
        }
    }
    private void UltrasonicSensor(int port) {
        byte[] message = new byte[9];

        // Direct command telegram, no response
        message[0] = DIRECT_COMMAND_REPLY;
        message[1] = 0x04;
        message[2] = 0x00;
        message[3] = OP_INPUT_READSI;
        message[4] = 0x00;          // Layer
        message[5] = (byte) port;   // port
        message[6] = ULTRASONIC;    // type
        message[7] = US_CM;         // mode
        message[8] = 0x60;
        sendMessage(message);

        byte[] reply = readData();

        if (reply[2] == DIRECT_COMMAND_SUCCESS) {
            // Read the SI unit value in float type
            byte[] data = Arrays.copyOfRange(reply, 3, reply.length);
            float siUnitValue = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            Log.v("Ultrasonic sensor", "value= " + siUnitValue);
            sendState(READED_USONIC,siUnitValue);
       }
    }

    public byte[] readData() {
        byte[] buffer = new byte[2];
        byte[] result;
        int numBytes;
        try {
            // Calculate the size of response by reading 2 bytes.
            ev3Din.read(buffer, 0, buffer.length);
            // Reply size
            numBytes = (int) buffer[0] + (buffer[1] << 8);

            // Read the body of response
            result = new byte[numBytes];
            ev3Din.read(result, 0, numBytes);


        }
        catch (IOException e) {
            Log.e("readdata", "Read failed.", e);
            throw new RuntimeException(e);
        }
        Log.v("readdata", "Read: " + result);
        return result;
    }
    private boolean sendMessage(byte[] message) {
        if (ev3Dos == null) {
            return false;
        }

        int bodyLength = message.length + 2; // add 2 for identification codes.
        byte[] header = {
                (byte) (bodyLength & 0xff), (byte) ((bodyLength >>> 8) & 0xff),
                0x00, 0x00
        };
        Log.v("sendMessage", "header=" + byteToStr(header));
        Log.v("sendMessage", "message=" + byteToStr(message));
        try {
            ev3Dos.write(header);
            ev3Dos.write(message);
            ev3Dos.flush();
        } catch (IOException ioe) {
            sendState(STATE_SENDERROR);
            return false;
        }
        return true;
    }
    private String byteToStr(byte[] mess) {
        StringBuffer strbuf = new StringBuffer();
        for(int i=0;i<mess.length;i++) {
            strbuf.append(String.format("%02x", (mess[i])));
        }
        return strbuf.toString();

    }

    private void waitSomeTime(int millis) {
        try {
            Thread.sleep(millis);

        } catch (InterruptedException e) {
        }
    }

    private void sendToast(String toastText) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", DISPLAY_TOAST);
        myBundle.putString("toastText", toastText);
        sendBundle(myBundle);
    }
    private void sendState(int message) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        sendBundle(myBundle);
    }
    private void sendState(int message,int value) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putInt("value1", value);
        sendBundle(myBundle);
    }
    private void sendState(int message,float value) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putFloat("value1",value);
        sendBundle(myBundle);
    }
    private void sendBundle(Bundle myBundle) {
        Message myMessage = myHandler.obtainMessage();  // Returns a new Message from the global message pool.
        myMessage.setData(myBundle);
        uiHandler.sendMessage(myMessage);
    }

    // receive messages from the UI
    final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message myMessage) {

            //int message;

            switch (myMessage.getData().getInt("message")) {
                case ROTATE_MOTOR_A:
                    moveMotorA(myMessage.getData().getInt("value1"));
                    break;
                case ROTATE_MOTOR_C:
                    moveMotorC(myMessage.getData().getInt("value1"));
                    break;
                case STOP_MOTOR_A:
                    stopMotorA();
                    break;
                case STOP_MOTOR_C:
                    stopMotorC();
                    break;
                case PLAYTONE:
                    playTone(myMessage.getData().getInt("value1"), myMessage.getData().getInt("value2"));
                    break;
                case SHOWPICTURE:
                    showPicture();
                    break;
                case READ_COLOR_SENSOR:
                    readColorSensor(myMessage.getData().getInt("value1"));
                    break;
                case READ_TOUCH_SENSOR:
                    readTouchSensor(myMessage.getData().getInt("value1"));
                    break;
                case READ_USONIC_SENSOR:
                    UltrasonicSensor(myMessage.getData().getInt("value1"));
                    break;
                case GO_FORWARD:
                    moveMotorAC(myMessage.getData().getInt("value1"));
                    break;
                case GO_BACKWARD:
                    moveBackwardMotorAC(myMessage.getData().getInt("value1"));
                    break;
                case GO_FORWARD_DEGREE:
                    moveMotorACbyDegree(myMessage.getData().getInt("value1"), myMessage.getData().getInt("value2"));
                    break;
                case GO_FORWARD_TIME:
                    moveMotorACbyTime(myMessage.getData().getInt("value1"), myMessage.getData().getInt("value2"));
                    break;
                case STOP_MOVE:
                    stopMotorAC();
                    break;
                case DISCONNECT:
                    destroyEV3connection();
                    break;
            }
        }
    };

    public void setMACAddress(String mMACaddress) {
        this.mMACaddress = mMACaddress;
    }

}
