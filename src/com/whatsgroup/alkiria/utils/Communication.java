package com.whatsgroup.alkiria.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Communication {
    public static final String STOP = "#quitServer";
    public static final int PORT = 35421;
    private BufferedReader in = null;
    private DataOutputStream out = null;
    private Socket client = null;
    
	public void sendMessage(byte[] msg){
        try {
            System.out.println(msg);
            client = new Socket("localhost",PORT);
            out = new DataOutputStream(client.getOutputStream());
            //Escribim text
            out.writeInt(msg.length);
            out.write(msg);
            out.flush();
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String dades = in.readLine();
            System.out.println("Resposta: " + dades);
        } catch (UnknownHostException ex) {
            //Logger.getLogger(AlkiriaClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(AlkiriaClient.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                out.close();
                client.close();
            } catch (IOException ex) {
                //Logger.getLogger(AlkiriaClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
	}	
}
