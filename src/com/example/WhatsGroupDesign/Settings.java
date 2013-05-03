package com.example.WhatsGroupDesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.WhatsGroupDesign.R.layout.settings;



public class Settings extends Activity implements View.OnClickListener {
    private Button btnCancel;
    //private Button btnCreate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(settings);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            Intent k = new Intent(Settings.this, MyActivity.class);
            startActivity(k);
        }
    }
}