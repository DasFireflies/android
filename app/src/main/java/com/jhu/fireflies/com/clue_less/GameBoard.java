package com.jhu.fireflies.com.clue_less;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GameBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // TODO store character information
    private BackendHandler backendHandler;
    private  SharedPreferences sharedPreferences;
    private String characterSelected = "";
    private String characterPosition = "";
    HashMap<String, Integer> players = new HashMap<>();
    HashMap<String, Integer> rooms = new HashMap<>();
    HashMap<String, String> halls = new HashMap<>();
    HashMap<String, String> positions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board_layout);

        //Get reference to backend
        backendHandler = BackendHandlerReference.getBackendHandler();

        //handle responses from backend
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //code to handle messages from server
                String msgFromServer = (String) msg.getData().get("messageFromServer");
                List<String> messageList = Arrays.asList(msgFromServer.split(","));

                //return if message isn't for this acitivity
                //check first index of messageList. If the number isn't relevant to this class, return.
                if(messageList.get(0).compareTo("5") != 0 && messageList.get(0).compareTo("3") != 0 && messageList.get(0).compareTo("2") != 0 && messageList.get(0).compareTo("22") != 0){return;}

                if (messageList.get(0).compareTo("3") == 0) {
                    String message = messageList.get(1)+" suggested that "+messageList.get(2)+" did it in the "+messageList.get(3)+" with the "+messageList.get(4)+". ";
                    if (messageList.size()<6) {
                        message = message + "No one was able to disprove the suggestion";
                    }
                    else {
                        message = message + messageList.get(5) + " disproved the suggestion";
                    }
                    BackendHandlerReference.addToServerLog(message);
                }else if (messageList.get(0).compareTo("5") == 0) {
                    BackendHandlerReference.addToServerLog("It is your move...");
                }
                else if (messageList.get(0).compareTo("2") == 0) {
                    //character moved location
                    BackendHandlerReference.addToServerLog(messageList.get(1)+" moved into "+messageList.get(2));
                }
                else if (messageList.get(0).compareTo("22") == 0) {
                    //character moved location
                    BackendHandlerReference.addToServerLog(messageList.get(1)+" was moved into "+messageList.get(2)+" by a suggestion");
                }

                if(messageList.get(0).compareTo("2") == 0 || messageList.get(0).compareTo("22") == 0) {
                    String character = messageList.get(1);
                    String room = messageList.get(2);

                    //move character into room
                    move(character, room);
                    //the only difference between 2 and 22 is that 2 means the player moved
                    //their own piece their. 22 means their piece was moved by someone
                    //making a suggestion. this should only affect what is written in the
                    //activity log
                }

                if(messageList.get(0).compareTo("5") == 0) {
                    //this means it is your turn and the server is waiting for a move
                    //call function to activate buttons

                    String you_were_just_moved_by_a_suggestion = messageList.get(1);
                    //this is "0" or "1" for False/True. If this value is "1" then
                    //you can begin your move by making a suggestion. Otherwise you
                    //must begin your move by moving or making an accusation
                    characterPosition = messageList.get(2);
                    startTurn();

                    //im not sure what to do with the above pieces 2 of info ^^^
                }

                //log messages
                //BackendHandlerReference.addToServerLog("Server", "Message");

            }
        };

        backendHandler.setGameboardHandler(handler);

        //Get Character Index
        sharedPreferences = getApplicationContext().getSharedPreferences("CharacterDetails", Context.MODE_PRIVATE);
        int characterIndex = sharedPreferences.getInt("characterIndex", 0);
        switch (characterIndex){
            case 0: characterSelected = "Baron Green"; break;
            case 1: characterSelected = "Lady Peacock"; break;
            case 2: characterSelected = "Madam White"; break;
            case 3: characterSelected = "Lady Scarlet"; break;
            case 4: characterSelected = "Dr. Plum"; break;
            case 5: characterSelected = "General Mustard"; break;
        }

        //Read from Shared Preferences
        characterPosition = sharedPreferences.getString("characterRoom", "Conservatory");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Instantiate the constraint layout
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.contentBoard);
        ConstraintSet constraint = new ConstraintSet();
        constraint.clone(constraintLayout);

        // Store all player information in a Hash Map
        players.put("Baron Green", R.id.baron_green);
        players.put("Lady Peacock", R.id.lady_peacock);
        players.put("Madam White", R.id.madam_white);
        players.put("Lady Scarlet", R.id.lady_scarlet);
        players.put("Dr. Plum", R.id.dr_plum);
        players.put("General Mustard", R.id.general_mustard);

        // Store all room information
        rooms.put("Study", R.id.study);
        rooms.put("Great Hall", R.id.great_hall);
        rooms.put("Lounge", R.id.lounge);
        rooms.put("Library", R.id.library);
        rooms.put("Billiard Room", R.id.billiard_room);
        rooms.put("Dining Room", R.id.dining_room);
        rooms.put("Conservatory", R.id.conservatory);
        rooms.put("Ballroom", R.id.ballroom);
        rooms.put("Kitchen", R.id.kitchen);
        rooms.put("Hall 1", R.id.hall1);
        rooms.put("Hall 2", R.id.hall2);
        rooms.put("Hall 3", R.id.hall3);
        rooms.put("Hall 4", R.id.hall4);
        rooms.put("Hall 5", R.id.hall5);
        rooms.put("Hall 6", R.id.hall6);
        rooms.put("Hall 7", R.id.hall7);
        rooms.put("Hall 8", R.id.hall8);
        rooms.put("Hall 9", R.id.hall9);
        rooms.put("Hall 10", R.id.hall10);
        rooms.put("Hall 11", R.id.hall11);
        rooms.put("Hall 12", R.id.hall12);

        // Store if a player is occupying a hallway
        halls.put("Hall 1", "None");
        halls.put("Hall 2", "None");
        halls.put("Hall 3", "None");
        halls.put("Hall 4", "None");
        halls.put("Hall 5", "None");
        halls.put("Hall 6", "None");
        halls.put("Hall 7", "None");
        halls.put("Hall 8", "None");
        halls.put("Hall 9", "None");
        halls.put("Hall 10", "None");
        halls.put("Hall 11", "None");
        halls.put("Hall 12", "None");

        this.onResume();

        positions = loadMap();

        // Move the players into their last position
        for (HashMap.Entry<String, String> positions : positions.entrySet()) {
            Log.d("GameBoard", "key: "+ positions.getKey() + "\nvalue: "+ positions.getValue());
            move(positions.getKey(), positions.getValue());
        }
    }

    // Saves the state of the positions Hash Map
    @Override
    protected void onPause() {
        super.onPause();
        saveMap(positions);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_investigationPad) {

            Intent i = (Intent) new Intent(GameBoard.this,
                    InvestigationPadActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_startOfGame) {

            Intent i = (Intent) new Intent(GameBoard.this,
                    StartOfGame.class);

            startActivity(i);
        } else if (id == R.id.nav_suggestion) {
            Intent i = (Intent) new Intent(GameBoard.this,
                    SuggestionAccusationActivity.class);
            i.putExtra("suggestAccuse", 1);
            startActivity(i);
        } else if (id == R.id.nav_acuse) {
            Intent i = (Intent) new Intent(GameBoard.this,
                    SuggestionAccusationActivity.class);
            i.putExtra("suggestAccuse", 2);
            startActivity(i);
        }else if (id == R.id.nav_initialClues) {
            Intent i = (Intent) new Intent(GameBoard.this,
                    InitialClues.class);
            startActivity(i);
        }else if (id == R.id.nav_activityLog) {
            Intent i = (Intent) new Intent(GameBoard.this,
                    ActivityLog.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Call method on specific players turn
    public void startTurn(){
        Toast.makeText(GameBoard.this, "Your Turn!", Toast.LENGTH_LONG).show();
        enable(characterPosition);
    }

    // Enable rooms to click based on player location
    private void enable(String characterPosition){

        Button study = (Button) findViewById(R.id.study);
        Button great_hall = (Button) findViewById(R.id.great_hall);
        Button lounge = (Button) findViewById(R.id.lounge);
        Button library = (Button) findViewById(R.id.library);
        Button billiard_room = (Button) findViewById(R.id.billiard_room);
        Button dining_room = (Button) findViewById(R.id.dining_room);
        Button conservatory = (Button) findViewById(R.id.conservatory);
        Button ballroom = (Button) findViewById(R.id.ballroom);
        Button kitchen = (Button) findViewById(R.id.kitchen);
        Button hall1 = (Button) findViewById(R.id.hall1);
        Button hall2 = (Button) findViewById(R.id.hall2);
        Button hall3 = (Button) findViewById(R.id.hall3);
        Button hall4 = (Button) findViewById(R.id.hall4);
        Button hall5 = (Button) findViewById(R.id.hall5);
        Button hall6 = (Button) findViewById(R.id.hall6);
        Button hall7 = (Button) findViewById(R.id.hall7);
        Button hall8 = (Button) findViewById(R.id.hall8);
        Button hall9 = (Button) findViewById(R.id.hall9);
        Button hall10 = (Button) findViewById(R.id.hall10);
        Button hall11 = (Button) findViewById(R.id.hall11);
        Button hall12 = (Button) findViewById(R.id.hall12);
        //View slideOutContainer = findViewById(R.id.nav_view);
        //Button suggestion = (Button) slideOutContainer.findViewById(R.id.nav_suggestion);
        //Button accusation = (Button) slideOutContainer.findViewById(R.id.nav_acuse);


        switch (characterPosition){
            case "Study":
                kitchen.setEnabled(true);
                if (halls.get("Hall 1").equals("None")) {
                    hall1.setEnabled(true);
                }
                if (halls.get("Hall 3").endsWith("None")) {
                    hall3.setEnabled(true);
                }
                break;
            case "Great Hall":
                if (halls.get("Hall 1").equals("None")) {
                    hall1.setEnabled(true);
                }
                if (halls.get("Hall 2").equals("None")) {
                    hall2.setEnabled(true);
                }
                if (halls.get("Hall 4").equals("None")){
                    hall4.setEnabled(true);
                }
                break;
            case "Lounge":
                conservatory.setEnabled(true);
                if (halls.get("Hall 2").equals("None")) {
                    hall2.setEnabled(true);
                }
                if (halls.get("Hall 5").equals("None")) {
                    hall5.setEnabled(true);
                }
                break;
            case "Library":
                if (halls.get("Hall 3").equals("None")) {
                    hall3.setEnabled(true);
                }
                if (halls.get("Hall 6").equals("None")) {
                    hall6.setEnabled(true);
                }
                if (halls.get("Hall 8").equals("None")) {
                    hall8.setEnabled(true);
                }
                break;
            case "Billiard Room":
                if (halls.get("Hall 4").equals("None")) {
                    hall4.setEnabled(true);
                }
                if (halls.get("Hall 6").equals("None")) {
                    hall6.setEnabled(true);
                }
                if (halls.get("Hall 7").equals("None")) {
                    hall7.setEnabled(true);
                }
                if (halls.get("Hall 9").equals("None")) {
                    hall9.setEnabled(true);
                }
                break;
            case "Dining Room":
                if (halls.get("Hall 5").equals("None")) {
                    hall5.setEnabled(true);
                }
                if (halls.get("Hall 7").equals("None")) {
                    hall7.setEnabled(true);
                }
                if (halls.get("Hall 10").equals("None")) {
                    hall10.setEnabled(true);
                }
                break;
            case "Conservatory":
                lounge.setEnabled(true);
                if (halls.get("Hall 8").equals("None")) {
                    hall8.setEnabled(true);
                }
                if (halls.get("Hall 11").equals("None")) {
                    hall11.setEnabled(true);
                }
                break;
            case "Ballroom":
                if (halls.get("Hall 9").equals("None")) {
                    hall9.setEnabled(true);
                }
                if (halls.get("Hall 11").equals("None")) {
                    hall11.setEnabled(true);
                }
                if (halls.get("Hall 12").equals("None")) {
                    hall12.setEnabled(true);
                }
                break;
            case "Kitchen":
                study.setEnabled(true);
                if (halls.get("Hall 10").equals("None")) {
                    hall10.setEnabled(true);
                }
                if (halls.get("Hall 12").equals("None")) {
                    hall12.setEnabled(true);
                }
                break;
            case "Hall 1":
                study.setEnabled(true);
                great_hall.setEnabled(true);
                break;
            case "Hall 2":
                great_hall.setEnabled(true);
                lounge.setEnabled(true);
                break;
            case "Hall 3":
                study.setEnabled(true);
                library.setEnabled(true);
                break;
            case "Hall 4":
                great_hall.setEnabled(true);
                billiard_room.setEnabled(true);
                break;
            case "Hall 5":
                lounge.setEnabled(true);
                dining_room.setEnabled(true);
                break;
            case "Hall 6":
                library.setEnabled(true);
                billiard_room.setEnabled(true);
                break;
            case "Hall 7":
                billiard_room.setEnabled(true);
                dining_room.setEnabled(true);
                break;
            case "Hall 8":
                library.setEnabled(true);
                conservatory.setEnabled(true);
                break;
            case "Hall 9":
                billiard_room.setEnabled(true);
                ballroom.setEnabled(true);
                break;
            case "Hall 10":
                dining_room.setEnabled(true);
                kitchen.setEnabled(true);
                break;
            case "Hall 11":
                conservatory.setEnabled(true);
                ballroom.setEnabled(true);
                break;
            case "Hall 12":
                ballroom.setEnabled(true);
                kitchen.setEnabled(true);
                break;
        }

        //accusation.setEnabled(true);
        //suggestion.setEnabled(true);
    }

    // Disable the buttons when the turn is finished
    private void disable(String characterPosition){

        Button study = (Button) findViewById(R.id.study);
        Button great_hall = (Button) findViewById(R.id.great_hall);
        Button lounge = (Button) findViewById(R.id.lounge);
        Button library = (Button) findViewById(R.id.library);
        Button billiard_room = (Button) findViewById(R.id.billiard_room);
        Button dining_room = (Button) findViewById(R.id.dining_room);
        Button conservatory = (Button) findViewById(R.id.conservatory);
        Button ballroom = (Button) findViewById(R.id.ballroom);
        Button kitchen = (Button) findViewById(R.id.kitchen);
        Button hall1 = (Button) findViewById(R.id.hall1);
        Button hall2 = (Button) findViewById(R.id.hall2);
        Button hall3 = (Button) findViewById(R.id.hall3);
        Button hall4 = (Button) findViewById(R.id.hall4);
        Button hall5 = (Button) findViewById(R.id.hall5);
        Button hall6 = (Button) findViewById(R.id.hall6);
        Button hall7 = (Button) findViewById(R.id.hall7);
        Button hall8 = (Button) findViewById(R.id.hall8);
        Button hall9 = (Button) findViewById(R.id.hall9);
        Button hall10 = (Button) findViewById(R.id.hall10);
        Button hall11 = (Button) findViewById(R.id.hall11);
        Button hall12 = (Button) findViewById(R.id.hall12);
        //View slideOutContainer = findViewById(R.id.nav_view);
        //Button suggestion = (Button) slideOutContainer.findViewById(R.id.nav_suggestion);
        //Button accusation = (Button) slideOutContainer.findViewById(R.id.nav_acuse);

        switch (characterPosition){
            case "Study":
                kitchen.setEnabled(false);
                hall1.setEnabled(false);
                hall3.setEnabled(false);
                break;
            case "Great Hall":
                hall1.setEnabled(false);
                hall2.setEnabled(false);
                break;
            case "Lounge":
                conservatory.setEnabled(false);
                hall2.setEnabled(false);
                hall5.setEnabled(false);
                break;
            case "Library":
                hall3.setEnabled(false);
                hall8.setEnabled(false);
                break;
            case "Billiard Room":
                hall4.setEnabled(false);
                hall6.setEnabled(false);
                hall7.setEnabled(false);
                hall9.setEnabled(false);
                break;
            case "Dining Room":
                hall5.setEnabled(false);
                hall7.setEnabled(false);
                hall10.setEnabled(false);
                break;
            case "Conservatory":
                lounge.setEnabled(false);
                hall8.setEnabled(false);
                hall11.setEnabled(false);
                break;
            case "Ballroom":
                hall9.setEnabled(false);
                hall11.setEnabled(false);
                hall12.setEnabled(false);
                break;
            case "Kitchen":
                study.setEnabled(false);
                hall10.setEnabled(false);
                hall12.setEnabled(false);
                break;
            case "Hall 1":
                study.setEnabled(false);
                great_hall.setEnabled(false);
                break;
            case "Hall 2":
                great_hall.setEnabled(false);
                lounge.setEnabled(false);
                break;
            case "Hall 3":
                study.setEnabled(false);
                library.setEnabled(false);
                break;
            case "Hall 4":
                great_hall.setEnabled(false);
                billiard_room.setEnabled(false);
                break;
            case "Hall 5":
                lounge.setEnabled(false);
                dining_room.setEnabled(false);
                break;
            case "Hall 6":
                library.setEnabled(false);
                billiard_room.setEnabled(false);
                break;
            case "Hall 7":
                billiard_room.setEnabled(false);
                dining_room.setEnabled(false);
                break;
            case "Hall 8":
                library.setEnabled(false);
                conservatory.setEnabled(false);
                break;
            case "Hall 9":
                billiard_room.setEnabled(false);
                ballroom.setEnabled(false);
                break;
            case "Hall 10":
                dining_room.setEnabled(false);
                kitchen.setEnabled(false);
                break;
            case "Hall 11":
                conservatory.setEnabled(false);
                ballroom.setEnabled(false);
                break;
            case "Hall 12":
                ballroom.setEnabled(false);
                kitchen.setEnabled(false);
                break;
        }

        //accusation.setEnabled(false);
        //suggestion.setEnabled(false);
    }

    // Room click action
    public void onRoomClick(View view){

        String room = "";
        String message ="";

        // Get ID of the button being clicked
        for (HashMap.Entry<String, Integer> rooms : rooms.entrySet()) {
            if (rooms.getValue()==view.getId()){
                room = rooms.getKey();
                break;
            }
            else{
                continue;
            }
        }

        //save room
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("characterRoom", room);
        editor.commit();

        // Disables the room buttons
        disable(characterPosition);

        // Move the player to the selected room
        move(characterSelected, room);

        // Send the message to the server
        message = "2" + "," + room;
        //send to server
        backendHandler.sendMessage(message);
        //log messages
        //BackendHandlerReference.addToServerLog("", message);

        // Enable finish button
        enableFinishButton(characterSelected, room);

        // Update characterPosition variable
        characterPosition = room;

    }

    // Move the player
    public void move(String player, String room){

        // Instantiate the constraint layout
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.contentBoard);
        ConstraintSet constraint = new ConstraintSet();
        constraint.clone(constraintLayout);

        // Each player goes to a specific location in the room
        switch (player){
            // Baron Green
            case "Baron Green":
                constraint.connect(players.get(player),ConstraintSet.LEFT, rooms.get(room),ConstraintSet.LEFT);
                constraint.connect(players.get(player), ConstraintSet.TOP, rooms.get(room), ConstraintSet.TOP);
                constraint.applyTo(constraintLayout);
                break;
            // Lady Peacock
            case "Lady Peacock":
                constraint.connect(players.get(player),ConstraintSet.RIGHT, rooms.get(room),ConstraintSet.RIGHT);
                constraint.connect(players.get(player), ConstraintSet.TOP, rooms.get(room), ConstraintSet.TOP);
                constraint.applyTo(constraintLayout);
                break;
            // Madam White
            case "Madam White":
                constraint.connect(players.get(player),ConstraintSet.LEFT, rooms.get(room),ConstraintSet.LEFT);
                constraint.connect(players.get(player), ConstraintSet.BOTTOM, rooms.get(room), ConstraintSet.BOTTOM);
                constraint.applyTo(constraintLayout);
                break;
            // Lady Scarlet
            case "Lady Scarlet":
                constraint.connect(players.get(player),ConstraintSet.RIGHT, rooms.get(room),ConstraintSet.RIGHT);
                constraint.connect(players.get(player), ConstraintSet.BOTTOM, rooms.get(room), ConstraintSet.BOTTOM);
                constraint.applyTo(constraintLayout);
                break;
            // Dr Plum
            case "Dr. Plum":
                constraint.connect(players.get(player),ConstraintSet.RIGHT, rooms.get(room),ConstraintSet.RIGHT);
                constraint.connect(players.get(player),ConstraintSet.TOP, rooms.get(room),ConstraintSet.TOP);
                constraint.connect(players.get(player), ConstraintSet.BOTTOM, rooms.get(room), ConstraintSet.BOTTOM);
                constraint.applyTo(constraintLayout);
                break;
            // General Mustard
            case "General Mustard":
                constraint.connect(players.get(player),ConstraintSet.LEFT, rooms.get(room),ConstraintSet.LEFT);
                constraint.connect(players.get(player),ConstraintSet.TOP, rooms.get(room),ConstraintSet.TOP);
                constraint.connect(players.get(player), ConstraintSet.BOTTOM, rooms.get(room), ConstraintSet.BOTTOM);
                constraint.applyTo(constraintLayout);
                break;
        }

        // Check if the player was in a hallway
        for (HashMap.Entry<String, String> halls : halls.entrySet()) {
            if (halls.getValue().equals(player)){
                halls.setValue("None");
                break;
            }
        }

        // Update the hash map if the player moved to a hallway
        for (HashMap.Entry<String, String> halls : halls.entrySet()) {
            if (halls.getKey().equals(room)){
                halls.setValue(player);
                break;
            }
        }
        positions.put(player,room);
        characterPosition = room;
        Log.d("GameBoard", "key: "+ player + "\nvalue: "+ room);
        Log.d("GameBoard", "CharacterPosition: " +characterPosition);
    }

    // End the players current turn
    private void enableFinishButton(String player, String room){
        final Button finish = (Button) findViewById(R.id.finish_button);
        finish.setVisibility(View.VISIBLE);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish.setVisibility(View.INVISIBLE);
                backendHandler.sendMessage("-1");
            }
        });
    }

    // Player notifications on what is occurring in the game
    public void notification(String notification){
        Toast.makeText(GameBoard.this, notification, Toast.LENGTH_SHORT).show();
    }

    private void saveMap(HashMap<String,String> inputMap){
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("Positions").commit();
            editor.putString("Positions", jsonString);
            editor.commit();
        }
    }

    private HashMap<String,String> loadMap(){
        HashMap<String,String> outputMap = new HashMap<String,String>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("Positions", (new JSONObject()).toString());
                Log.d("GameBoard", jsonString);
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
