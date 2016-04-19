/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sib.rest;

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding; 
import javax.xml.ws.http.HTTPException;

/**
 *
 * @author jose.rubalcaba
 */
@WebServiceProvider
@BindingType(value = HTTPBinding.HTTP_BINDING)
public class TempConvertRestful implements Provider<Source>{

    @Resource
    private WebServiceContext cxt; 
    
    /**
     * The service must be runned at tomcat container, using 
     * org.apache.catalina.realm.RealmBase to authenticate with the service, adding a user and role to tomcat-users.xml file
     * @param request
     * @return 
     */
    public Source invoke(Source request) {
        //grab the message context and extract request verb 
        String verb = ((String)cxt.getMessageContext().get(MessageContext.HTTP_REQUEST_METHOD)
                ).trim().toUpperCase(); 
        if(verb.equals("GET")) return doGet(400);
        else throw new HTTPException(405);
    }

    private Source doGet(int i) {
        String query = (String)cxt.getMessageContext().get(MessageContext.QUERY_STRING); 
        if(query == null)throw new HTTPException(400); //bad request
        String temp = getValueFromQuery("temp", query); 
        if(temp == null)throw new HTTPException(400);
        
        List<String> converts = new ArrayList<String>(); 
        try{
            float f = Float.parseFloat(temp.trim()); 
            float f2c = f2c(f);
            float c2f = c2f(f);
            converts.add("f2c: " + f2c);
            converts.add("c2f: " + c2f);
        }catch(NumberFormatException ex){
            System.err.println(ex);
            throw new HTTPException(400);
        }
        
        //generate XML and return 
        ByteArrayInputStream in = encodeToStream(converts);
        return new StreamSource(in);
    }

    private String getValueFromQuery(String temp, String query) {
        String[] parts = query.split("="); 
        if(!parts[0].equalsIgnoreCase(temp))
            throw new HTTPException(400);
        return parts[1].trim(); 
    }

    private float c2f(float f) {
        return 32.0f + (f * 9.0f / 5.0f); 
    }

    private float f2c(float f) {
        return (5.0f / 9.0f) * (f - 32.0f); 
    }

    private ByteArrayInputStream encodeToStream(Object obj) {
        //serialize object to XML and return 
        ByteArrayOutputStream out = new ByteArrayOutputStream(); 
        XMLEncoder enc = new XMLEncoder(out);
        enc.writeObject(obj);
        enc.close(); 
        return new ByteArrayInputStream(out.toByteArray()); 
    }
    
}
