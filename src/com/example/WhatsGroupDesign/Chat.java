package com.example.WhatsGroupDesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ScrollView;
import android.widget.Toast;

import static com.example.WhatsGroupDesign.R.layout.chat;

public class Chat extends Activity {

    private EditText message;
    private Button enviar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(chat);
        message = (EditText)findViewById(R.id.message);
        enviar = (Button)findViewById(R.id.sendMessage);
        //ScrollView sv = (ScrollView) findViewById(R.id.messageList);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == enviar){
                    String msg = message.getText().toString();
                    Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                    //Aquí tengo que empezar a añadir el contenido al scrollview
                }
            }
        });

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