/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client;

import com.ws.samples.client.rabbit.FibException_Exception;
import com.ws.samples.client.rabbit.RabbitCounter;
import com.ws.samples.client.rabbit.RabbitCounterService;
import com.ws.samples.handler.UUIDHandler;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

/**
 *
 * @author jose.rubalcaba
 */
public class RabbitCounterClient {
    public static void main(String[] args) {
        RabbitCounterService service = new RabbitCounterService();
        //add a handler resolver
        service.setHandlerResolver(new ClientHandlerResolver());
        RabbitCounter counter = service.getRabbitCounterPort(); 
        
        try{
            int n = -100; 
            System.out.println("Fib(" + n + ") = " + counter.countRabbits(n));
        }catch(FibException_Exception ex){
            System.err.println("Error: " + ex.getMessage());
        }
    }
    
    static class ClientHandlerResolver implements HandlerResolver{

        public List<Handler> getHandlerChain(PortInfo portInfo) {
            List<Handler> chain = new ArrayList<Handler>(); 
            chain.add(new UUIDHandler()); // add the handler created for uuid testing
            return chain; 
        }
         
    }
}
