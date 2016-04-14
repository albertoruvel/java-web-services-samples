/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.rest;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author jose.rubalcaba
 */
@Path("/")
public class MessageResource {
    private static  String message = "Hello, Jersey";
    
    @GET
    @Produces("text/plain")
    public String read(){
        return message + "\n";
    }
    
    @GET
    @Produces("text/plain")
    @Path("{extra}")
    private String personalizedRead(@FormParam("msg")String msg){
        this.message = msg;
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        XMLEncoder enc = new XMLEncoder(bais); 
        enc.writeObject(msg);
        enc.close();
        return new String(bais.toByteArray()) + "\n";
    }
    
    @DELETE
    @Produces("text/plain")
    public String delete(){
        this.message = null; 
        return "Message deleted";
    }
}
