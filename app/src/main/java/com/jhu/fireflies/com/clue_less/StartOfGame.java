package com.jhu.fireflies.com.clue_less;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class StartOfGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_of_game_layout);

        setAllPlayersInvisble();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("StartOfGame", Context.MODE_PRIVATE);
        String savedValues = sharedPreferences.getString("numberOfPlayers", "");
        /*int numberOfPlayers;
        List<String> messageList = Arrays.asList(savedValues.split(","));
        try{
            numberOfPlayers = Integer.parseInt(messageList.get(1));
        }catch (NumberFormatException e){
            numberOfPlayers = 0;
        }

        for(int i =1; i < numberOfPlayers+1; i++){
            int numberOfCards;
            try{
                numberOfCards = Integer.parseInt(messageList.get(i));
            }catch (NumberFormatException e){
                numberOfCards = 0;
            }
            setPlayerText(i,numberOfCards);
        }*/

        //Handle Messages from the Backend
        BackendHandler backendHandler = BackendHandlerReference.getBackendHandler();
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String msgFromServer = (String) msg.getData().get("messageFromServer");
                Toast.makeText(StartOfGame.this, msgFromServer, Toast.LENGTH_SHORT).show();

                int numberOfPlayers;
                List<String> messageList = Arrays.asList(msgFromServer.split(","));

                if(messageList.get(0) != "9"){return;}

                try{
                    numberOfPlayers = Integer.parseInt(messageList.get(1));
                }catch (NumberFormatException e){
                    numberOfPlayers = 0;
                }


                for(int i =2; i < numberOfPlayers+2; i++){
                    int numberOfCards;
                    try{
                        numberOfCards = Integer.parseInt(messageList.get(i));
                    }catch (NumberFormatException e){
                        numberOfCards = 0;
                    }
                    setPlayerText(i,numberOfCards);
                }
                //Log.d("StartOfGame1", "finished setting text");

                String initialCards = "";
                for(int i = numberOfPlayers+2; i < messageList.size(); i++){
                    initialCards = initialCards + messageList.get(i) + "\n";
                }


                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("StartOfGame", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.d("StartOfGame", "list size: " + messageList.size());
                Log.d("StartOfGame", "Initial Cards: " + initialCards);
                editor.putString("initialCards", initialCards);
                editor.putString("numberOfPlayers", msgFromServer);
                editor.commit();
            }
        };
        backendHandler.setMainMenuHandler(handler);

        /*setPlayerText(1, 5);
        setPlayerText(2, 4);
        setPlayerText(3, 5);
        setPlayerText(4, 4);*/

        Button okButton = (Button) findViewById(R.id.startOfGameClose);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartOfGame.this,
                        GameBoard.class);

                startActivity(intent);
                finish();
            }
        });
    }

    private void setAllPlayersInvisble(){

        TextView textView = (TextView) findViewById(R.id.player1Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player2Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player3Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player4Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player5Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player6Start);
        textView.setVisibility(View.INVISIBLE);

        return;
    }

    private void setPlayerText(int player, int numberOfCards){

        int id = 0;
        switch (player){
            case 1: id = R.id.player1Start; break;
            case 2: id = R.id.player2Start; break;
            case 3: id = R.id.player3Start; break;
            case 4: id = R.id.player4Start; break;
            case 5: id = R.id.player5Start; break;
            case 6: id = R.id.player6Start; break;
        }

        if(id <0 && id >6){
            return; //invalid id given
        }

        TextView textView = (TextView) findViewById(id);
        textView.setVisibility(View.VISIBLE);

        String message = "Player " + player + " receives " + numberOfCards + " cards.";
        textView.setText(message);
        return;
    }

}
