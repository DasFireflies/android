package com.jhu.fireflies.com.clue_less;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class SuggestionAccusationActivity extends AppCompatActivity {

    private int suggestAccuseConfig = 0;
    private String characterRoom;
    private String suspectValue;
    private String weaponValue;
    private BackendHandler backendHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestion_accusation_layout);

        //get reference to backend
        backendHandler = BackendHandlerReference.getBackendHandler();

        //get Character room
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CharacterDetails", Context.MODE_PRIVATE);
        characterRoom = sharedPreferences.getString("characterRoom", "defaultRoom");


        //set the list of suspects in dropdown
        final Spinner suspectSpinner = (Spinner) findViewById(R.id.suspectSpinner);
        final ArrayAdapter<CharSequence> suspectAdapter = ArrayAdapter.createFromResource(this,R.array.suspects, android.R.layout.simple_spinner_item);
        suspectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        suspectSpinner.setAdapter(suspectAdapter);

        //set the list of weapons in dropdown
        final Spinner weaponSpinner = (Spinner) findViewById(R.id.weaponSpinner);
        ArrayAdapter<CharSequence> weaponAdapter = ArrayAdapter.createFromResource(this,R.array.weapons, android.R.layout.simple_spinner_item);
        weaponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weaponSpinner.setAdapter(weaponAdapter);

        Button confirmButtom = (Button) findViewById(R.id.confirmSuggestion);
        confirmButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suspectValue = suspectSpinner.getSelectedItem().toString();
                weaponValue = weaponSpinner.getSelectedItem().toString();
                String test = "Suspect: " + suspectValue + "\nWeapon: " + weaponValue + "\nRoom: " + characterRoom;
                Toast.makeText(SuggestionAccusationActivity.this, test, Toast.LENGTH_SHORT).show();


                backendHandler.sendMessage(suggestAccuseConfig + "," + suspectValue + "," + characterRoom + "," + weaponValue);
            }
        });

        //set room name
        setRoom(characterRoom);

        int suggestAccuse = getIntent().getExtras().getInt("suggestAccuse");
        setSuggestAccuse(suggestAccuse);

        //set backend handler callback
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                String msgFromServer = (String) msg.getData().get("messageFromServer");
                List<String> messageList = Arrays.asList(msgFromServer.split(","));

                String disprovedCard = "Suggestion not disproved";
                if(messageList.size()>2){
                    disprovedCard = messageList.get(2);
                }

                String playerWhoDisproved = messageList.get(1);
                String disproveMessage;
                if(playerWhoDisproved == "0"){
                    disproveMessage = "No one was able to disprove your suggestion of " + suspectValue + " with the " + weaponValue + " in the " + characterRoom;
                }else{
                    disproveMessage = "Player " + playerWhoDisproved + " showed you " + disprovedCard;
                }

                BackendHandlerReference.addToServerLog("server", disproveMessage);
                Toast.makeText(SuggestionAccusationActivity.this, disproveMessage, Toast.LENGTH_SHORT).show();

            }
        };
        backendHandler.setSuggestionAccusationHandler(handler);


    }

    private void setSuggestAccuse(int i){
        TextView suggestAccuseLabel = (TextView) findViewById(R.id.suggestionAccusationText);

        if(i == 1){
            suggestAccuseLabel.setText("Make a suggestion...");
            suggestAccuseConfig = 3;
        }else if (i ==2){
            suggestAccuseLabel.setText("Make an accusation...");
            suggestAccuseConfig = 4;
        }

    }

    private void setRoom(String roomName){
        TextView roomLabel = (TextView) findViewById(R.id.roomLabelText);
        roomLabel.setText(roomName);

    }
}
