/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sib;

import com.ws.samples.util.MapDump;
import java.util.Map;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author jose.rubalcaba
 */
@WebService
//@HandlerChain(file="handler-chain.xml")
public class Echo {
    
    //enable CDI on the service class 
    @Resource
    private WebServiceContext webServiceContext; 
    
    @WebMethod
    public String echo(String fromClient){
        String response = "Echoeing user message: " + fromClient; 
        return response; 
    }
}
