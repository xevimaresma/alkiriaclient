package com.whatsgroup.alkiria.entities;

import java.nio.ByteBuffer;

import com.whatsgroup.alkiria.utils.Encryption;

public class MsgUser {
    public static final int TIPUS_USER_CREATE = 1;
    public static final int TIPUS_USER_LOGIN = 2;
    private String login;
    private String pass;
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    public byte[] getMessage(int tipus){
        byte[] loginchars = new byte[64];
        byte[] passchars = new byte[64];
        
        loginchars = login.getBytes();
        passchars = pass.getBytes();
        
        ByteBuffer buffer = ByteBuffer.allocate(132);
        buffer.putInt(tipus);
        buffer.put(loginchars);
        buffer.position(68);
        buffer.put(passchars);
        //buffer.put(loginchars);
        //buffer.put(passchars);
        Encryption encrypt = new Encryption();
        encrypt.setClau(Encryption.CLAU);
        try {
            encrypt.encrypt(buffer.array());
            return encrypt.getMsgEncriptat();
        } catch (Exception ex) {
            //Logger.getLogger(MsgUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

