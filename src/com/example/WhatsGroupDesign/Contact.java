package com.example.WhatsGroupDesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Contact extends Activity implements View.OnClickListener{

    private Button btnSalir;
    private Button btnEntrar;
    private String[]contactos = {"Rafa","Xevi","Miquel"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        btnSalir = (Button)findViewById(R.id.salirContact);
        btnSalir.setOnClickListener(this);
        btnEntrar = (Button)findViewById(R.id.chatear);
        btnEntrar.setOnClickListener(this);
        ListView lv = (ListView) findViewById(R.id.listaContactos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,contactos);
        lv.setAdapter(adapter);
        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Ha pulsado el item " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == btnSalir){
            Intent m = new Intent(Contact.this, MyActivity.class);
            Toast.makeText(getApplicationContext(), "See you later!!", Toast.LENGTH_SHORT).show();
            startActivity(m);
            finish();
        }
        else if (v == btnEntrar){
            Intent n = new Intent(Contact.this, Dialog.class);
            Toast.makeText(getApplicationContext(),"Enjoy!!!", Toast.LENGTH_LONG).show();
            startActivity(n);
            finish();
        }
    }


}