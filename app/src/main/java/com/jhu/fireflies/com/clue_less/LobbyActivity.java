package com.jhu.fireflies.com.clue_less;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LobbyActivity extends AppCompatActivity {
    private int characterIndex;
    private RadioGroup characterOption;
    private Button characterSelectButton;
    private boolean playerReady;
    private BackendHandler backendHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_layout);

        backendHandler = BackendHandlerReference.getBackendHandler();

        //Handle Messages from the Server
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String msgFromServer = (String) msg.getData().get("messageFromServer");
                Toast.makeText(LobbyActivity.this, msgFromServer, Toast.LENGTH_SHORT).show();


                int serverResponse;
                try{
                    serverResponse = Integer.parseInt(msgFromServer);
                }catch (NumberFormatException e){
                    //change this back to 0 for final. 1 is used for debug
                    serverResponse = 0;
                }
                if(serverResponse == 1){
                    toggleReadyButton();
                }else {
                    enableAllCharacterSelection();
                    disableCharacterSelect(characterIndex);
                    Toast.makeText(LobbyActivity.this, "Error: Character Already Taken", Toast.LENGTH_SHORT).show();
                }
            }
        };
        backendHandler.setLobbyHandler(handler);

        playerReady = false;

        characterSelectButton = (Button) findViewById(R.id.characterSelectButton);
        characterOption = (RadioGroup) findViewById(R.id.characterSelect);

        characterSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedButton = characterOption.getCheckedRadioButtonId();
                RadioButton selectedCharacter = (RadioButton) findViewById(selectedButton);

                characterIndex = characterOption.indexOfChild(selectedCharacter);

                //toggleReadyButton();

                Toast.makeText(getApplicationContext(), selectedCharacter.getText(), Toast.LENGTH_SHORT).show();
                /*selectedCharacter.setText("Selected Character");
                disableCharacterSelect();
                disbableAllCharacterSelection();*/

                backendHandler.sendMessage("" +characterIndex);
                disbableAllCharacterSelection();

            }
        });

    }

    private void disableCharacterSelect(int indexToEnable){
        RadioButton selectedCharacter = (RadioButton) characterOption.getChildAt(indexToEnable);

        selectedCharacter.setText("Ready!");
        selectedCharacter.setEnabled(false);

    }

    private void enableCharacterSelect(int indexToEnable){
        RadioButton selectedCharacter = (RadioButton) characterOption.getChildAt(indexToEnable);
        selectedCharacter.setEnabled(true);

        switch (indexToEnable){
            case 0:
                selectedCharacter.setText(R.string.green);
                break;
            case 1:
                selectedCharacter.setText(R.string.blue);
                break;
            case 2:
                selectedCharacter.setText(R.string.white);
                break;
            case 3:
                selectedCharacter.setText(R.string.red);
                break;
            case 4:
                selectedCharacter.setText(R.string.purple);
                break;
            case 5:
                selectedCharacter.setText(R.string.yellow);
                break;
        }

    }

    private void enableAllCharacterSelection(){
        for(int i = 0; i<6; i++){
            enableCharacterSelect(i);
        }
    }

    private void disbableAllCharacterSelection(){

        for(int i = 0; i < characterOption.getChildCount(); i++) {
            characterOption.getChildAt(i).setEnabled(false);
        }
    }

    private void toggleReadyButton(){

        if(!playerReady){
            playerReady = true;
            characterSelectButton.setText(R.string.characterSelectBack);

            RadioButton selectedCharacter = (RadioButton) characterOption.getChildAt(characterIndex);
            selectedCharacter.setText("Selected Character");
            disableCharacterSelect(characterIndex);
            disbableAllCharacterSelection();
            enableStartButton();
        }
        else{
            playerReady = false;
            characterSelectButton.setText(R.string.characterSelectReady);
            enableCharacterSelect(characterIndex);
            disableStartButton();

        }
    }

    private void enableStartButton(){
        Button startGame = (Button) findViewById(R.id.startGame);
        startGame.setVisibility(View.VISIBLE);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backendHandler.sendMessage("-1");
                Intent intent = new Intent(LobbyActivity.this,
                        StartOfGame.class);

                startActivity(intent);
            }
        });
    }

    private void disableStartButton(){
        Button startGame = (Button) findViewById(R.id.startGame);
        startGame.setVisibility(View.INVISIBLE);
    }

    public void testNetwork(View view){
        backendHandler.sendMessage("send from lobby");
       // BackendHandler backendHandler = BackendHandlerReference.getBackendHandler();
       // backendHandler.sendMessage("hello");
    }
}


