/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.publish;

import com.ws.samples.handler.EchoSecurityHandler;
import com.ws.samples.sib.Echo;
import java.util.LinkedList;
import java.util.List;
import javax.xml.ws.Binding;
import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;

/**
 *
 * @author jose.rubalcaba
 */
public class EchoPublisher {
    public static void main(String[] args) {
        Endpoint end = Endpoint.create(new Echo()); 
        Binding binding = end.getBinding(); 
        List<Handler> hchain = new LinkedList<Handler>(); 
        hchain.add(new EchoSecurityHandler()); 
        binding.setHandlerChain(hchain);
        end.publish("http://localhost:7777/echo");
        System.out.println("Published to http://localhost:7777/echo");
    }
}
