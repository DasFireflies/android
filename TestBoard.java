package com.jhu.fireflies.com.clue_less;

import android.os.Bundle;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.view.Gravity;

public class TestBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //Ask the backend what player this is
                //Look at all player icons and store in hashmap
            }
        });
    }

    // Room click action
    public void onRoomClick(View view){

        // Get the ID of the button being clicked
        int room = (Integer) view.getId();
        // Find player id

        //Call the backend

        // Instantiate the constraint layout
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.testboard);
        ConstraintSet constraint = new ConstraintSet();
        constraint.clone(constraintLayout);

        constraint.connect(R.id.lady_scarlet,ConstraintSet.RIGHT, room,ConstraintSet.RIGHT);
        constraint.connect(R.id.lady_scarlet,ConstraintSet.TOP, room,ConstraintSet.TOP);
        constraint.connect(R.id.lady_scarlet, ConstraintSet.BOTTOM, room, ConstraintSet.BOTTOM);
        constraint.applyTo(constraintLayout);

    }

    // Move the player
    public void move(int player, int room){

        // Instantiate the constraint layout
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.testboard);
        ConstraintSet constraint = new ConstraintSet();
        constraint.clone(constraintLayout);

        // Each player goes to a specific location in the room
        // Case case 0-6 to actual player IDs
        switch (player){
            // Baron Green
            case 1:
                constraint.connect(player,ConstraintSet.LEFT, room,ConstraintSet.LEFT);
                constraint.connect(player, ConstraintSet.TOP, room, ConstraintSet.TOP);
                constraint.applyTo(constraintLayout);
            // Lady Peacock
            case 2:
                constraint.connect(player,ConstraintSet.RIGHT, room,ConstraintSet.RIGHT);
                constraint.connect(player, ConstraintSet.TOP, room, ConstraintSet.TOP);
                constraint.applyTo(constraintLayout);
            // Madam White
            case 3:
                constraint.connect(player,ConstraintSet.LEFT, room,ConstraintSet.LEFT);
                constraint.connect(player, ConstraintSet.BOTTOM, room, ConstraintSet.BOTTOM);
                constraint.applyTo(constraintLayout);
            // Lady Scarlet
            case 4:
                constraint.connect(player,ConstraintSet.RIGHT, room,ConstraintSet.RIGHT);
                constraint.connect(player, ConstraintSet.BOTTOM, room, ConstraintSet.BOTTOM);
                constraint.applyTo(constraintLayout);
            // Dr Plum
            case 5:
                constraint.connect(player,ConstraintSet.RIGHT, room,ConstraintSet.RIGHT);
                constraint.connect(player,ConstraintSet.TOP, room,ConstraintSet.TOP);
                constraint.connect(player, ConstraintSet.BOTTOM, room, ConstraintSet.BOTTOM);
                constraint.applyTo(constraintLayout);
            // General Mustard
            case 6:
                constraint.connect(player,ConstraintSet.LEFT, room,ConstraintSet.LEFT);
                constraint.connect(player,ConstraintSet.TOP, room,ConstraintSet.TOP);
                constraint.connect(player, ConstraintSet.BOTTOM, room, ConstraintSet.BOTTOM);

                constraint.applyTo(constraintLayout);
        }
    }

    // Player notifications
    public void notification(String notification){
        Toast.makeText(TestBoard.this, notification, Toast.LENGTH_SHORT).show();
    }

    // Start a specific players turn
    public void startTurn(){
        Toast.makeText(TestBoard.this, "Your Turn!", Toast.LENGTH_LONG).show();
    }

    // Check whether the player has finished their turn
    public void endTurn(){

    }

    // Check whether the selected action is valid for that player
    public void verify(){
        //
    }

    //Diable all buttons for players that are not on their turn
}
