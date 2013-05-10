package com.example.WhatsGroupDesign;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.whatsgroup.alkiria.entities.MsgUser;

import android.app.Activity;
import android.content.Intent;
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
    
    protected class CommunicationTask extends AsyncTask<byte[], Integer, String>{
        public static final String STOP = "#quitServer";
        public static final int PORT = 35421;
        private BufferedReader in = null;
        private DataOutputStream out = null;
        private Socket client = null;
        
    	public String sendMessage(byte[] msg){
            try {
                System.out.println(msg);
                //client = new Socket("localhost",PORT);
                //client = new Socket("10.0.2.2",PORT);
                client = new Socket("alkiria.xevimr.eu",PORT);
                out = new DataOutputStream(client.getOutputStream());
                //Escribim text
                out.writeInt(msg.length);
                out.write(msg);
                out.flush();
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String dades = in.readLine();
                //System.out.println("Resposta: " + dades);
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

    	@Override
    	protected String doInBackground(byte[]... params) {
    		// TODO Auto-generated method stub
    		Toast.makeText(getApplicationContext(), "El teu Token es: " + params.toString(), Toast.LENGTH_SHORT).show();
    		return null;
    	}	
    }
    

    @Override
    public void onClick(View v) {
            if(v == b1){
            	doLogin();
            	//Toast.makeText(getApplicationContext(), "El teu Token es: " + doLogin(), Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(MyActivity.this, Chat.class);
                //startActivity(i);
                //finish();
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
                break;
            case R.id.ap2:
                Toast.makeText(getApplicationContext(),"bYe, bYe!!", Toast.LENGTH_SHORT).show();
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
