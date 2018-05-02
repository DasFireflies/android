package com.jhu.fireflies.com.clue_less;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;

public class MainMenuActivity extends AppCompatActivity {

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


        final Handler handler = new Handler();
        BackendHandler backendHandler = new BackendHandler(handler);

        BackendHandlerReference.setBackendHandler(backendHandler);

        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendHandler backendHandler = BackendHandlerReference.getBackendHandler();
                backendHandler.execute("test");
            }
        });


    }

}
