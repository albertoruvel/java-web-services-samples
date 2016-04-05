/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client;

import com.ws.samples.client.echo.Echo;
import com.ws.samples.client.echo.EchoService;
import com.ws.samples.util.MapDump;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author jose.rubalcaba
 */
public class EchoClient {
    public static void main(String[] args) {
        EchoService service = new EchoService();
        Echo echo = service.getEchoPort();          //USED wsimport GENERATED CLIENT
        Map<String, Object> mapCxt = ((BindingProvider)echo).getRequestContext(); 
        /**
         * Sample invocation 
         * java EchoClient http://localhost:8080 echo 
         * 1st command line arg ends with service location port number 
         * 2nd command line arg is the service operation 
         */
        if(args.length >= 2){
            //endpoint address becomes http://localhost:8080/echo
            mapCxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, args[0] + "/" + args[1]);  //dynamically setting the service url 
            //SOAP action becomes "echo"
            mapCxt.put(BindingProvider.SOAPACTION_URI_PROPERTY, args[1]); 
        }
        
        //add some application-specific HTTP headers 
        Map<String, List<String>> headers = new HashMap<String, List<String>>(); 
        headers.put("Accept-Encoding", Collections.singletonList("gzip")); 
        headers.put("Greeting", Collections.singletonList("Greetings, human!")); 
        //insert customized header into HTTP headers 
        mapCxt.put(MessageContext.HTTP_REQUEST_HEADERS, headers); 
        
        MapDump.mapDump(mapCxt, "");
        System.out.println("\n\nRequest above, Response below \n\n");
        
        //invoke service operation to generate HTTP response 
        String response = echo.echo("Have a nice day :)");
        Map<String, Object> resp = ((BindingProvider)echo).getResponseContext(); 
        //print the map 
        MapDump.mapDump(resp, "");
        Object responseCode = mapCxt.get(MessageContext.HTTP_RESPONSE_HEADERS); 
    }
}
