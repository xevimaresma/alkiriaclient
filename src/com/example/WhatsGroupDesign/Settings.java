package com.example.WhatsGroupDesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.WhatsGroupDesign.R.layout.settings;



public class Settings extends Activity implements View.OnClickListener {
    private Button btnCancel;
    private Button btnCreate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(settings);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnCreate = (Button)findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            Intent k = new Intent(Settings.this, MyActivity.class);
            startActivity(k);
        }
        if(v ==btnCreate){
            Intent l = new Intent(Settings.this,Contact.class);
            Toast.makeText(getApplicationContext(), "Account is created!", Toast.LENGTH_SHORT).show();
            startActivity(l);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.ap2:
            Intent i = new Intent(Settings.this, MyActivity.class);
            startActivity(i);
            break;
        }
        return false;
    }

}