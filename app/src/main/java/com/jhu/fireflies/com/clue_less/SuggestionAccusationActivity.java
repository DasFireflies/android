package com.jhu.fireflies.com.clue_less;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestionAccusationActivity extends AppCompatActivity {

    private int suggestAccuseConfig = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestion_accusation_layout);

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
                String suspectValue = suspectSpinner.getSelectedItem().toString();
                String weaponValue = weaponSpinner.getSelectedItem().toString();
                String test = "Suspect: " + suspectValue + "\nWeapon: " + weaponValue;
                Toast.makeText(SuggestionAccusationActivity.this, test, Toast.LENGTH_SHORT).show();
            }
        });

        //set room name
        setRoom("Kitchen");

        int suggestAccuse = getIntent().getExtras().getInt("suggestAccuse");
        setSuggestAccuse(suggestAccuse);
    }

    private void setSuggestAccuse(int i){
        TextView suggestAccuseLabel = (TextView) findViewById(R.id.suggestionAccusationText);

        if(i == 1){
            suggestAccuseLabel.setText("Make a suggestion...");
            suggestAccuseConfig = 1;
        }else if (i ==2){
            suggestAccuseLabel.setText("Make an accusation...");
            suggestAccuseConfig = 2;
        }

    }

    private void setRoom(String roomName){
        TextView roomLabel = (TextView) findViewById(R.id.roomLabelText);
        roomLabel.setText(roomName);
    }
}
