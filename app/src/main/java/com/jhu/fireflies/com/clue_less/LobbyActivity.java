package com.jhu.fireflies.com.clue_less;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LobbyActivity extends AppCompatActivity {
    private int characterIndex;
    private RadioGroup characterOption;
    private Button characterSelectButton;
    private boolean playerReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_layout);

        playerReady = false;

        characterSelectButton = (Button) findViewById(R.id.characterSelectButton);
        characterOption = (RadioGroup) findViewById(R.id.characterSelect);

        characterSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedButton = characterOption.getCheckedRadioButtonId();
                RadioButton selectedCharacter = (RadioButton) findViewById(selectedButton);

                characterIndex = characterOption.indexOfChild(selectedCharacter);

                toggleReadyButton();

                /*selectedCharacter.setText("Selected Character");
                disableCharacterSelect();
                disbableAllCharacterSelection();*/

            }
        });

    }

    private void disableCharacterSelect(){
        RadioButton selectedCharacter = (RadioButton) characterOption.getChildAt(characterIndex);

        selectedCharacter.setText("Ready!");
        selectedCharacter.setEnabled(false);

    }

    private void enableCharacterSelect(int indexToEnable){
        RadioButton selectedCharacter = (RadioButton) characterOption.getChildAt(characterIndex);
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
            disableCharacterSelect();
            disbableAllCharacterSelection();
            enableStartButton();
        }
        else{
            playerReady = false;
            characterSelectButton.setText(R.string.characterSelectReady);
            enableCharacterSelect(characterIndex);

        }
    }

    private void enableStartButton(){
        Button startGame = (Button) findViewById(R.id.startGame);
        startGame.setVisibility(View.VISIBLE);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyActivity.this,
                        StartOfGame.class);

                startActivity(intent);
            }
        });
    }

    private void hideStartButton(){
        Button startGame = (Button) findViewById(R.id.startGame);
        startGame.setVisibility(View.INVISIBLE);
    }
}


