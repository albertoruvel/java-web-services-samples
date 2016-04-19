/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.publish.https;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.http.HTTPException;

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
        HttpsServer server = null; 
        try{
            InetSocketAddress inet = new InetSocketAddress(port); 
            server = HttpsServer.create(inet, 5); //2nd arg number of client requests to queue
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
            final SSLEngine engine = cxt.createSSLEngine(); 
            server.setHttpsConfigurator(new HttpsConfigurator(cxt){
                public void configure(HttpsParameters params){
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols()); 
                }
            });
            server.setExecutor(null); //default
            HttpContext httpCxt = server.createContext(path, new MyHttpHandler(this)); 
            
        }catch(CertificateException ex){
            System.err.println(ex);
        }catch(IOException ex){
            System.err.println(ex);
        }catch(KeyManagementException ex){
            System.err.println(ex);
        }catch(KeyStoreException ex){
            System.err.println(ex);
        }catch(NoSuchAlgorithmException ex){
            System.err.println(ex);
        }catch(UnrecoverableKeyException ex){
            System.err.println(ex);
        }
        return server; 
    }
    
    class MyHttpHandler implements HttpHandler{

        private HttpsRabbitCounterPublisher pub; 
        private MyHttpHandler(HttpsRabbitCounterPublisher pub){ this.pub = pub; }
        
        public void handle(HttpExchange he) throws IOException {
            authenticate(he); 
            String verb = he.getRequestMethod().toUpperCase(); 
            if(verb.equals("GET"))doGet(he); 
            else if(verb.equals("POST"))doPost(he);
            else if(verb.equals("DELETE"))doDelete(he); 
            else throw new HTTPException(405); //method not allowed
        }

        private void doGet(HttpExchange he) {
            Map<Integer, Integer> fibs = pub.getFib();
            Collection<Integer> list = fib.values();
            responseToClient(he, list.toString());
        }

        private void doPost(HttpExchange he) {
            Map<Integer, Integer> fibs = pub.getFib(); 
            fibs.clear(); 
            try{
                InputStream in = he.getRequestBody(); 
                byte[] raw = new byte[4096];
                in.read(raw);
                String nums = new String(raw); 
                nums = nums.replace('[', '\0');
                nums = nums.replace(']', '\0');
                String[] parts = nums.split(","); 
                List<Integer> list = new ArrayList<Integer>(); 
                for(String next : parts){
                    int n = Integer.parseInt(next.trim()); 
                    fibs.put(n, countRabbits(n));
                    list.add(fibs.get(n));
                }
                Collection<Integer> col = fibs.values(); 
                String res = "POSTed: " + fibs.toString(); 
                responseToClient(he, res);
            }catch(IOException ex){
                System.err.println(ex);
            }
        }

        private void doDelete(HttpExchange he) {
            Map<Integer, Integer> fibs = pub.getFib(); 
            fibs.clear(); 
            responseToClient(he, "All entries delete");
        }
        
        private void responseToClient(HttpExchange he, String res){
            try{
                he.sendResponseHeaders(200, 0); //means: arbitrarily many bytes
                OutputStream out = he.getResponseBody(); 
                out.write(res.getBytes());
                out.flush();
                he.close(); // close all streams
            }catch(IOException ex){
                System.err.println(ex);
            }
        }

        private int countRabbits(int n) {
            n = Math.abs(n);
            if (n < 2) {
                return n; // easy cases
            }
            Map<Integer, Integer> fibs = pub.getFib();
            // Return cached values if present.
            if (fibs.containsKey(n)) {
                return fibs.get(n);
            }
            if (fibs.containsKey(n - 1)
                    && fibs.containsKey(n - 2)) {
                fibs.put(n, fibs.get(n - 1) + fibs.get(n - 2));
                return fibs.get(n);
            }
            // Otherwise, compute from scratch, cache, and return.
            int fib = 1, prev = 0;
            for (int i = 2; i <= n; i++) {
                int temp = fib;
                fib += prev;
                prev = temp;
            }
            fibs.put(n, fib);
            return fib;
        }

        private void authenticate(HttpExchange he) {
            //extract header entries 
            Headers headers = he.getRequestHeaders(); 
            List<String> ulist = headers.get(BindingProvider.USERNAME_PROPERTY); 
            List<String> plist = headers.get(BindingProvider.PASSWORD_PROPERTY); 
            
            //extract username/password from the two singleton lists
            String username = ulist.get(0); 
            String password = plist.get(0);
            if(!username.equals("alberto") || !password.equals("rubalcaba"))
                throw new HTTPException(401);
        }
        
    }
}
