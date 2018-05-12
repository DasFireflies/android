package com.jhu.fireflies.com.clue_less;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TextBased extends AppCompatActivity {

    private  BackendHandler backendHandler;

    private String responses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_based);

        backendHandler = BackendHandlerReference.getBackendHandler();

        responses = BackendHandlerReference.getServerLog();
        TextView responseHistory = (TextView) findViewById(R.id.serverResponsesHistory);
        responseHistory.setText(responses);

    }

    public void sendToServer(View view){

        EditText commandLine = (EditText) findViewById(R.id.commandEntry);
        String command = commandLine.getText().toString();
        //responses = "Me: " + command + "\n" + responses;
        BackendHandlerReference.addToServerLog(command, "me");

        TextView responseHistory = (TextView) findViewById(R.id.serverResponsesHistory);
        responseHistory.setText(BackendHandlerReference.getServerLog());
    }

}
