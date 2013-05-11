package com.example.WhatsGroupDesign;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.WhatsGroupDesign.MyActivity.CommunicationTask;
import com.whatsgroup.alkiria.utils.Encryption;
import com.whatsgroup.alkiria.utils.MsgSender;

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
//import android.widget.ScrollView;
import android.widget.Toast;

import static com.example.WhatsGroupDesign.R.layout.chat;

public class Chat extends Activity {

    private EditText message;
    private Button enviar;
    private String mailContacte;
    private MsgSender missEnvia;

    protected class CommunicationTaskUDP extends AsyncTask<byte[], Void, String>{        
        public static final int PORT = 9876;
        
        @Override
        protected void onPostExecute(String dades) {
        	dades=dades.trim();
        	if (dades.equals("OK")) {
        		message = (EditText)findViewById(R.id.message);
        		message.setText(null);
        	} else if (dades.equals("KO")) {
        		Toast.makeText(getApplicationContext(),"ERROR! Torna-ho a intentar.", Toast.LENGTH_LONG).show();
        	} else {
        		Toast.makeText(getApplicationContext(),"Resposta: "+dades, Toast.LENGTH_LONG).show();
        	}
        	
        }
        
    	@Override
    	protected String doInBackground(byte[]... params) {
    		String dadesRebudes;
            try {
                dadesRebudes=missEnvia.enviamentUDP(params[0]);                
            } catch (Exception ex) {
                Logger.getLogger(CommunicationTaskUDP.class.getName()).log(Level.SEVERE, null, ex);
                dadesRebudes="ERROR";
            } 
            return dadesRebudes;
    	}	
    }
    
    protected class CommunicationTaskUDPByte extends AsyncTask<byte[], Void, byte[]>{        
        public static final int PORT = 9876;
        
        @Override
        protected void onPostExecute(byte[] dades) {        	
        	/*if (dades.equals("OK")) {
        		message = (EditText)findViewById(R.id.message);
        		message.setText(null);
        	} else if (dades.equals("KO")) {
        		Toast.makeText(getApplicationContext(),"ERROR! Torna-ho a intentar.", Toast.LENGTH_LONG).show();
        	} else {
        		Toast.makeText(getApplicationContext(),"Resposta: "+dades, Toast.LENGTH_LONG).show();
        	}*/
        	ByteBuffer buffer = ByteBuffer.wrap(dades);
            int tipus = buffer.getInt();
            byte[] arrtoken = new byte[64];
            buffer.get(arrtoken);
            byte[] arrdesti = new byte[64];
            buffer.get(arrdesti);
            byte[] arrmsg = new byte[64];
            buffer.get(arrmsg);                
            String token=new String(arrtoken).trim();  
            Encryption encripta=new Encryption();
            encripta.setClau(token);
            String desti=new String(arrdesti);
            //String missatgeS=new String(arrmsg);
            try {
            	encripta.decrypt(arrmsg);
            } catch (Exception e) { } 
            String missatgeS=encripta.getMsgDesencriptat();
            String dadestxt="Missatge de "+token+" a "+desti+" que diu "+missatgeS;
            Toast.makeText(getApplicationContext(),dadestxt, Toast.LENGTH_LONG).show();
        }
        
    	@Override
    	protected byte[] doInBackground(byte[]... params) {
    		byte[] dadesRebudes;
            try {
                dadesRebudes=missEnvia.enviamentUDPByte(params[0]);                
            } catch (Exception ex) {
                Logger.getLogger(CommunicationTaskUDPByte.class.getName()).log(Level.SEVERE, null, ex);
                dadesRebudes=null;
            } 
            return dadesRebudes;
    	}	
    }
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(chat);
        Intent intent = getIntent();
        this.mailContacte=intent.getStringExtra("contacto");
        Toast.makeText(getApplicationContext(),mailContacte, Toast.LENGTH_LONG).show();
        message = (EditText)findViewById(R.id.message);
        enviar = (Button)findViewById(R.id.sendMessage);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == enviar){
                    String msg = message.getText().toString();
                    //SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences prefs = getSharedPreferences("alkiria", MODE_PRIVATE);
                    String token= prefs.getString("tokenAlkiria", null);
                    if (token==null) { token="000"; }
                    //Toast.makeText(getApplicationContext(),"Enviant...", Toast.LENGTH_LONG).show();
                    

                    missEnvia=new MsgSender(msg,token);
                    try {
                    	byte[] missEnviaByte=missEnvia.enviaMsg(token,mailContacte,2);
                    	CommunicationTaskUDP c = new CommunicationTaskUDP();
                        c.execute(missEnviaByte);
                    } catch (Exception e) {
                    	Toast.makeText(getApplicationContext(),"Error: "+e.toString(), Toast.LENGTH_LONG).show();
                    	e.printStackTrace();
                    }                    
                    
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
                finish();
                break;
            case R.id.add:
                //Toast.makeText(getApplicationContext(),"Updating your contacts", Toast.LENGTH_LONG).show();
            	SharedPreferences prefs = getSharedPreferences("alkiria", MODE_PRIVATE);
                String token= prefs.getString("tokenAlkiria", null);
                missEnvia=new MsgSender("",token);
                try {
                	byte[] missEnviaByte=missEnvia.enviaMsg(token,mailContacte,3);
                	CommunicationTaskUDPByte c = new CommunicationTaskUDPByte();
                    c.execute(missEnviaByte);
                } catch (Exception e) {
                	Toast.makeText(getApplicationContext(),"Error: "+e.toString(), Toast.LENGTH_LONG).show();
                	e.printStackTrace();
                }                    
                //finish();
        }
        return false;
    }
}