package com.jhu.fireflies.com.clue_less;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Shawn on 4/29/18.
 */

public class BackendHandler extends AsyncTask<String, String, TCPClient> {


            //socket = new Socket("18.188.114.251", 5000);
            private static final String     COMMAND     = "test"      ;
    private              TCPClient  tcpClient                        ;
    private Handler mHandler                         ;
    private static final String     TAG         = "ShutdownAsyncTask";

    int count = 0;
    /**
     * ShutdownAsyncTask constructor with handler passed as argument. The UI is updated via handler.
     * In doInBackground(...) method, the handler is passed to TCPClient object.
     * @param mHandler Handler object that is retrieved from MainActivity class and passed to TCPClient
     *                 class for sending messages and updating UI.
     */
    public BackendHandler(Handler mHandler){
        this.mHandler = mHandler;
    }


    /**
     * Overriden method from AsyncTask class. There the TCPClient object is created.
     * @param params From MainActivity class empty string is passed.
     * @return TCPClient object for closing it in onPostExecute method.
     */
    @Override
    protected TCPClient doInBackground(String... params) {
        Log.d(TAG, "In do in background");

        try {

            tcpClient = new TCPClient(mHandler,
                    COMMAND,
                    "10.0.0.184",
                    new TCPClient.MessageCallback() {
                        @Override
                        public void callbackMessageReceiver(String message) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("receivedMsg", message);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);

                            Log.d("PLEASE WORK", "callbackMessageReceiver: " + message);
                            publishProgress(message);
                        }
                    });

        } catch (NullPointerException e) {
            Log.d(TAG, "Caught null pointer exception");
            e.printStackTrace();
        }
        tcpClient.run();
        return null;
    }

    /**
     * Overriden method from AsyncTask class. Here we're checking if server answered properly.
     * @param values If "restart" message came, the client is stopped and computer should be restarted.
     *               Otherwise "wrong" message is sent and 'Error' message is shown in UI.
     */
    @Override
    protected void onProgressUpdate(String... values) {
        //super.onProgressUpdate(values);
        Log.d(TAG, "In progress update, values: " + values.toString());
        /*if(values[0].equals("shutdown")){
            tcpClient.sendMessage(COMMAND);
            tcpClient.stopClient();
            mHandler.sendEmptyMessageDelayed(MainActivity.SHUTDOWN, 2000);

        }else{
            tcpClient.sendMessage("wrong");
            mHandler.sendEmptyMessageDelayed(MainActivity.ERROR, 2000);
            tcpClient.stopClient();
        }*/
    }

    public void sendMessage(String message){
        tcpClient.setCommand(message);
        count ++;
        if(count>3){
            tcpClient.stopClient();
        }
    }

    @Override
    protected void onPostExecute(TCPClient result){
      //  super.onPostExecute(result);
        Log.d(TAG, "In on post execute");
       /* if(result != null && result.isRunning()){
            result.stopClient();
        }
        mHandler.sendEmptyMessageDelayed(MainActivity.SENT, 4000);*/

    }

}
