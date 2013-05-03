package com.example.WhatsGroupDesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyActivity extends Activity implements View.OnClickListener {

    private Button b1,b2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b1 = (Button)findViewById(R.id.btnCreate);
        b2 = (Button)findViewById(R.id.btnAccount);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == b1){
            Intent i = new Intent(MyActivity.this, Dialog.class);
            startActivity(i);
            finish();
        }
        else if(v == b2){
            Intent j = new Intent(MyActivity.this, Settings.class);
            startActivity(j);
            finish();
        }

    }
}