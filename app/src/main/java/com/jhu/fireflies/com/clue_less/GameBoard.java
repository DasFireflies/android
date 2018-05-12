package com.jhu.fireflies.com.clue_less;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telecom.Call;
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

import java.util.HashMap;

public class GameBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // TODO store character information
    private String characterSelected = "Lady Peacock";
    private String characterPosition = "Study";
    HashMap<String, Integer> players = new HashMap<>();
    HashMap<String, Integer> rooms = new HashMap<>();
    HashMap<String, String> halls = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board_layout);


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
        rooms.put("lounge", R.id.lounge);
        rooms.put("Library", R.id.library);
        rooms.put("Billiard Room", R.id.billiard_room);
        rooms.put("Dining Room", R.id.dining_room);
        rooms.put("Conservatory", R.id.conservatory);
        rooms.put("Ballroom", R.id.ballrom);
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
            i.putExtra("suggestAccuse", 2);
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
        Button ballroom = (Button) findViewById(R.id.ballrom);
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

        switch (characterPosition){
            case "Study":
                lounge.setEnabled(true);
                conservatory.setEnabled(true);
                if (halls.get("Hall 1").equals("None")) {
                    hall1.setEnabled(true);
                }
                if (halls.get("Hall 3").endsWith("None")) {
                    hall3.setEnabled(true);
                }
                break;
            case "Great Hall":
                ballroom.setEnabled(true);
                if (halls.get("Hall 1").equals("None")) {
                    hall1.setEnabled(true);
                }
                if (halls.get("Hall 2").equals("None")) {
                    hall2.setEnabled(true);
                }
                break;
            case "Lounge":
                study.setEnabled(true);
                kitchen.setEnabled(true);
                if (halls.get("Hall 2").equals("None")) {
                    hall2.setEnabled(true);
                }
                if (halls.get("Hall 5").equals("None")) {
                    hall5.setEnabled(true);
                }
                break;
            case "Library":
                dining_room.setEnabled(true);
                if (halls.get("Hall 3").equals("None")) {
                    hall3.setEnabled(true);
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
                library.setEnabled(true);
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
                study.setEnabled(true);
                kitchen.setEnabled(true);
                if (halls.get("Hall 8").equals("None")) {
                    hall8.setEnabled(true);
                }
                if (halls.get("Hall 11").equals("None")) {
                    hall11.setEnabled(true);
                }
                break;
            case "Ballroon":
                great_hall.setEnabled(true);
                if (halls.get("Hall 11").equals("None")) {
                    hall11.setEnabled(true);
                }
                if (halls.get("Hall 12").equals("None")) {
                    hall12.setEnabled(true);
                }
                break;
            case "Kitchen":
                lounge.setEnabled(true);
                conservatory.setEnabled(true);
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
        Button ballroom = (Button) findViewById(R.id.ballrom);
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

        switch (characterPosition){
            case "Study":
                lounge.setEnabled(false);
                conservatory.setEnabled(false);
                hall1.setEnabled(false);
                hall3.setEnabled(false);
                break;
            case "Great Hall":
                ballroom.setEnabled(false);
                hall1.setEnabled(false);
                hall2.setEnabled(false);
                break;
            case "Lounge":
                study.setEnabled(false);
                kitchen.setEnabled(false);
                hall2.setEnabled(false);
                hall5.setEnabled(false);
                break;
            case "Library":
                dining_room.setEnabled(false);
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
                library.setEnabled(false);
                hall5.setEnabled(false);
                hall7.setEnabled(false);
                hall10.setEnabled(false);
                break;
            case "Conservatory":
                study.setEnabled(false);
                kitchen.setEnabled(false);
                hall8.setEnabled(false);
                hall11.setEnabled(false);
                break;
            case "Ballroon":
                great_hall.setEnabled(false);
                hall11.setEnabled(false);
                hall12.setEnabled(false);
                break;
            case "Kitchen":
                lounge.setEnabled(false);
                conservatory.setEnabled(false);
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
    }

    // Room click action
    public void onRoomClick(View view){

        String room = "";

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

        // Disables the room buttons
        disable(characterPosition);

        // Move the player to the selected room
        move(characterSelected, room);

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
    }

    // End the players current turn
    private void enableFinishButton(String player, String room){
        Button finish = (Button) findViewById(R.id.finish_button);
        finish.setVisibility(View.VISIBLE);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GameBoard.this, "Calling the server", Toast.LENGTH_SHORT).show();
                // TODO Call the backend, player is finished
                // TODO Send the backend who moved and where
            }
        });
    }

    // Player notifications on what is occurring in the game
    public void notification(String notification){
        Toast.makeText(GameBoard.this, notification, Toast.LENGTH_SHORT).show();
    }
}
