/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.handler;

import com.sun.xml.wss.ProcessingContext;
import com.sun.xml.wss.SubjectAccessor;
import com.sun.xml.wss.XWSSProcessor;
import com.sun.xml.wss.XWSSProcessorFactory;
import com.sun.xml.wss.XWSSecurityException;
import com.sun.xml.wss.impl.callback.PasswordValidationCallback;
import com.ws.samples.callbacks.Verifier;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author jose.rubalcaba
 */
public class EchoSecurityHandler implements SOAPHandler<SOAPMessageContext>{

    private XWSSProcessor processor; 

    public EchoSecurityHandler() throws XWSSecurityException{
        XWSSProcessorFactory factory = null; 
        try{
            factory = XWSSProcessorFactory.newInstance(); 
        }catch(XWSSecurityException ex){
            System.err.println(ex);
        }
        FileInputStream config = null; 
        try{
            config = new FileInputStream(new File("META-INF/server.xml")); 
            processor = factory.createProcessorForSecurityConfiguration(config, new Verifier()); // a callback processor to handle authentication
            config.close();
        }catch(IOException ex){
            System.err.println(ex);
        }
    }
    
    
    
    public Set<QName> getHeaders() {
        
        String uri = "http://docs-oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"; 
        QName header = new QName(uri, "Security", "wsse");
        HashSet<QName> headers = new HashSet<QName>(); 
        headers.add(header); 
        return headers; 
    }

    public boolean handleMessage(SOAPMessageContext context) {
        Boolean out = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY); 
        SOAPMessage message = context.getMessage(); 
        if(!out.booleanValue()){
            //validate the message
            try{
                ProcessingContext pCxt = processor.createProcessingContext(message); 
                pCxt.setSOAPMessage(message);
                SOAPMessage verified = processor.verifyInboundMessage(pCxt); 
                context.setMessage(verified);
                System.out.println(SubjectAccessor.getRequesterSubject(pCxt));
                dumpMessage("Incoming message: ",  verified);
            }catch(XWSSecurityException ex){
                System.err.println(ex);
            }
        }
        return true; 
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true; 
    }

    public void close(MessageContext context) {
        
    }

    private void dumpMessage(String string, SOAPMessage message) {
        try{
            System.out.println(message);
            message.writeTo(System.out);
            System.out.println("");
        }catch(SOAPException ex){
            System.err.println(ex);
        }catch(IOException ex){
            System.err.println(ex);
        }
    }
    
    
    
}
