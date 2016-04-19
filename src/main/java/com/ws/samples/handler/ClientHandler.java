/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.handler;

import com.sun.xml.wss.ProcessingContext;
import com.sun.xml.wss.XWSSProcessor;
import com.sun.xml.wss.XWSSProcessorFactory;
import com.sun.xml.wss.XWSSecurityException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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
public class ClientHandler implements SOAPHandler<SOAPMessageContext>{

    private XWSSProcessor processor; 

    public ClientHandler() {
        XWSSProcessorFactory fac = null; 
        try{
            fac = XWSSProcessorFactory.newInstance(); 
        }catch(XWSSecurityException ex){
            System.err.println(ex);
        }
        //read client configuration file and configure security 
        try{
            FileInputStream config = new FileInputStream(new File("META-INF/client.xml")); 
            processor = fac.createProcessorForSecurityConfiguration(config, new Prompter()); 
            config.close(); 
        }catch(XWSSecurityException ex){
            
        }catch(IOException ex){
            
        }
    }
    
    
    
    public Set<QName> getHeaders() {
        String uri = "http://docs.oasis-open.org/wss/2004/01/" +
            "oasis-200401-wss-wssecurity-secext-1.0.xsd";
        QName header = new QName(uri, "Security", "wsse");
        HashSet headers = new HashSet<QName>();
        headers.add(header); 
        return headers; 
    }

    public boolean handleMessage(SOAPMessageContext context) {
        Boolean out = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY); 
        SOAPMessage msg = context.getMessage(); 
        
        if(out.booleanValue()){
            //create a message that can be validated 
            ProcessingContext pCxt = null; 
            try{
                pCxt = processor.createProcessingContext(msg); 
                pCxt.setSOAPMessage(msg);
                SOAPMessage secured = processor.secureOutboundMessage(pCxt); 
                context.setMessage(secured);
                dumpMessage("Outgoing message: ", secured); 
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

    private void dumpMessage(String msg, SOAPMessage soap) {
        try{
            System.out.println(msg);
            soap.writeTo(System.out);
            System.out.println("");
        }catch(IOException ex){
            System.err.println(ex);
        }catch(SOAPException ex){
            System.err.println(ex);
        }
    }
    
}
