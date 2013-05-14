package com.example.WhatsGroupDesign;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.WhatsGroupDesign.MyActivity.CommunicationTask;
import com.whatsgroup.alkiria.entities.MsgUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.WhatsGroupDesign.R.layout.settings;



public class Settings extends Activity implements View.OnClickListener {
    private Button btnCancel;
    private Button btnCreate;
    private EditText username;
    private EditText password;
    private EditText retype;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(settings);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        retype = (EditText)findViewById(R.id.retype);
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
            finish();
        }
        if(v ==btnCreate){
        	if(password.getText().toString().equals("") || username.getText().toString().equals("")){
        		Toast.makeText(getApplicationContext(), "Error: username or password are empty.", Toast.LENGTH_SHORT).show();
        	}else{
        		if(password.getText().toString().equals(retype.getText().toString())){
        			createUser();
        		}else{
        			Toast.makeText(getApplicationContext(), "Password and confirmation doesn't match.", Toast.LENGTH_SHORT).show();
        		}
        	}
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
            case R.id.exit:
            Intent i = new Intent(Settings.this, MyActivity.class);
            startActivity(i);
            finish();
            break;
        }
        return false;
    }
    
    private void createUser(){
        MsgUser msguser = new MsgUser();
        msguser.setLogin(username.getText().toString());
        msguser.setPass(password.getText().toString());
        byte[] msg = msguser.getMessage(MsgUser.TIPUS_USER_CREATE);
        CommunicationTask c = new CommunicationTask();
        c.execute(msg);
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
            	Toast.makeText(getApplicationContext(), "Error creating user, try again.", Toast.LENGTH_SHORT).show();
        	}else{
        		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        		editor.putString("loginAlkiria", username.getText().toString());
        		editor.putString("passAlkiria", password.getText().toString());
        		editor.commit();
            	Intent l = new Intent(Settings.this,Contact.class);
                Toast.makeText(getApplicationContext(), "Account is created!", Toast.LENGTH_SHORT).show();
                startActivity(l);
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


}