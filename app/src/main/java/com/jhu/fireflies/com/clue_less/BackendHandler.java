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

import org.w3c.dom.Text;

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
    private Handler initialCluesHandler;
    private Handler startOfGameHandler;
    private Handler suggestionAccusationHandler;
    private Handler textBasedHandler;
    private Handler activityLogHandler;
    private String messageToServer;


    public BackendHandler(){

        Thread readerThread = new Thread(new ReaderRunnable(socket));
        readerThread.start();


    }

    public void setGameboardHandler(Handler in){gameboardHandler = in;}
    public void setMainMenuHandler(Handler in){MainMenuHandler = in;}
    public void setLobbyHandler(Handler in){lobbyHandler = in;}
    public void setInitialCluesHandler(Handler in){initialCluesHandler = in;}
    public void setStartOfGameHandler(Handler in){startOfGameHandler = in;}
    public void setSuggestionAccusationHandler(Handler in){suggestionAccusationHandler = in;}
    /*public void setTextBasedHandler(Handler in){textBasedHandler = in;}
    public void setActivityLogHandler(Handler in){activityLogHandler = in;}*/

    public void sendMessage(String s){

        messageToServer = s;
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
                //socket = new Socket("10.0.0.184", 5000);
                socket = new Socket("18.188.114.251", 5000);
                BackendHandlerReference.setSocket(socket);

            }catch (IOException e){
                e.printStackTrace();
            }

            if (socket != null && socket.isConnected()) {
                try {
                    //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    //out.println();
                    //out.flush();

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
                        //...
                        //Game Board Handler
                        Bundle bundleGame = new Bundle();
                        bundleGame.putString("messageFromServer", response);
                        Message msgGame = new Message();
                        msgGame.setData(bundleGame);
                        if(gameboardHandler != null){
                            gameboardHandler.sendMessage(msgGame);
                        }
                        //Main Menu Handler
                        Bundle bundleMain = new Bundle();
                        bundleMain.putString("messageFromServer", response);
                        Message msgMain = new Message();
                        msgMain.setData(bundleMain);
                        if(MainMenuHandler != null){
                            MainMenuHandler.sendMessage(msgMain);
                        }
                        //Lobby Handler
                        Bundle bundleLobby = new Bundle();
                        bundleLobby.putString("messageFromServer", response);
                        Message msgLobby = new Message();
                        msgLobby.setData(bundleLobby);
                        if(lobbyHandler != null){
                            lobbyHandler.sendMessage(msgLobby);
                        }
                        //Initial Clues Handler
                        Bundle bundleInitialClues = new Bundle();
                        bundleInitialClues.putString("messageFromServer", response);
                        Message msgInitialClues = new Message();
                        msgInitialClues.setData(bundleInitialClues);
                        if(initialCluesHandler != null){
                            initialCluesHandler.sendMessage(msgInitialClues);
                        }
                        //Start of Game Handler
                        Bundle bundleStartOfGame = new Bundle();
                        bundleStartOfGame.putString("messageFromServer", response);
                        Message msgStartOfGame = new Message();
                        msgStartOfGame.setData(bundleStartOfGame);
                        if(startOfGameHandler != null){
                            startOfGameHandler.sendMessage(msgStartOfGame);
                        }
                        //Suggestion Accusation Handler
                        Bundle bundleSuggestionAccustation = new Bundle();
                        bundleSuggestionAccustation.putString("messageFromServer", response);
                        Message msgSuggestionAccusation = new Message();
                        msgSuggestionAccusation.setData(bundleSuggestionAccustation);
                        if(suggestionAccusationHandler != null){
                            suggestionAccusationHandler.sendMessage(msgSuggestionAccusation);
                        }
                        /*//Text Based Handler
                        Bundle bundleTextBased = new Bundle();
                        bundleTextBased.putString("messageFromServer", response);
                        Message msgTextBased = new Message();
                        msgTextBased.setData(bundleTextBased);
                        if(MainMenuHandler != null){
                            MainMenuHandler.sendMessage(msgTextBased);
                        }
                        //Activity Log Handler
                        Bundle bundleActivityLog = new Bundle();
                        bundleActivityLog.putString("messageFromServer", response);
                        Message msgActivityLog = new Message();
                        msgActivityLog.setData(bundleActivityLog);
                        if(lobbyHandler != null){
                            lobbyHandler.sendMessage(msgActivityLog);
                        }*/


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

                    out.println(messageToServer);
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
