package com.jhu.fireflies.com.clue_less;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;

public class MainMenuActivity extends AppCompatActivity {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    BackendHandler backendHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);

        Button joinGame = (Button) findViewById(R.id.joinGame);

        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this,
                                           LobbyActivity.class);

                startActivity(intent);
            }
        });


        backendHandler = new BackendHandler();

        BackendHandlerReference.setBackendHandler(backendHandler);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String msgFromServer = (String) msg.getData().get("messageFromServer");
                Toast.makeText(MainMenuActivity.this, msgFromServer, Toast.LENGTH_SHORT).show();
            }
        };

        backendHandler.setMainMenuHandler(handler);

       /* Handler handler = new Handler(){
          @Override
            public void handleMessage(Message msg){
              Log.d("Received Message", "handleMessage: " + msg.getData().getString("receivedMsg"));
              BackendHandlerReference.addToServerLog(msg.getData().getString("receivedMsg"), "server");
          }
        };
        BackendHandler backendHandler = new BackendHandler(handler);
        BackendHandlerReference.setBackendHandler(backendHandler);

        backendHandler.execute("test");*/

       Handler networkHandler = new Handler();

       //Handle Network connection on different thread
      /*  final Thread networkThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    socket = new Socket("10.0.0.184", 5000);
                    // Create PrintWriter object for sending messages to server.
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    //Create BufferedReader object for receiving messages from server.
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    out.println("Send this message\n");
                    out.flush();

                    while(!in.ready())
                        try {
                            sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    String response = in.readLine();

                    Log.d("MainMenuActivity", "networkThread: "+response);



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };*/

        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backendHandler.sendMessage("test");
               /* if(networkThread.getState() == Thread.State.NEW){
                    Log.d("MainMenuActivity", "Starting new thread");
                    networkThread.start();
                }else {
                    Log.d("MainMenuActivity", "Thread already alive ");
                }*/

                //BackendHandler backendHandler = BackendHandlerReference.getBackendHandler();
                //backendHandler.sendMessage("sent another message");
            }
        });


    }

    public void onTextBasedClicked(View view){
        Intent intent = new Intent(MainMenuActivity.this,
                TextBased.class);

        startActivity(intent);
    }

}
