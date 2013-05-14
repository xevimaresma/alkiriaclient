package com.example.WhatsGroupDesign;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.WhatsGroupDesign.R.layout.chat;

public class Chat extends Activity {
	
	// Definim variables, primer un handler per poder actualitzar els missatges quan s'actualitzi
	private Handler uiCallback;

	// Els elements de la interfície
    private EditText message;
    private Button enviar;
    private String mailContacte;
    private MsgSender missEnvia;
    LinearLayout la;
    
    // I les dades de socket, per tenir-ne només un i poder rebre de forma centralitzada.
    byte[] buf = new byte[260];
    DatagramSocket socket;
    DatagramPacket packet;
    
    // Mètodes AsyncTask per comunicació UDP.
    
    // Aquest cas és el d'enviament de missatge. Fa l'enviament al servidor, espera la resposta, i
    // si aquesta és OK, esborra el text de l'input, i en cas contrari dóna l'error o mostra la
    // resposta del servidor.
    protected class CommunicationTaskUDP extends AsyncTask<byte[], Void, String>{        
        public static final int PORT = 9876;
        
        @Override
        protected void onPostExecute(String dades) {
        	dades=dades.trim();
        	if (dades.equals("OK")) {
        		message = (EditText)findViewById(R.id.message);
        		message.setText(null);
        	} else if (dades.equals("KO")) {
        		Toast.makeText(getApplicationContext(),"Error: please, try again", Toast.LENGTH_LONG).show();
        	} else {
        		Toast.makeText(getApplicationContext(),"Server response: "+dades, Toast.LENGTH_LONG).show();
        	}
        	
        }
        
    	@Override
    	protected String doInBackground(byte[]... params) {
    		String dadesRebudes;
            try {
                dadesRebudes=missEnvia.enviamentUDP(params[0],socket);                
            } catch (Exception ex) {
                Logger.getLogger(CommunicationTaskUDP.class.getName()).log(Level.SEVERE, null, ex);
                dadesRebudes="ERROR";
            } 
            return dadesRebudes;
    	}	
    }
    
    // En aquest cas, el mètode no fa res OnPostExecute, només llança el missatge via UDP
    // perquè un thread que obrim a l'onCreate el "caci" després.
    protected class CommunicationTaskUDPByteActualitza extends AsyncTask<byte[], Void, Void>{        
        public static final int PORT = 9876;
        
        @Override
        protected void onPostExecute(Void dades) {
        }
        
    	@Override
    	protected Void doInBackground(byte[]... params) {    		
            try {
                missEnvia.enviamentUDPByte(params[0],socket);                
            } catch (Exception ex) {
                Logger.getLogger(CommunicationTaskUDPByteActualitza.class.getName()).log(Level.SEVERE, null, ex);                
            } 
            return null;
    	}	
    }
    
    
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(chat); 
        Intent intent = getIntent();
        // Instanciem el LinearLayout que és on hi haurà els missatges
        la = (LinearLayout)findViewById(R.id.messageListlayout);        
        la.setOrientation(LinearLayout.VERTICAL);
        // I creem un handler per poder actualitzar fàcilment.
        uiCallback = new Handler () {
            public void handleMessage (Message msg) {         
            	// Rebrà un missatge (msg), instanciarà un nou TextView de text negre
                TextView tv = new TextView(getApplicationContext());
                tv.setTextColor(Color.BLACK);
                // I li assignarà les dades del missatge, partint el text a ||END|| i prenent només la part que hi hagi davant,
                // així no tenim espais en blanc usats per omplir l'array de bytes.
                tv.setText(msg.getData().getString("msg").split("\\|\\|END\\|\\|")[0]);
                // Unnim la vista TextView al LinearLayout
                la.addView(tv);
            }
        };
        
        // Prenem la variable que ens venia amb el mail del contacte
        this.mailContacte=intent.getStringExtra("contacto");
        // La mostrem en un toast
        Toast.makeText(getApplicationContext(),mailContacte, Toast.LENGTH_LONG).show();
        message = (EditText)findViewById(R.id.message);
        enviar = (Button)findViewById(R.id.sendMessage);
        // I comencem: connectem el socket al port 9876.
        try {	        
	  		socket = new DatagramSocket(9876);
	  		socket.setBroadcast(true);
            socket.setReuseAddress(true);
        } catch (Exception e){
        	System.out.println("ERROR "+e.toString());
        }
        
