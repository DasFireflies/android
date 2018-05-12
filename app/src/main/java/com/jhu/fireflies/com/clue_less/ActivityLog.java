package com.jhu.fireflies.com.clue_less;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ActivityLog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);



    }

    public void refreshLog(View view){

        String serverLog = BackendHandlerReference.getServerLog();

        TextView log = (TextView) findViewById(R.id.serverLog);
        log.setText(serverLog);

    }
}
