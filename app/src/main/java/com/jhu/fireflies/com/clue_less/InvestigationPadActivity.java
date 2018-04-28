package com.jhu.fireflies.com.clue_less;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InvestigationPadActivity extends AppCompatActivity {

    //save the first Button ID. Store calue in array[0...n] and convert between button and index by subtracting first button id
    int firstButtonID;
    HashMap<String, Integer> buttonValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.investigation_pad_layout);
        firstButtonID = R.id.suspectGreen1;

        buttonValues = new HashMap<String, Integer>();

    }

    private void clearHash(){
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").commit();
            editor.commit();
        }
        buttonValues.clear();
    }

    private void debugHash(){
        Iterator iterator = buttonValues.entrySet().iterator();
        while(iterator.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) iterator.next();
            int value = buttonValues.get(pair.getKey());
            ;
            String keyWithText = (String) pair.getKey();
            Log.d("Debug Hash:", keyWithText.substring(3));
            int mapKey = (int) Integer.parseInt(keyWithText.substring(3));
        }
    }

    public void onDebugClicked(View view){
        debugHash();
        clearHash();
        saveMap(buttonValues);
    }

    public void onInvestigationButtonClicked(View view){
        int item = view.getId();
        Button button = (Button) findViewById(item);
        int index = item - firstButtonID;
        String mapKey = "key" + index;

        boolean containsKey = buttonValues.containsKey(mapKey);
        if(containsKey){
            int value = buttonValues.get(mapKey);
            if(value == 0){
                Log.d("Create Map", "Value saved to map: ");
                buttonValues.put(mapKey, 1);
                button.setBackgroundColor(getResources().getColor(R.color.green));

            }else if(value == 1){
                buttonValues.put(mapKey, 2);
                button.setBackgroundColor(getResources().getColor(R.color.red));

            }else{
                buttonValues.put(mapKey, 0);
                button.setBackgroundResource(android.R.drawable.btn_default);
            }
        }
        else {
            buttonValues.put(mapKey, 1);
            button.setBackgroundColor(getResources().getColor(R.color.green));
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        buttonValues = loadMap();
        reloadButtons();

        Log.d("onResume", Integer.toString(R.id.suspectGreen1));
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveMap(buttonValues);
    }

    private void reloadButtons(){
        Iterator iterator = buttonValues.entrySet().iterator();
        while(iterator.hasNext()){
            HashMap.Entry pair = (HashMap.Entry) iterator.next();
            Log.d("Reload Buttons", (String) pair.getKey());
            int value = buttonValues.get(pair.getKey());
            Log.d("Reload Buttons:", Integer.toString(value));
            String keyWithText = (String) pair.getKey();
            Log.d("Reload Buttons:", keyWithText);
            Log.d("Reload Buttons:", keyWithText.substring(3));
            int mapKey = (int) Integer.parseInt(keyWithText.substring(3));
            mapKey = mapKey + firstButtonID;
            Log.d("Reload Buttons:", Integer.toString(mapKey));
            Button button = (Button) findViewById(mapKey);

            if(button != null) {
                if (value == 1) {
                    button.setBackgroundColor(getResources().getColor(R.color.green));

                } else if (value == 2) {
                    button.setBackgroundColor(getResources().getColor(R.color.red));

                } else {
                    button.setBackgroundResource(android.R.drawable.btn_default);
                }
            }
        }
    }

    //from https://stackoverflow.com/questions/7944601/how-to-save-hashmap-to-shared-preferences
    private void saveMap(HashMap<String,Integer> inputMap){
        Log.d("saveMap", "saveMap start");
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").commit();
            editor.putString("My_map", jsonString);
            editor.commit();
        }
    }

    //from https://stackoverflow.com/questions/7944601/how-to-save-hashmap-to-shared-preferences
    private HashMap<String,Integer> loadMap(){
        HashMap<String,Integer> outputMap = new HashMap<String, Integer>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Integer value = (Integer) jsonObject.get(key);
                    outputMap.put(key, value);

                    Log.d("map loading", "key");

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
