/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.handler;

import com.ws.samples.client.rabbit.CountRabbits;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author jose.rubalcaba
 */
public class ArgHandler implements LogicalHandler<LogicalMessageContext>{
    
    private final Logger logger = Logger.getLogger("ArgHandler"); 

    public ArgHandler() {
    }
    
    public boolean handleMessage(LogicalMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {
            logger.info("ArgHandler.handleMessage");
            LogicalMessage message = context.getMessage();

            try {
                JAXBContext jaxCxt = JAXBContext.newInstance("com.ws.samples");
                Object payload = message.getPayload(jaxCxt);
                if (payload instanceof JAXBElement) {
                    Object obj = ((JAXBElement) payload).getValue();
                    CountRabbits cRabbits = (CountRabbits) obj;
                    int n = cRabbits.getArg0(); //current value 
                    if (n < 0) {//negative argument
                        cRabbits.setArg0(Math.abs(n)); //make a non negative number
                        //update message 
                        ((JAXBElement)payload).setValue(cRabbits);
                        message.setPayload(payload, jaxCxt);
                    }
                }
            }catch(JAXBException ex){
                
            }
            
        }
        return true;
    }

    public boolean handleFault(LogicalMessageContext context) {
        logger.info("ArgHandler.handleFault");
        return true; 
    }

    public void close(MessageContext context) {
        logger.info("ArgHandler.close"); 
    }
    
}
