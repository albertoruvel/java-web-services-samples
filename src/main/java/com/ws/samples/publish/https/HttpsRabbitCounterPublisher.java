/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.publish.https;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author MACARENA
 */
public class HttpsRabbitCounterPublisher {
    
    private Map<Integer, Integer> fib; 

    public HttpsRabbitCounterPublisher() {
        fib = Collections.synchronizedMap(new HashMap<Integer, Integer>()); 
    }

    public Map<Integer, Integer> getFib() {
        return fib;
    }
    
    
    public static void main(String[] args) {
        new HttpsRabbitCounterPublisher().publish(); 
    }

    private void publish() {
        int port = 9090; 
        String ip = "https://localhost"; 
        String path = "/fib";
        String url = ip + ":" + port + path; 
        HttpServer server = getHttpServer(ip, port, path); 
        HttpContext cxt = server.createContext(path); 
        
        System.out.println("Publishing RabbitCounter at " + url);
        if(server != null)server.start();
        else System.err.println("Failed to start server");
        
    }

    private HttpServer getHttpServer(String ip, int port, String path) {
        HttpServer server = null; 
        try{
            InetSocketAddress inet = new InetSocketAddress(port); 
            server = HttpServer.create(inet, 5); //2nd arg number of client requests to queue
            SSLContext cxt = SSLContext.getInstance("TLS"); 
            //password for keystore 
            KeyStore ks = KeyStore.getInstance("JKS"); 
            FileInputStream stream = new FileInputStream("rc.keystore"); 
            char[] password = "rubalcaba".toCharArray(); 
            ks.load(stream, password);
            KeyManagerFactory factory = KeyManagerFactory.getInstance("sunX509"); 
            factory.init(ks, password);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunX509"); 
            tmf.init(ks);
            cxt.init(factory.getKeyManagers(), tmf.getTrustManagers(), null);
            
            //create ssl engine and configure https to use it 
            SSLEngine engine = cxt.createSSLEngine(); 
            server.
            
        }
    }
}
