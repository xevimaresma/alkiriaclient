package com.example.WhatsGroupDesign;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class Contact extends Activity implements View.OnClickListener{

    private Button btnSalir;
    private Button btnEntrar;
    private String[]contactos = {"Rafa","Xevi","Miquel"};
    
    public ArrayList<String> getNameEmailDetails() {
        ArrayList<String> names = new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query( 
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", 
                                new String[]{id}, null); 
                while (cur1.moveToNext()) { 
                    //to get the contact names
                    String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.e("Name :", name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", email);
                    if(email!=null){
                        names.add(name);
                    }                    
                }                 
                cur1.close();
            }
        }
        return names;
    }
    
    protected class carregaContactes extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            Contact.this.setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
        	ArrayList<String> nomsContactes=getNameEmailDetails();
            return nomsContactes;
        }

        @Override
        protected void onPostExecute(ArrayList<String> nomsContactes) {
        	setContentView(R.layout.contact);            
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Contact.this, android.R.layout.simple_list_item_1,nomsContactes);
            ListView lv = (ListView) findViewById(R.id.listaContactos);
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
            Contact.this.setProgressBarIndeterminateVisibility(false);
        }
    }
        

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        btnSalir = (Button)findViewById(R.id.salirContact);
        btnSalir.setOnClickListener(this);
        btnEntrar = (Button)findViewById(R.id.chatear);
        btnEntrar.setOnClickListener(this);
        carregaContactes tCarregaContactes = new carregaContactes();
        tCarregaContactes.execute();
        /*ListView lv = (ListView) findViewById(R.id.listaContactos);
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
        });*/
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
            Intent n = new Intent(Contact.this, Chat.class);
            Toast.makeText(getApplicationContext(),"Enjoy!!!", Toast.LENGTH_LONG).show();
            startActivity(n);
            finish();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.searchContact:
                Intent i = new Intent(Contact.this, Chat.class);
                startActivity(i);
                break;
            case R.id.updateContact:
                Toast.makeText(getApplicationContext(),"Updating!!", Toast.LENGTH_SHORT).show();
                showContacts();
        }
        return false;
    }

    public void showContacts() {
        Intent i = new Intent();
        i.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
        i.setAction("android.intent.action.MAIN");
        i.addCategory("android.intent.category.LAUNCHER");
        i.addCategory("android.intent.category.DEFAULT");
        startActivity(i);
    }



}