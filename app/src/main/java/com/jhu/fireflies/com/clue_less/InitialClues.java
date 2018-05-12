package com.jhu.fireflies.com.clue_less;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class InitialClues extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_clues);

        //Handle Messages from the Backend
        BackendHandler backendHandler = BackendHandlerReference.getBackendHandler();
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                String msgFromServer = (String) msg.getData().get("messageFromServer");
                Toast.makeText(InitialClues.this, msgFromServer, Toast.LENGTH_SHORT).show();
            }
        };
        backendHandler.setMainMenuHandler(handler);

        String initialClues = "";
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("StartOfGame", Context.MODE_PRIVATE);
        initialClues = sharedPreferences.getString("initialCards", "error reading cards");

        setInitialCLues(initialClues);
    }

    public void closeInitialClues(View view){
        Intent intent = new Intent(InitialClues.this,
                GameBoard.class);

        startActivity(intent);

    }

    public void setInitialCLues(String clues){
        TextView initialClues = (TextView) findViewById(R.id.initalClueCards);

        initialClues.setText(clues);
    }
}
