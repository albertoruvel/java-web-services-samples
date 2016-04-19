/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.callbacks;

import com.sun.xml.wss.impl.callback.PasswordValidationCallback;
import com.ws.samples.handler.EchoSecurityHandler;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *
 * @author jose.rubalcaba
 */
public class Verifier implements CallbackHandler{
    private static final String username = "alberto";
    private static final String password = "rubalcaba";

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof PasswordValidationCallback) {
                PasswordValidationCallback cb = (PasswordValidationCallback) callbacks[i];
                if (cb.getRequest() instanceof PasswordValidationCallback.PlainTextPasswordRequest) {
                    cb.setValidator(new PlainTextPasswordVerifier());
                }
            }
        }
    }
    
    private class PlainTextPasswordVerifier implements PasswordValidationCallback.PasswordValidator{

        public boolean validate(PasswordValidationCallback.Request req) throws PasswordValidationCallback.PasswordValidationException {
            PasswordValidationCallback.PlainTextPasswordRequest pass = 
                    (PasswordValidationCallback.PlainTextPasswordRequest)req; 
            if(username.equals(pass.getUsername()) && password.equals(pass.getPassword()))
                return true; 
            else return false; 
        }
        
    }
}
