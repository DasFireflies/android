package com.jhu.fireflies.com.clue_less;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InitialClues extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_clues);

        setInitialCLues("Clue 1\n Clue 2\n Clue 3");
    }

    public void closeInitialClues(View view){
        Intent intent = new Intent(InitialClues.this,
                GameBoard.class);

        startActivity(intent);

    }

    public void setInitialCLues(String clues){
        TextView initialClues = (TextView) findViewById(R.id.initalClueCards);

        initialClues.setText(clues);
    }
}
