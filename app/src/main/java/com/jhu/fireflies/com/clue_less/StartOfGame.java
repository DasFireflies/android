package com.jhu.fireflies.com.clue_less;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StartOfGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_of_game_layout);

        setAllPlayersInvisble();

        setPlayerText(1, 5);
        setPlayerText(2, 4);
        setPlayerText(3, 5);
        setPlayerText(4, 4);

        Button okButton = (Button) findViewById(R.id.startOfGameClose);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartOfGame.this,
                        GameBoard.class);

                startActivity(intent);
            }
        });
    }

    private void setAllPlayersInvisble(){

        TextView textView = (TextView) findViewById(R.id.player1Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player2Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player3Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player4Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player5Start);
        textView.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.player6Start);
        textView.setVisibility(View.INVISIBLE);

        return;
    }

    private void setPlayerText(int player, int numberOfCards){

        int id = 0;
        switch (player){
            case 1: id = R.id.player1Start; break;
            case 2: id = R.id.player2Start; break;
            case 3: id = R.id.player3Start; break;
            case 4: id = R.id.player4Start; break;
            case 5: id = R.id.player5Start; break;
            case 6: id = R.id.player6Start; break;
        }

        if(id <0 && id >6){
            return; //invalid id given
        }

        TextView textView = (TextView) findViewById(id);
        textView.setVisibility(View.VISIBLE);

        String message = "Player " + player + " receives " + numberOfCards + " cards.";
        textView.setText(message);
        return;
    }

}