        // Ara obrim un thread per "escoltar" el socket UDP
       Thread t = new Thread() {
    	    public void run() {           	 
           	 try {           	
           		 //Log.d("UDP Receiver", "Preparant receptor UDP...");
           		 while (true) {
           			//Log.d("UDP Receiver", "Listening...");
                    byte[] buf = new byte[268];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    ByteBuffer buffer = ByteBuffer.wrap(packet.getData());
                    int tipus = buffer.getInt();
                    // Quan arribin dades prenem el primer enter. Si és 3, ens arriba llista de missatges
                    if (tipus==3) {
                    	// per tant, prenem les dades i les guardem
                    	byte[] arrtoken = new byte[64];
                        buffer.get(arrtoken);
                        byte[] arrdesti = new byte[64];
                        buffer.get(arrdesti);
                        byte[] arrremitent = new byte[64];            
                        buffer.get(arrremitent);
                        byte[] arrmsg = new byte[64];            
                        buffer.get(arrmsg);  
                        int horaLliura=buffer.getInt();
                        int horaEnvia=buffer.getInt();                        
                        // Convertim bytes a cadenes
                        String token=new String(arrtoken).trim();
                        String remitent=new String(arrremitent).trim();
                        String mistxt=new String(arrmsg).trim();
                        String desti=new String(arrdesti).trim();
                        String missatgeS="";
                        Encryption encripta=new Encryption();
                        encripta.setClau(token);
                        // Intentem desencriptar el missatge
                        try {
                        	encripta.decrypt(arrmsg);
                        	missatgeS=encripta.getMsgDesencriptat().trim();
                        } catch (Exception e) { } 
                        // I com que arriben tots els missatges junts, només mostrem els del remitent
                        // que ens ha arribat amb l'intent
                        if (remitent.equals(mailContacte)) {
	                        String dadestxt="<"+remitent+"> "+missatgeS+".";
	                    	//Log.d("UDP",dadestxt);
	                        
	                        // I l'escribim amb el handler que havíem creat abans
	                    	Bundle b = new Bundle();
	                    	b.putString("msg", dadestxt);
	                    	Message msg = new Message();
	                    	msg.setData(b);
	                    	uiCallback.sendMessage(msg);
                        }
            			
                    }
           		 }           		 
           	 } catch (Exception e) {  
           		 System.out.println(e.toString());
           	 }   
    	    }     	               
    	};
    	// Engeguem el thread
    	t.start();
    	
    	// Prenem el token de les SharedPreferences
    	SharedPreferences prefs = getSharedPreferences("alkiria", MODE_PRIVATE);
        String token= prefs.getString("tokenAlkiria", null);
        missEnvia=new MsgSender("",token);
        // I enviem un missatge al servidor sol·licitant la llista de missatges rebuts, per mostrar-los d'entrada.
        try {
        	byte[] missEnviaByte=missEnvia.enviaMsg(token,mailContacte,3);                	
        	CommunicationTaskUDPByteActualitza c = new CommunicationTaskUDPByteActualitza();
            c.execute(missEnviaByte);
        } catch (Exception e) {
        	Toast.makeText(getApplicationContext(),"Error: "+e.toString(), Toast.LENGTH_LONG).show();
        	e.printStackTrace();
        }        
    	    	
        // Botó enviar missatge 
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == enviar){
                	// Passem el missatge a text, prenem el token per xifrar-lo i el generem
                    String msg = message.getText().toString();
                    SharedPreferences prefs = getSharedPreferences("alkiria", MODE_PRIVATE);
                    String token= prefs.getString("tokenAlkiria", null);
                    if (token==null) { token="0000"; }
                    missEnvia=new MsgSender(msg,token);
                    try {
                    	// Finalment preparem l'enviament i el fem amb una AsyncTask
                    	byte[] missEnviaByte=missEnvia.enviaMsg(token,mailContacte,2);
                    	CommunicationTaskUDP c = new CommunicationTaskUDP();
                        c.execute(missEnviaByte);
                        // I directament afegim a la UI via handler el missatge enviat
                        Bundle b = new Bundle();                      
                    	b.putString("msg", "-> "+missEnvia.getMissatge());
                    	Message msgPosa = new Message();
                    	msgPosa.setData(b);
                    	uiCallback.sendMessage(msgPosa);
                    } catch (Exception e) {
                    	Toast.makeText(getApplicationContext(),"Error: "+e.toString(), Toast.LENGTH_LONG).show();
                    	e.printStackTrace();
                    }
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
            	// Aquest botó torna enrere
                Intent i = new Intent(Chat.this, Contact.class);
                startActivity(i);
                socket.close();
                finish();
                break;
            case R.id.add:
            	// I aquest sol·licita actualització, com quan hem entrat per primer cop a l'Activity
            	SharedPreferences prefs = getSharedPreferences("alkiria", MODE_PRIVATE);
                String token= prefs.getString("tokenAlkiria", null);
                missEnvia=new MsgSender("",token);
                try {
                	la = (LinearLayout)findViewById(R.id.messageListlayout);
                    la.removeAllViews();
                	byte[] missEnviaByte=missEnvia.enviaMsg(token,mailContacte,3);                	
                	CommunicationTaskUDPByteActualitza c = new CommunicationTaskUDPByteActualitza();
                    c.execute(missEnviaByte);
                } catch (Exception e) {
                	Toast.makeText(getApplicationContext(),"Error: "+e.toString(), Toast.LENGTH_LONG).show();
                	e.printStackTrace();
                }                        
        }
        return false;
    }
}