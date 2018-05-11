package com.jhu.fireflies.com.clue_less;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Shawn on 4/29/18.
 * some code from https://stackoverflow.com/questions/13760332/socket-read-and-write-threads
 */

public class BackendHandler {

    private Socket socket;
    private Handler gameboardHandler;
    private Handler MainMenuHandler;
    private Handler lobbyHandler;


    public BackendHandler(){

        Thread readerThread = new Thread(new ReaderRunnable(socket));
        readerThread.start();


    }

    public void setGameboardHandler(Handler in){gameboardHandler = in;}
    public void setMainMenuHandler(Handler in){MainMenuHandler = in;}
    public void setLobbyHandler(Handler in){lobbyHandler = in;}

    public void sendMessage(String s){

        Thread writerThread = new Thread(new WriteRunnable(socket));
        writerThread.start();
    }

    // You can put this class outside activity with public scope
    class ReaderRunnable implements Runnable {
        Socket socket;

        public ReaderRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try{
                socket = new Socket("10.0.0.184", 5000);
                //socket = new Socket("18.188.114.251", 5000);
                BackendHandlerReference.setSocket(socket);

            }catch (IOException e){
                e.printStackTrace();
            }

            if (socket != null && socket.isConnected()) {
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println();
                    out.flush();

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                    boolean end = false;
                    while (!end) {
                        while (!in.ready()) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        String response = in.readLine();
                        if(response == "end"){
                            end = true;
                        }

                        //send message to all handlers
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("messageFromServer", response);
                        msg.setData(bundle);

                        if(gameboardHandler != null){
                            gameboardHandler.sendMessage(msg);
                        }

                        Message msgMain = new Message();
                        Bundle bundleMain = new Bundle();
                        bundleMain.putString("messageFromServer", response);
                        msgMain.setData(bundleMain);
                        if(MainMenuHandler != null){
                            MainMenuHandler.sendMessage(msgMain);
                        }

                        Message msgLobby = new Message();
                        Bundle bundleLobby = new Bundle();
                        bundleLobby.putString("messageFromServer", response);
                        msg.setData(bundleLobby);
                        if(lobbyHandler != null){
                            lobbyHandler.sendMessage(msgLobby);
                        }


                        //Do reader code
                        Log.d("BackendHandler", "Reader: " + response);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //Handle error case
            }
        }


    }

    // You can put this class outside activity with public scope
    class WriteRunnable implements Runnable {
        Socket socket;

        public WriteRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            this.socket = BackendHandlerReference.getSocket();

            if (socket != null && socket.isConnected()) {
                try {
                    //Do writer code
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    out.println();
                    out.flush();
                    Log.d("BackendHandler", "writerThread: this worked");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                //Handle error case
                Log.d("BackendHandler", "writerThread: socket null");
            }
            return;
        }
    }


}
