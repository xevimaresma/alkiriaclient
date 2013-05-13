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
	
	private Handler uiCallback;

    private EditText message;
    private Button enviar;
    private String mailContacte;
    private MsgSender missEnvia;
    LinearLayout la;
    
    byte[] buf = new byte[260];
    DatagramSocket socket;
    DatagramPacket packet;
    
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
                dadesRebudes=missEnvia.enviamentUDP(params[0],socket);                
            } catch (Exception ex) {
                Logger.getLogger(CommunicationTaskUDP.class.getName()).log(Level.SEVERE, null, ex);
                dadesRebudes="ERROR";
            } 
            return dadesRebudes;
    	}	
    }
    
/*    protected class UDPListener extends AsyncTask<Void, Void, Void>{        
        public static final int PORT = 9876;
        
        @Override
    	protected Void doInBackground(Void...params) {
        	ScrollView sv = (ScrollView)findViewById(R.id.messageList);
    		while (true) {
    			byte[] buf = new byte[196];    			
    			try {
    				    				    				
    				DatagramSocket socket = new DatagramSocket(PORT);
    				DatagramPacket packet = new DatagramPacket(buf, buf.length);
    				socket.receive(packet);
    				//Toast.makeText(getApplicationContext(),"Rebo paquet UDP Listener", Toast.LENGTH_LONG).show();
    				ByteBuffer buffer = ByteBuffer.wrap(packet.getData());
    				InetAddress IPAddress = packet.getAddress();
    	            int port = packet.getPort();    	            
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
    	            /*try {
    	            	encripta.decrypt(arrmsg);
    	            } catch (Exception e) { } 
    	            String missatgeS=encripta.getMsgDesencriptat();
    	            String dadestxt="Missatge de "+token+" a "+desti+" que diu "+missatgeS;
    	            String dadestxt="Missatge de "+token+" a "+desti+".";
    	            TextView tv = new TextView(getApplicationContext());
    				sv.addView(tv);
    				message = (EditText)findViewById(R.id.message);    				
    				
    				tv.setText(dadestxt);    				
    				message.setText(dadestxt);
    				
    			} catch (Exception e) {    				    				
    			}
    		}            
    	}	
    }
*/   
    protected class CommunicationTaskUDPByte extends AsyncTask<byte[], Void, Void>{        
        public static final int PORT = 9876;
        
        @Override
        protected void onPostExecute(Void param) {        	
        	/*
        	 * Aquest codi haurà d'anar al UDPListener, però no aconsegueixo que escolti, el UDP de dalt!!!!
			*/
        	/*ByteBuffer buffer = ByteBuffer.wrap(dades);
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
            Toast.makeText(getApplicationContext(),dadestxt, Toast.LENGTH_LONG).show();*/
        }
        
    	@Override
    	protected Void doInBackground(byte[]... params) {
    		byte[] dadesRebudes;
            try {
                //dadesRebudes=missEnvia.enviamentUDPByte(params[0]);                
            	missEnvia.enviamentUDPByte(params[0],socket);
            } catch (Exception ex) {
                Logger.getLogger(CommunicationTaskUDPByte.class.getName()).log(Level.SEVERE, null, ex);
                dadesRebudes=null;
            } 
            //return dadesRebudes;
            return null;
    	}	
    }

    protected class CommunicationTaskUDPByteActualitza extends AsyncTask<byte[], Void, Void>{        
        public static final int PORT = 9876;
        
        @Override
        protected void onPostExecute(Void dades) {
        	/*ScrollView sv = (ScrollView)findViewById(R.id.messageList);

			ByteBuffer buffer = ByteBuffer.wrap(dades);
            int tipus = buffer.getInt();
            byte[] arrtoken = new byte[64];
            buffer.get(arrtoken);
            byte[] arrdesti = new byte[64];
            buffer.get(arrdesti);
            byte[] arrremitent = new byte[64];            
            buffer.get(arrremitent);
            byte[] arrmsg = new byte[64];            
            buffer.get(arrmsg);          
            
            String token=new String(arrtoken).trim();  
            Encryption encripta=new Encryption();
            encripta.setClau(token);
            try {
            	encripta.decrypt(arrmsg);
            } catch (Exception e) { } 
            
            String desti=new String(arrdesti);
            String dadestxt="Missatge de "+token+" a "+desti+" desde "+arrremitent+".";
            String missatgeS=encripta.getMsgDesencriptat();
        	TextView tv = new TextView(getApplicationContext());
			sv.addView(tv);
			message = (EditText)findViewById(R.id.message);*/    				
			
			//tv.setText(missatgeS);
			//tv.setText(new String(dades));
			//message.setText(dadestxt);
        	
        	/*
        	 * Aquest codi haurà d'anar al UDPListener, però no aconsegueixo que escolti, el UDP de dalt!!!!
			*/
 /*       	ByteBuffer buffer = ByteBuffer.wrap(dades);
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
*/            
        }
        
    	@Override
    	protected Void doInBackground(byte[]... params) {
    		byte[] dadesRebudes;
            try {
                dadesRebudes=missEnvia.enviamentUDPByte(params[0],socket);                
            } catch (Exception ex) {
                Logger.getLogger(CommunicationTaskUDPByte.class.getName()).log(Level.SEVERE, null, ex);
                dadesRebudes=null;
            } 
            return null;
    	}	
    }
    
    
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(chat);
        Intent intent = getIntent();
        la = (LinearLayout)findViewById(R.id.messageListlayout);
        
        uiCallback = new Handler () {
            public void handleMessage (Message msg) {
                TextView tv = new TextView(getApplicationContext());
                tv.setText("Proves");
                la.addView(tv);
                //sv.removeAllViews();
    			//sv.addView(tv);
            }
        };
        
        this.mailContacte=intent.getStringExtra("contacto");
        Toast.makeText(getApplicationContext(),mailContacte, Toast.LENGTH_LONG).show();
        message = (EditText)findViewById(R.id.message);
        enviar = (Button)findViewById(R.id.sendMessage);
        //UDPListener servUDP=new UDPListener();
        //servUDP.execute();
        try {
	        
	  		socket = new DatagramSocket(9876);
	  		socket.setBroadcast(true);
            socket.setReuseAddress(true);
	  		//packet = new DatagramPacket(buf, buf.length); 
        } catch (Exception e){
        	System.out.println("ERROR "+e.toString());
        }
        
       Thread t = new Thread() {
    	    public void run() {           	 
           	 try {           	
           		 Log.d("UDP Receiver", "Preparant receptor UDP...");
           		 while (true) {
           			 /*socket.receive(packet);
           			 message = (EditText)findViewById(R.id.message);
           			 message.setText(packet.toString());
           			 Log.e("Click: ", "Missatge UDP: "+packet.toString());
           			 Toast.makeText(getApplicationContext(),"Rebo paquet UDP Listener", Toast.LENGTH_LONG).show();*/
           			Log.d("UDP Receiver", "Listening...");
                    byte[] buf = new byte[260];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    ByteBuffer buffer = ByteBuffer.wrap(packet.getData());
                    int tipus = buffer.getInt();
                    if (tipus==3) {
                    	byte[] arrtoken = new byte[64];
                        buffer.get(arrtoken);
                        byte[] arrdesti = new byte[64];
                        buffer.get(arrdesti);
                        byte[] arrremitent = new byte[64];            
                        buffer.get(arrremitent);
                        byte[] arrmsg = new byte[64];            
                        buffer.get(arrmsg);          
                        
                        String token=new String(arrtoken).trim();
                        String remitent=new String(arrremitent).trim();
                        String mistxt=new String(arrmsg).trim();
                        String desti=new String(arrdesti).trim();
                        String missatgeS="";
                        Encryption encripta=new Encryption();
                        encripta.setClau(token);
                        try {
                        	encripta.decrypt(arrmsg);
                        	missatgeS=encripta.getMsgDesencriptat().trim();
                        } catch (Exception e) { } 
                        
                        String dadestxt="Missatge de "+remitent+" ("+token+") a "+desti+" desde i missatge ("+mistxt+"): "+missatgeS+".";

                    	Log.d("UDP",dadestxt);
                    	uiCallback.sendEmptyMessage(0);
/*                    	TextView tv = new TextView(getApplicationContext());
                    	ScrollView sv = (ScrollView)findViewById(R.id.messageList);
            			sv.addView(tv);
            			tv.setText(dadestxt);*/
            			message = (EditText)findViewById(R.id.message);
            			
                    }
                    //Log.d("UDP", "Received: '" + new String(packet.getData()).trim() + "'");
           		 }           		 
           	 } catch (Exception e) {  
           		 System.out.println(e.toString());
           	 }   
    	    } 
    	};
    	
    	t.start();
        
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == enviar){
                    String msg = message.getText().toString();
                    //SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences prefs = getSharedPreferences("alkiria", MODE_PRIVATE);
                    String token= prefs.getString("tokenAlkiria", null);
                    if (token==null) { token="0000"; }
                    //Toast.makeText(getApplicationContext(),"A enviar ("+token+"): "+msg, Toast.LENGTH_LONG).show();
                    

                    missEnvia=new MsgSender(msg,token);
                    try {
                    	byte[] missEnviaByte=missEnvia.enviaMsg(token,mailContacte,2);
                    	CommunicationTaskUDP c = new CommunicationTaskUDP();
                        c.execute(missEnviaByte);
                    } catch (Exception e) {
                    	Toast.makeText(getApplicationContext(),"Error: "+e.toString(), Toast.LENGTH_LONG).show();
                    	e.printStackTrace();
                    }
                    //Toast.makeText(getApplicationContext(),"Enviado o no?", Toast.LENGTH_LONG).show();
                    
                    //AquÃ­ tengo que empezar a aÃ±adir el contenido al scrollview
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
                	CommunicationTaskUDPByteActualitza c = new CommunicationTaskUDPByteActualitza();
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