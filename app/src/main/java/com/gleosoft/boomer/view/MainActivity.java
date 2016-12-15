package com.gleosoft.boomer.view;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gleosoft.boomer.R;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends Activity implements IAcceptServerData {
    private  Thread th = null;
    private WifiManager mWifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_title);
        mWifiManager = (WifiManager) this.getSystemService(this.WIFI_SERVICE);
        int state = mWifiManager.getWifiState();
        if(state == 3) {
            th = new Thread(new AcceptThread());
            th.start();
        }else{
            Toast.makeText(this,"无wifi",Toast.LENGTH_SHORT).show();
        }
        //禁用
        findViewById(R.id.button).setEnabled(false);
        findViewById(R.id.button2).setEnabled(false);
        findViewById(R.id.button3).setEnabled(false);
        findViewById(R.id.button4).setEnabled(false);

    }
     public void start(View v){
         try {
             Button btn = (Button) findViewById(v.getId());
             switch (v.getId())
             {
                 case R.id.button:
                 case R.id.button2:
                 case R.id.button3:
                 case R.id.button4:
                     new Thread(new BThread(btn.getText().toString())).start();
                     Toast.makeText(this,"发送成功",Toast.LENGTH_SHORT).show();
                     break;
                 case R.id.re:
                     int state = mWifiManager.getWifiState();

                     findViewById(R.id.button).setEnabled(false);
                     findViewById(R.id.button2).setEnabled(false);
                     findViewById(R.id.button3).setEnabled(false);
                     findViewById(R.id.button4).setEnabled(false);

                     Button bt = (Button)findViewById(R.id.button);
                     bt.setText("未连接");
                     bt = (Button)findViewById(R.id.button2);bt.setText("未连接");
                     bt = (Button)findViewById(R.id.button3);bt.setText("未连接");
                     bt = (Button)findViewById(R.id.button4);bt.setText("未连接");

                     if(state == 3) {
                         if(th != null) {
                             th.interrupt();
                         }
                         th = new Thread(new AcceptThread());
                         th.start();
                         Toast.makeText(this,"刷新成功",Toast.LENGTH_SHORT).show();
                     }else{
                         Toast.makeText(this,"无wifi",Toast.LENGTH_SHORT).show();
                     }
                     break;
             }
         }catch (Exception e){
             e.printStackTrace();
         }
    }
    private Handler MyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Button btn = (Button) findViewById(R.id.button);
                    btn.setText( msg.obj.toString());
                    btn.setEnabled(true);
                    break;
                case 2:
                    Button btn2 = (Button) findViewById(R.id.button2);
                    btn2.setText( msg.obj.toString());
                    btn2.setEnabled(true);
                    break;
                case 3:
                    Button btn3 = (Button) findViewById(R.id.button3);
                    btn3.setText( msg.obj.toString());
                    btn3.setEnabled(true);
                    break;
                case 4:
                    Button btn4 = (Button) findViewById(R.id.button4);
                    btn4.setText( msg.obj.toString());
                    btn4.setEnabled(true);
                    break;
                default:
                    break;
            }

        }
    };

    public class AcceptThread implements Runnable {

        public AcceptThread() {

        }

        @Override
        public void run() {
            while (true) {
                String data = Tools.getServerData();
                if(data != null) {
                    acceptUdpData(data);
                }
            }
        }
    }

    @Override
    public void acceptUdpData(String data) {
        Message msg = new Message();
        switch (data) {
            case "001":
                msg.what = 1;
                break;
            case "002":
                msg.what = 2;
                break;
            case "003":
                msg.what = 3;
                break;
            case "004":
                msg.what = 4;
                break;
            default:
                break;
        }
        msg.obj = data;
        MyHandler.sendMessage(msg);
    }



    public class BThread implements Runnable {
       String str = "";
        public BThread(String msg) {
            str = msg;
        }

        @Override
        public void run() {
            try{
            InetAddress serverAddr = InetAddress.getByName(IAcceptServerData.SERVERIP);
            DatagramSocket socket = new DatagramSocket();
            byte[] buf = str.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, IAcceptServerData.SERVERPORT);
            socket.send(packet);
                socket.send(packet);
                socket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
