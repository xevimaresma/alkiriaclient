package com.whatsgroup.alkiria.utils;

import java.nio.ByteBuffer;
import java.io.*;
import java.net.*;

import com.example.WhatsGroupDesign.Chat;
import com.example.WhatsGroupDesign.R;

public class MsgSender {
	//private String servidor="alkiria.xevimr.eu";
	//private String servidor="192.168.1.171";
	private String servidor="10.0.2.2";
    public static final int TIPUS_ENVIA_MSG = 2;
    public static final int TIPUS_DEMANA_MSG = 3;
    public static final int TIPUS_LLIURA_MSG = 4;
    private String missatge;   
    private String clauEncriptacio;    
    private String mailDesti;
    
    private int port=9876;
    
    public MsgSender(String missatgeRep) {
        this.missatge=missatgeRep;
        this.arreglaCadena();
    }
    
    public MsgSender(String missatgeRep, String clauEncripta) {
        this.missatge=missatgeRep;
        this.clauEncriptacio=clauEncripta;
        this.arreglaCadena();
    }
    
    public void setMissatge(String missatgeRep) {
        this.missatge=missatgeRep;
        this.arreglaCadena();
    }
    
    public String getMissatge() {
        this.arreglaCadena();
        return this.missatge;
    }
    
    public void setClau(String clauEncripta) {
        this.clauEncriptacio=clauEncripta;
    }
    
    public byte[] enviaMsg() throws Exception {        
        //String token="518bd9ed53f1b46ca694ddb5";
        String token="515dd85856861ee247ccf15a";
        String destinatari="prova";
        int tipusMissatge=TIPUS_ENVIA_MSG;
        return enviaMsg(token,destinatari,"",tipusMissatge);
    }
    
    public byte[] enviaMsg(String token, String destinatari, int tipusMissatge) throws Exception {
    	return enviaMsg(token,destinatari,"",tipusMissatge);
    }
    
    public byte[] enviaMsg(String token, String destinatari, String remitent, int tipusMissatge) throws Exception {                        
        byte[] sendData = new byte[64];          
                        
        Encryption encripta=new Encryption();
        encripta.setClau(this.clauEncriptacio);
        encripta.encrypt(this.missatge);
        this.arreglaCadena();            
        //System.out.println(encripta.toString());
        sendData=encripta.getMsgEncriptat();                
        
        ByteBuffer buffer;
        if (tipusMissatge==3) {
            byte[] valors = new byte[260];        
            buffer = ByteBuffer.wrap(valors);
            buffer.putInt(tipusMissatge);        
            buffer.put(token.getBytes());
            buffer.position(68);
            buffer.put(destinatari.getBytes());
            buffer.position(132);
            buffer.put(remitent.getBytes());
            buffer.position(196);
            buffer.put(sendData);           
        } else {
            byte[] valors = new byte[196];        
            buffer = ByteBuffer.wrap(valors);
            buffer.putInt(tipusMissatge);        
            buffer.put(token.getBytes());
            buffer.position(68);
            buffer.put(destinatari.getBytes());
            buffer.position(132);
            buffer.put(sendData);
        };
        
        return buffer.array();
    }
    
    public String enviamentUDP(byte[] msg, DatagramSocket socket){
        try {                       
            byte[] sendData = new byte[196];
            byte[] receiveData = new byte[1024]; 
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            //DatagramSocket clientSocket = new DatagramSocket();                       
            InetAddress IPAddress = InetAddress.getByName(servidor);
            DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, IPAddress, port);
            //clientSocket.send(sendPacket);
            socket.send(sendPacket);
            return "OK";
            /*DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);           
            String modifiedSentence = new String(receivePacket.getData());          
            clientSocket.close();
            return modifiedSentence;*/
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
    
    public byte[] enviamentUDPByte(byte[] msg, DatagramSocket socket){
        try {                       
            byte[] sendData = new byte[196];
            byte[] receiveData = new byte[1024];             
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(servidor);                        
            //InetAddress IPAddress = InetAddress.getByName("192.168.1.171");
            DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, IPAddress, port);
            //clientSocket.send(sendPacket);
            socket.send(sendPacket);
            /*DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            return receivePacket.getData();*/
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void enviamentUDPByteSenseResposta(byte[] msg){
        try {                       
            byte[] sendData = new byte[196];
            byte[] receiveData = new byte[1024];             
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(servidor);                        
            //InetAddress IPAddress = InetAddress.getByName("192.168.1.171");
            DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, IPAddress, port);
            clientSocket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void arreglaCadena() {
        String[] partsCadena;
        partsCadena=this.missatge.split("\\|\\|END\\|\\|");
        this.missatge=partsCadena[0];               
    }

}
