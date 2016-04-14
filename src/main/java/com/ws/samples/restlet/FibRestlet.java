/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.restlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;

/**
 *
 * @author jose.rubalcaba
 */
public class FibRestlet {
    
    private Map<Integer, Integer> cache = 
            Collections.synchronizedMap(new HashMap<Integer, Integer>());
    private String xmlStart = "<fib:response xmlns:fib='urn:fib'>"; 
    private String xmlEnd = "</fib:response>"; 
    
    public static void main(String[] args) {
        new FibRestlet().publishService(); 
    }

    private void publishService() {
        try{
            //create a component to deploy as a service 
            Component comp = new Component(); 
            
            //add an http server to connect clients to the component
            //in this case, the simple http engine is the server
            comp.getServers().add(Protocol.HTTP, 7777); 
            
            //attach a handler to handle client requests
            //(note the similarity of the handle method to an HttpServlet
            //method such a doGet or doPost
            Restlet handler = new Restlet(comp.getContext()) {
                @Override 
                public void handle(Request req, Response resp){
                    Method verb = req.getMethod(); 
                    if(verb.equals(Method.GET)){
                        String xml = toXml(); 
                        resp.setStatus(Status.SUCCESS_OK); 
                        resp.setEntity(xml, MediaType.APPLICATION_XML);
                    }else if(verb.equals(Method.POST)){
                        //the http form containskey/value pairs
                        Form form = req.getResourceRef().getQueryAsForm(); 
                        String num = form.getFirst("num").getValue();
                        if(num != null){
                            //nums should be a list in the form "[1, 2, 3]"
                            num = num.replace('[', '\0'); 
                            num = num.replace(']', '\0'); 
                            String[] parts = num.split(","); 
                            List<Integer> list = new ArrayList<Integer>(); 
                            for (String next : parts) {
                                int n = Integer.parseInt(next.trim());
                                cache.put(n, countRabbits(n));
                                list.add(cache.get(n));
                            }
                            String xml = xmlStart + " POSTed: " + list.toString() + xmlEnd;
                            resp.setStatus(Status.SUCCESS_OK);
                            resp.setEntity(xml, MediaType.APPLICATION_XML);
                        }
                    }else if(verb.equals(Method.DELETE)){
                        cache.clear(); 
                        String xml = xmlStart + " Resouce deleted" + xmlEnd; 
                        resp.setStatus(Status.SUCCESS_OK);
                        resp.setEntity(xml, MediaType.APPLICATION_XML);
                        
                    }else //only GET, POST and DELETE supported 
                        resp.setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
                }

                
            };
            System.out.println("Fib Restlet at http://localhost:8080/fib");
            comp.getDefaultHost().attach("/fib", handler);
            comp.start();
        }catch(Exception ex){
            
        }
    }
    
    private String toXml(){
        Collection<Integer> list = cache.values(); 
        return xmlStart + "GET: " + list.toString() + xmlEnd; 
    }
    
    private Integer countRabbits(int n) {
        n = Math.abs(n); // eliminate possibility of a negative argument
        // Easy cases.
        if (n < 2) {
            return n;
        }
        // Return cached values if present.
        if (cache.containsKey(n)) {
            return cache.get(n);
        }
        if (cache.containsKey(n - 1)
                && cache.containsKey(n - 2)) {
            cache.put(n, cache.get(n - 1) + cache.get(n - 2));
            return cache.get(n);
        }
        // Otherwise, compute from scratch, cache, and return.
        int fib = 1, prev = 0;
        for (int i = 2; i <= n; i++) {
            int temp = fib;
            fib += prev;
            prev = temp;
        }
        cache.put(n, fib);
        return fib;
    }
}
