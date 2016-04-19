/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.callbacks;

import com.sun.xml.wss.impl.callback.UsernameCallback;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *
 * @author jose.rubalcaba
 */
public class Prompter implements CallbackHandler{

    private String readLine(){
        String line = null; 
        try{
            line = new BufferedReader(new InputStreamReader(System.in)).readLine(); 
        }catch(IOException ex){
            System.err.println(ex);
        }
        return line; 
    }
    
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if(callbacks[i] instanceof UsernameCallback){
                UsernameCallback cb = (UsernameCallback)callbacks[i];
                System.out.println("Username: ");
                String username = readLine(); 
                if(username != null)cb.setUsername(username);
            }else if(callbacks[i] instanceof PasswordCallback){
                PasswordCallback cb = (PasswordCallback)callbacks[i]; 
                System.out.println("Password: ");
                String password = readLine(); 
                if(password != null)cb.setPassword(password.toCharArray());
            }
        }
    }
    
}
