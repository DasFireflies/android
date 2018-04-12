package com.jhu.fireflies.com.clue_less;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

public class InvestigationPadActivity extends AppCompatActivity {

    //save the first Button ID. Store calue in array[0...n] and convert between button and index by subtracting first button id
    int firstButtonID;
    HashMap<Integer, Integer> buttonValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.investigation_pad_layout);
        firstButtonID = R.id.suspectGreen1;

        buttonValues = new HashMap<Integer, Integer>();

    }

    public void onInvestigationButtonClicked(View view){
        int item = view.getId();
        Button button = (Button) findViewById(item);
        int index = item - firstButtonID;

        boolean containsKey = buttonValues.containsKey(index);
        if(containsKey){
            int value = buttonValues.get(index);
            if(value == 0){
                buttonValues.put(index, 1);
                button.setBackgroundColor(getResources().getColor(R.color.green));

            }else if(value == 1){
                buttonValues.put(index, 2);
                button.setBackgroundColor(getResources().getColor(R.color.red));

            }else{
                buttonValues.put(index, 0);
                button.setBackgroundResource(android.R.drawable.btn_default);
            }
        }
        else {
            buttonValues.put(index, 1);
            button.setBackgroundColor(getResources().getColor(R.color.green));
        }

    }

}
