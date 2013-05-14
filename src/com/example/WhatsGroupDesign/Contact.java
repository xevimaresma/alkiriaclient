package com.example.WhatsGroupDesign;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
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

	// Definim les variables de la classe
    private Button btnSalir;
    // El diàleg de càrrega de contactes
    ProgressDialog pdu;
    // I la llista de contactes predefinida, per les proves i la demo ens facilita trobar amb qui comunicar-nos 
    private String[] contactos = {"xevimaresma@gmail.com","prova","miquelserrabassa@gmail.com","xevi@cdmon.com","inane.ensemble@gmail.com","brossa@ramosiso.com"};
    
    // Mètode per localitzar els mails dels nostres contactes. Sol·licita el llistat, mira si tenen correu i si és així, el posa en un ArrayList que retorna
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
                    String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.e("Name :", name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", email);
                    if(email!=null){
                        names.add(email);
                    }                    
                }                 
                cur1.close();
            }
        }
        return names;
    }

    /* Com que la càrrega anterior pot ser molt lenta si tenim molts contactes,
     * vàrem optar per fer-la córrer en segon pla perquè sinó semblava que es bloquegés la UI.
     * D'aquesta manera, el processdialog es mostra i no queda tant parat -ni Android demana si
     * volem tancar l'app.
     */
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
        	// Rebem ja els contactes, així que assignem l'ArrayList a un adaptador, l'adaptador al ListView de contactes        	// 
        	setContentView(R.layout.contact);            
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Contact.this, android.R.layout.simple_list_item_1,nomsContactes);
            ListView lv = (ListView) findViewById(R.id.listaContactos);
            lv.setAdapter(adapter);
            lv.setClickable(false);
            lv.setFocusable(false);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            	@Override
            	// I si algú clica una opció, obrim l'activity Chat passant-li una variable "contacto" amb el mail del contacte.
            	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent n = new Intent(Contact.this, Chat.class);
                	n.putExtra("contacto",(String) parent.getItemAtPosition(position));
                	startActivity(n);
                	finish();
                }
            });
            pdu.dismiss();
            Contact.this.setProgressBarIndeterminateVisibility(false);
        }
    }
        

    public void onCreate(Bundle savedInstanceState) {
    	// A l'obrir, mostrarem el ListView amb els valors de demo.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        btnSalir = (Button)findViewById(R.id.salirContact);
        btnSalir.setOnClickListener(this);
        ListView lv = (ListView) findViewById(R.id.listaContactos);
        lv.setClickable(false);
        lv.setFocusable(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,contactos);                
        lv.setAdapter(adapter); 
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Intent n = new Intent(Contact.this, Chat.class);
            	n.putExtra("contacto",(String) parent.getItemAtPosition(position));
            	startActivity(n);
            	finish();
            }
        });        
    }

    @Override
    public void onClick(View v) {
        if(v == btnSalir){
        	// El botó de Logout carrega les SharedPreferences d'Alkiria i les esborra per després tornar
        	// a MyActivity, la pantalla de login.
        	SharedPreferences.Editor editor= getSharedPreferences("alkiria", MODE_PRIVATE).edit();
    		editor.clear();    		
    		editor.commit();
            Intent m = new Intent(Contact.this, MyActivity.class);
            Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
            startActivity(m);
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
            	// Aquest botó temporalment serveix per fer logout.
            	SharedPreferences.Editor editor= getSharedPreferences("alkiria", MODE_PRIVATE).edit();
        		editor.clear();    		
        		editor.commit();
                Intent m = new Intent(Contact.this, MyActivity.class);
                Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
                startActivity(m);
                finish();
             break;
            case R.id.updateContact:
                //Determinar la duraciÃ³n y la busqueda, pero este es el cÃ³digo a implementar
                pdu = ProgressDialog.show(Contact.this,"Updating contacts","Retrieving contact data from your phone. Please be patient.",true);
                //Determina si el usuario puede cancelar la operaciÃ³n
                pdu.setCancelable(false);
                // Càrrega de contactes del mòbil, de moment en "demo" amb diàleg de càrrega.
                carregaContactes tCarregaContactes = new carregaContactes();
                tCarregaContactes.execute();
        }
        return false;
    }


}