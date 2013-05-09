package com.example.WhatsGroupDesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import static com.example.WhatsGroupDesign.R.layout.dialog;

public class Chat extends Activity {

    private Button cancel;
    private Button create;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(dialog);

        //cancel = (Button)findViewById(R.id.btnCancel);

    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.undo:
                Intent i = new Intent(Chat.this, Contact.class);
                startActivity(i);
                break;
            case R.id.add:
                Toast.makeText(getApplicationContext(),"Updating your contacts", Toast.LENGTH_LONG).show();
                //finish();
        }
        return false;
    }
}