/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.publish.rest;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author jose.rubalcaba
 */
public class JerseyPublisher {
    public static void main(String[] args) {
        final String url = "http://localhost:8080/";
        final Map<String, String> config = new HashMap<String, String>();
        config.put("com.sun.jersey.config.property.packages", "com.ws.samples.rest"); //package with resource classes
        System.out.println("Grizzly starting on port 8080\nKill with Ctrl+C");
        try{
            //SelectorThread th = GrizzlyWebContainerFactory.create(url);
            SelectorThread th = GrizzlyWebContainerFactory.create(url, config); 
        } catch(Exception ex){
            System.err.println(ex);
        }
    }
}
