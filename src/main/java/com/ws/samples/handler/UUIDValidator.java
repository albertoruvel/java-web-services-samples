/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.handler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

/**
 *
 * @author jose.rubalcaba
 */
public class UUIDValidator implements SOAPHandler<SOAPMessageContext>{

    private final Logger logger = Logger.getLogger("UUIDValidator(ServerSide)"); 
    private static final int UUIDvariant = 2; // layout
    private static final int UUIDversion = 4; // version
    
    public UUIDValidator() {
    }

    
    public Set<QName> getHeaders() {
        return null; 
        
    }

    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY); 
        //handle the response only if is incoming 
        if(!outbound){
            SOAPMessage message = null; 
            SOAPEnvelope env = null; 
            SOAPHeader header = null; 
            try{
                message = context.getMessage(); 
                env = message.getSOAPPart().getEnvelope();
                header = env.getHeader(); 
                
                if(header == null)
                    generateSOAPFault(message, "No message header found.."); 
                //get UUID value from header block if it's there 
                Iterator iterator = header.extractHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT); 
                if(iterator == null || !iterator.hasNext())
                    generateSOAPFault(message, "No header block found for next actor");
                Node next = (Node)iterator.next(); 
                String value = next == null ? null : next.getValue(); 
                if(value == null)
                    generateSOAPFault(message, "No UUID in header block"); 
                //reconstruct a UUID object to check some properties 
                UUID uuid = UUID.fromString(value.trim()); 
                if(uuid.variant() != UUIDvariant ||
                        uuid.version() != UUIDversion)
                    generateSOAPFault(message, "Bad UUID on header block"); 
                message.writeTo(System.out);
            }catch(SOAPException ex){
                logger.severe("Exception: " + ex.getMessage());
            }catch(IOException ex){
                logger.severe("Exception: " + ex.getMessage());
            }
        }
        return true; //continue with handler chain 
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true; 
    }

    public void close(MessageContext context) {
        
    }

    private void generateSOAPFault(SOAPMessage message, String string) {
        SOAPBody body = null; 
        SOAPFault fault = null; 
        try{
            body = message.getSOAPPart().getEnvelope().getBody(); 
            fault = body.addFault(); 
            fault.setFaultString(string);
            
            //wrapper for SOAP 1.1 or 1.2 fault
            throw new SOAPFaultException(fault); 
        }catch(SOAPException ex){
            logger.severe("Exception: " + ex.getMessage());
        }
    }
    
}
