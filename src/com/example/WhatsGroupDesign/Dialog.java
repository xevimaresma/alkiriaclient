package com.example.WhatsGroupDesign;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import static com.example.WhatsGroupDesign.R.layout.dialog;

public class Dialog extends Activity {

    private Button cancel;
    private Button create;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(dialog);

        //cancel = (Button)findViewById(R.id.btnCancel);

    }
}