/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client.restlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Protocol;

/**
 *
 * @author jose.rubalcaba
 */
public class FibRestletClient {
    public static void main(String[] args) {
        new FibRestletClient().sendRequests(); 
    }

    private void sendRequests() {
        try{
            //setup the request
            Request request = new Request(); 
            request.setResourceRef("http://localhost:7777/fib");
            
            //to begin, a POST to create some service data 
            List<Integer> nums = new ArrayList<Integer>(); 
            for(int i = 0; i < 12; i ++)nums.add(i); 
            
            Form form = new Form(); 
            form.add("nums", nums.toString()); 
            request.setMethod(Method.POST);
            request.setEntity(form.getWebRepresentation());
            
            //generate a client and make the call 
            Client client = new Client(Protocol.HTTP);
            
            //POST request 
            Response response = getResponse(client, request);
            dump(response);
            
            //GET request to confirm POST
            request.setMethod(Method.GET); 
            request.setEntity(null);
            response = getResponse(client, request);
            dump(response);
            
            //DELETE request
            request.setMethod(Method.DELETE);
            request.setEntity(null);
            response = getResponse(client, request);
            dump(response);
            
            //GET request to confirm DELETE
            request.setMethod(Method.GET);
            request.setEntity(null);
            response = getResponse(client, request);
            dump(response);
        }catch(Exception ex){
            System.err.println(ex);
        }
    }

    private Response getResponse(Client client, Request request) {
        return client.handle(request); 
    }

    private void dump(Response response) {
        try{
            if(response.getStatus().isSuccess())
                response.getEntity().write(System.out);
            else System.err.println(response.getStatus().getDescription());
        }catch(IOException ex){
            System.err.println(ex);
        }
    }
}
