package com.jhu.fireflies.com.clue_less;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        //set the list of room in dropdown
        final Spinner roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
        ArrayAdapter<CharSequence> roomAdapter = ArrayAdapter.createFromResource(this,R.array.rooms, android.R.layout.simple_spinner_item);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(roomAdapter);


        Button confirmButtom = (Button) findViewById(R.id.confirmSuggestion);
        confirmButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner roomSpinner = (Spinner) findViewById(R.id.roomSpinner);

                suspectValue = suspectSpinner.getSelectedItem().toString();
                weaponValue = weaponSpinner.getSelectedItem().toString();
                String roomGuess = characterRoom;

                if(suggestAccuseConfig == 4){
                    roomGuess = roomSpinner.getSelectedItem().toString();
                }


                String test = "Suspect: " + suspectValue + "\nWeapon: " + weaponValue + "\nRoom: " + roomGuess;
                Toast.makeText(SuggestionAccusationActivity.this, test, Toast.LENGTH_SHORT).show();


                //send message
                backendHandler.sendMessage(suggestAccuseConfig + "," + suspectValue + "," + roomGuess + "," + weaponValue);
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
                //return if this message isn't for us
                if(messageList.get(0).compareTo("-3") != 0 && messageList.get(0).compareTo("4") != 0){return;}

                Log.d("SuggestionAccustion", messageList.get(0));
                if (messageList.get(0).compareTo("-3") == 0) {
                    String message;
                    if (messageList.get(1).compareTo("0") == 0) {
                        //if your suggestion was not disproved
                        message = "No one was able to disprove your suggestion";
                    }
                    else {
                        //if your suggestion was disproved
                        message = messageList.get(2)+" disproved your suggestion by showing you the card: " +messageList.get(3);
                    }
                    BackendHandlerReference.addToServerLog(message);
                }else if (messageList.get(0).compareTo("4") == 0) {
                    String message;
                    if (messageList.get(2).compareTo("0") == 0) {
                        //if accusation was incorrect
                        message = messageList.get(1) + " made an incorrect accusation. They lose";
                    }
                    else {
                        //if accusation was correct
                        message = messageList.get(1) + " made a correct accusation. They win!!!!!!!!!!!!!!!!!!! The correct answer was that " + messageList.get(3)+" did it in the "+messageList.get(4)+" with the "+messageList.get(5);
                    }
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    BackendHandlerReference.addToServerLog(message);
                }

            }
        };
        backendHandler.setSuggestionAccusationHandler(handler);


    }

    private void setSuggestAccuse(int i){
        TextView suggestAccuseLabel = (TextView) findViewById(R.id.suggestionAccusationText);
        TextView roomLabel = (TextView) findViewById(R.id.roomLabelText);
        Spinner roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
        roomLabel.setVisibility(View.INVISIBLE);
        roomSpinner.setVisibility(View.INVISIBLE);


        if(i == 1){
            suggestAccuseLabel.setText("Make a suggestion...");
            suggestAccuseConfig = 3;
            roomLabel.setVisibility(View.VISIBLE);

        }else if (i ==2){
            suggestAccuseLabel.setText("Make an accusation...");
            suggestAccuseConfig = 4;
            roomSpinner.setVisibility(View.VISIBLE);
        }

    }

    private void setRoom(String roomName){
        TextView roomLabel = (TextView) findViewById(R.id.roomLabelText);
        roomLabel.setText(roomName);

    }
}
