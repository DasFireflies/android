package com.jhu.fireflies.com.clue_less;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
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
        setSharedPreferencesDefaults();

        Button joinGame = (Button) findViewById(R.id.joinGame);

        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Put code to reset all shared preferences to defaults
                 *
                 */

                Intent intent = new Intent(MainMenuActivity.this,
                                           LobbyActivity.class);

                startActivity(intent);
            }
        });

        Button newGame = (Button) findViewById(R.id.newGame);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Put code to reset all shared preferences to defaults
                 *
                 */

                Intent intent = new Intent(MainMenuActivity.this,
                        LobbyActivity.class);

                startActivity(intent);

            }
        });


        backendHandler = new BackendHandler();

        BackendHandlerReference.setBackendHandler(backendHandler);

        //Handle Messages from the Backend
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String msgFromServer = (String) msg.getData().get("messageFromServer");
                return;
                //Toast.makeText(MainMenuActivity.this, msgFromServer, Toast.LENGTH_SHORT).show();
            }
        };
        backendHandler.setMainMenuHandler(handler);



       Handler networkHandler = new Handler();



        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backendHandler.sendMessage("test");

            }
        });


    }

    public void onTextBasedClicked(View view){
        Intent intent = new Intent(MainMenuActivity.this,
                TextBased.class);

        startActivity(intent);
        finish();
    }

    private void setSharedPreferencesDefaults(){
        //Put code for saving defaults here
        HashMap<String, String> positions = new HashMap<>();
        positions.put("Baron Green", "Hall 11");
        positions.put("Lady Peacock", "Hall 8");
        positions.put("Madam White", "Hall 12");
        positions.put("Lady Scarlet", "Hall 2");
        positions.put("Dr. Plum", "Hall 3");
        positions.put("General Mustard", "Hall 5");
        saveMap(positions);

    }

    private void saveMap(HashMap<String,String> inputMap){
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            Log.d("MainMenu", jsonString);
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("Positions").commit();
            editor.putString("Positions", jsonString);
            editor.commit();
        }
    }

}
