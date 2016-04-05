/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.handler;

import com.ws.samples.util.MapDump;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author jose.rubalcaba
 */
public class EchoHandler implements SOAPHandler<SOAPMessageContext>{

    public EchoHandler() {
    }

    
    public Set<QName> getHeaders() {
        return null; 
    }

    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY); 
        if(! outbound)
            MapDump.mapDump((Map)context, "");
        return true; //continue on handler chain
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true; 
    }

    public void close(MessageContext context) {
        
    }
    
}
