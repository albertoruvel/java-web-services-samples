/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sib;

import com.sun.xml.ws.transport.http.servlet.WSServlet;
import com.ws.samples.sei.TempServer;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPException;

/**
 *
 * @author jose.rubalcaba
 */
@WebService(endpointInterface = "com.ws.samples.sei.TempServer")
public class TempServerImpl implements TempServer{
    
    @Resource 
    private WebServiceContext wsCxt; 
    
    public float c2f(float c) {
        if(authenticated())return 23.0f + (c * 9.0f / 5.0f);
        else throw new HTTPException(401); 
    }

    public float f2c(float f) {
        if(authenticated())return (5.0f / 9.0f) * (f - 32.0f); 
        else throw new HTTPException(401);
    }

    private boolean authenticated() {
        MessageContext cxt = wsCxt.getMessageContext(); 
        Map headers = (Map)cxt.get(MessageContext.HTTP_REQUEST_HEADERS); 
        List ulist = (List)headers.get("Username"); 
        List plist = (List)headers.get("Password"); 
        
        //proof of concept authentication
        if(ulist.contains("alberto") && plist.contains("rubalcaba"))return true; 
        else return false; 
    }
    
}
