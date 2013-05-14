package com.example.WhatsGroupDesign;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.whatsgroup.alkiria.entities.MsgUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyActivity extends Activity implements View.OnClickListener {

    private Button b1,b2;
    protected EditText e1,e2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        b1 = (Button)findViewById(R.id.btnCreate);
        b2 = (Button)findViewById(R.id.btnAccount);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        e1 = (EditText)findViewById(R.id.campoMail);        
        e2 = (EditText)findViewById(R.id.campoPass);
        
        SharedPreferences prefs = getSharedPreferences("alkiria", MODE_PRIVATE); 
        String restoredText = prefs.getString("loginAlkiria", null);
        if (restoredText != null) {          
           e1.setText(restoredText);
           e2.setText(prefs.getString("passAlkiria",null));
           doLogin();
        }

    }
    
    protected class CommunicationTask extends AsyncTask<byte[], Void, String>{
        public static final String STOP = "#quitServer";
        public static final int PORT = 35421;
        private BufferedReader in = null;
        private DataOutputStream out = null;
        private Socket client = null;
        
        @Override
        protected void onPostExecute(String dades) {
        	if(dades.equals("LOGIN ERROR")){
            	Toast.makeText(getApplicationContext(), "Mail or password are not valid. Try again.", Toast.LENGTH_SHORT).show();
        	}else{
        		SharedPreferences.Editor editor= getSharedPreferences("alkiria", MODE_PRIVATE).edit();        		
        		editor.putString("loginAlkiria", e1.getText().toString());
        		editor.putString("passAlkiria", e2.getText().toString());        		
        		editor.putString("tokenAlkiria", dades);
        		editor.commit();
                Intent i = new Intent(MyActivity.this, Contact.class);
                startActivity(i);
                finish();
        	}
        }
        
    	@Override
    	protected String doInBackground(byte[]... params) {
            try {
                System.out.println(params[0]);
                client = new Socket("alkiria.xevimr.eu",PORT);
                out = new DataOutputStream(client.getOutputStream());
                //Escribim text
                out.writeInt(params[0].length);
                out.write(params[0]);
                out.flush();
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String dades = in.readLine();
                return dades;
            } catch (UnknownHostException ex) {
                Logger.getLogger(CommunicationTask.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CommunicationTask.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    out.close();
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(CommunicationTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            return null;
    	}	
    }
    

    @Override
    public void onClick(View v) {
            if(v == b1){
            	if (e1.getText().toString().equals("provaAlkiria")) {
            		Intent i = new Intent(MyActivity.this, Contact.class);
                    startActivity(i);
                    finish();
            	} else {
            		doLogin();
            	}
            }
            else if(v == b2){
                Intent j = new Intent(MyActivity.this, Settings.class);
                startActivity(j);
                finish();
            }

    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ap1:
              Intent i = new Intent(MyActivity.this, Settings.class);
              startActivity(i);
              finish();
            break;
            case R.id.ap2:
                Toast.makeText(getApplicationContext(),"You have been successfully logged out.", Toast.LENGTH_SHORT).show();
                finish();
        }
        return false;
    }



     //Clases añadidas al proyecto

    private void doLogin(){
        MsgUser msguser = new MsgUser();
        msguser.setLogin(e1.getText().toString());
        msguser.setPass(e2.getText().toString());
        byte[] msg = msguser.getMessage(MsgUser.TIPUS_USER_LOGIN);
        CommunicationTask c = new CommunicationTask();
        c.execute(msg);
    }
    


}
