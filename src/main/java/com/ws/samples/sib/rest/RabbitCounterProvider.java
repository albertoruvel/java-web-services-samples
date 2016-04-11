/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sib.rest;

import com.ws.samples.dispatch.NSResolver;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 *
 * @author jose.rubalcaba
 */
@WebServiceProvider
@BindingType(value = HTTPBinding.HTTP_BINDING)
public class RabbitCounterProvider implements Provider<Source>{

    @Resource
    private WebServiceContext wsCxt; 
    
    //map to save computed values
    private Map<Integer, Integer> cache = 
            Collections.synchronizedMap(new HashMap<Integer, Integer>());
     
    private static final String xmlStart = "<fib:response xmlns:fib = 'urn:fib'>"; 
    private static final String xmlStop = "</fib:response>"; 
    private static final String nsUri = "urn:fib"; 
    
    public Source invoke(Source request) {
        if(wsCxt == null)throw new RuntimeException("DI failed on web service context");
        //grab the message context and request verb
        String verb = ((String)wsCxt.getMessageContext().get(MessageContext.HTTP_REQUEST_METHOD))
                .trim().toUpperCase(); 
        if(verb.equals("GET")) return doGet(); 
        else if(verb.equals("DELETE")) return doDelete(); 
        else if(verb.equals("POST")) return doPost(request);
        else throw new HTTPException(405); // bad request
    }

    private Source doGet() {
        Collection<Integer> cacheValues = cache.values(); 
        String xml = xmlStart + " GET: " + cacheValues.toString() + xmlStop; 
        return makeStreamSource(xml); 
    }

    private Source doDelete() {
        cache.clear();
        String xml = xmlStart + " MAP cleared" + xmlStop; 
        return makeStreamSource(xml); 
    }

    private Source doPost(Source request) {
        if(request == null)throw new HTTPException(400); //bad request
        String nums = extractRequest(request); 
        //extract integers from a string 
        nums = nums.replace("[", "\0"); 
        nums = nums.replace("]", "\0"); 
        
        String[] parts = nums.split(","); 
        List<Integer> list = new ArrayList<Integer>(); 
        for (String next : parts) {
            int n = Integer.parseInt(next.trim()); 
            cache.put(n, countRabbits(n)); 
            list.add(cache.get(n)); 
        }
        
        String xml = xmlStart + " POSTed: " + list.toString() + xmlStop; 
        return makeStreamSource(xml); 
    }

    private String extractRequest(Source request) {
        String requestString = null; 
        try{
            DOMResult dom = new DOMResult(); 
            Transformer trans = TransformerFactory.newInstance().newTransformer(); 
            trans.transform(request, dom);
            XPathFactory factory = XPathFactory.newInstance(); 
            XPath path= factory.newXPath(); 
            path.setNamespaceContext(new NSResolver("fib", nsUri));
            requestString = path.evaluate("/fib:request", dom.getNode()); 
            
        }catch(TransformerException ex){
            
        }catch(TransformerFactoryConfigurationError ex){
            
        }catch(XPathExpressionException ex){
            
        }
        return requestString; 
    }

    private Integer countRabbits(int n) {
        if (n < 0) {
            throw new HTTPException(403); // forbidden
        }// Easy cases.
        if (n < 2) {
            return n;
        }
        // Return cached values if present.
        if (cache.containsKey(n)) {
            return cache.get(n);
        }
        if (cache.containsKey(n - 1) && cache.containsKey(n - 2)) {
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

    private Source makeStreamSource(String xml) {
        ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes()); 
        return new StreamSource(stream); 
    }
    
}
