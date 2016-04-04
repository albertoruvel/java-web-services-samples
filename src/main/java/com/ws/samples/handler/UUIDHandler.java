/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author jose.rubalcaba
 */
public class UUIDHandler implements SOAPHandler<SOAPMessageContext>{

    private final Logger logger = Logger.getLogger("UUID Handler");
    private boolean log = true;

    public UUIDHandler() {
    }
    
    
    
    public Set<QName> getHeaders() {
        logger.info("getHeaders");
        return null; 
    }

    public boolean handleMessage(SOAPMessageContext context) {
        if(log)logger.info("handleMessage");
        //is this an outbound message? i.e. a request? 
        Boolean request = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        //manipulate the SOAP only if the request is outbound 
        if(request){
            //generates a UUID and a timestamp to inject to the headers 
            UUID uuid = UUID.randomUUID(); 
            SOAPMessage message = null; 
            SOAPEnvelope env = null; 
            SOAPHeader header = null; 
            try{
                message = context.getMessage(); 
                env = message.getSOAPPart().getEnvelope(); 
                header = env.getHeader(); 
                //ensure that the soap message has a header 
                if(header == null) header = env.addHeader(); 
                QName qname = new QName("http://ch03.fib", "uuid"); 
                SOAPHeaderElement element = header.addHeaderElement(qname); 
                element.setActor(SOAPConstants.URI_SOAP_ACTOR_NEXT); //this is default 
                element.addTextNode(uuid.toString()); 
                message.saveChanges();
                message.writeTo(System.out); 
                message.writeTo(new FileOutputStream(new File("output-message.xml")));
            }catch(SOAPException ex){
                logger.severe("SOAPException: " + ex.getMessage()); 
            }catch(IOException ex){
                logger.severe("IOException: " + ex.getMessage()); 
            }
        }
        return true; //continue on the handler chain system
    }

    public boolean handleFault(SOAPMessageContext context) {
        logger.info("handleFault");
        try{
            context.getMessage().writeTo(System.out);
            context.getMessage().writeTo(new FileOutputStream(new File("output-fault-message.xml")));
        }catch(SOAPException ex){
                logger.severe("SOAPException: " + ex.getMessage()); 
            }catch(IOException ex){
                logger.severe("IOException: " + ex.getMessage()); 
            }
        return true; 
    }

    public void close(MessageContext context) {
        logger.info("close");
    }
    
}
