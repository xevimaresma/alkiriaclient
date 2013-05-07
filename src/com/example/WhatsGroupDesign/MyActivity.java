package com.example.WhatsGroupDesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyActivity extends Activity implements View.OnClickListener {

    private Button b1,b2;
    private EditText e1,e2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b1 = (Button)findViewById(R.id.btnCreate);
        b2 = (Button)findViewById(R.id.btnAccount);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        e1 = (EditText)findViewById(R.id.campoUser);
        e1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Toast.makeText(getApplicationContext(), "Introduces your username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        e2 = (EditText)findViewById(R.id.campoPass);

        //Dependiendo de donde se situe el cursor aparece el tooltip

        e2.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Toast.makeText(getApplicationContext(), "Introduces your password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        try{
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
        }catch (Exception ex){
              ex.printStackTrace();
        }


    }
    /*public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }*/



}
