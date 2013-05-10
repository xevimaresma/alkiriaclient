package com.whatsgroup.alkiria.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.AsyncTask;

public class CommunicationTask extends AsyncTask<byte[], Integer, String>{
    public static final String STOP = "#quitServer";
    public static final int PORT = 35421;
    private BufferedReader in = null;
    private DataOutputStream out = null;
    private Socket client = null;
    
	public String sendMessage(byte[] msg){
        try {
            System.out.println(msg);
            //client = new Socket("localhost",PORT);
            client = new Socket("10.0.2.2",PORT);
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
		return null;
	}	
}
