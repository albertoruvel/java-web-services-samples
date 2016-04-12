/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client.dispatch;

import com.ws.samples.dispatch.NSResolver;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 *
 * @author jose.rubalcaba
 */
public class DispatchClient {
    public static void main(String[] args) {
        new DispatchClient().setupAndTest(); 
    }

    private void setupAndTest() {
        URI nsUri = null; 
        try{
            nsUri = new URI("urn:fib"); 
        }catch(URISyntaxException ex){
            System.err.println(ex);
        }
        QName serviceName = new QName("rcService", nsUri.toString());
        QName port = new QName("rcPort", nsUri.toString());
        String endpoint = "http://localhost:8080/fib";
        
        //now create a service proxy or dispatcher
        Service service = Service.create(serviceName); 
        service.addPort(port, HTTPBinding.HTTP_BINDING, endpoint);
        Dispatch<Source> dispatch = service.createDispatch(port, Source.class, 
                Service.Mode.PAYLOAD); 
        
        //send some requests
        String xmlStart = "<fib:request xmlns:fib='urn:fib'>"; 
        String xmlEnd = "</fib:request>";
        
        //to begin, a POST to create a fibonacci numbers
        List<Integer> nums = new ArrayList<Integer>(); 
        for (int i = 0; i < 12; i++) nums.add(i + 1); 
        String xml = xmlStart + nums.toString() + xmlEnd; 
        invoke(dispatch, "POST", nsUri.toString(), xml);
        
        //GET request to test whether the POST worked 
        invoke(dispatch, "GET", nsUri.toString(), null); 
        
        //DELETE request to remove the list
        invoke(dispatch, "DELETE", nsUri.toString(), null);
        
        //GET to test whether the DELETE worked 
        invoke(dispatch, "GET", nsUri.toString(), null); 
        
        //POST to repopulate and a final GET to confirm 
        nums = new ArrayList<Integer>(); 
        for (int i = 0; i < 24; i++) nums.add(i + 1); 
        xml = xmlStart + nums.toString() + xmlEnd;
        invoke(dispatch, "POST", nsUri.toString(), xml); 
        invoke(dispatch, "GET", nsUri.toString(), null); 
    }

    private void invoke(Dispatch<Source> dispatch, String verb, String uri, Object data) {
        Map<String, Object> requestContext = dispatch.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, verb); 
        System.out.println("Request: " + data);
        
        //invoke
        StreamSource source = null; 
        if(data != null)source = makeStreamSource(data.toString());
        Source result = dispatch.invoke(source); 
        displayResult(result, uri); 
    }

    private StreamSource makeStreamSource(String toString) {
        ByteArrayInputStream bais = new ByteArrayInputStream(toString.getBytes()); 
        return new StreamSource(bais);
    }

    private void displayResult(Source result, String uri) {
        DOMResult domResult = new DOMResult(); 
        try{
            Transformer trans = TransformerFactory.newInstance().newTransformer(); 
            trans.transform(result, domResult);
            XPath path = XPathFactory.newInstance().newXPath(); 
            path.setNamespaceContext(new NSResolver("fib", uri));
            String resultString = path.evaluate("/fib:response", domResult.getNode()); 
            System.out.println(resultString);
        }catch(TransformerException ex){
            System.err.println(ex);
        }catch(TransformerFactoryConfigurationError ex){
            System.err.println(ex);
        }catch(XPathExpressionException ex){
            System.err.println(ex);
        }
    }
}
