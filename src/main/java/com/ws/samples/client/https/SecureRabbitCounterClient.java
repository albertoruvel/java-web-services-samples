/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client.https;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author jose.rubalcaba
 */
public class SecureRabbitCounterClient {
    private static final String URL = "https://localhost:8080/fib"; 
    
    public static void main(String[] args) {
        new SecureRabbitCounterClient().doIt(); 
    }

    private void doIt() {
        try{
            SSLContext cxt = SSLContext.getInstance("TLS"); 
            TrustManager[] mgrs = getTrustManagers(); 
            cxt.init(null, //key manager  
                    mgrs,  //trsut managers
                    null); //use default secure random generator
            HttpsURLConnection.setDefaultSSLSocketFactory(cxt.getSocketFactory());
            URL url = new URL(URL); 
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection(); 
            conn.setRequestProperty(BindingProvider.USERNAME_PROPERTY, "alberto");
            conn.setRequestProperty(BindingProvider.PASSWORD_PROPERTY, "rubalcaba");
            //guard against "bad hostname" errors during handshake
            conn.setHostnameVerifier(new HostnameVerifier(){
                @Override
                public boolean verify(String host, SSLSession session){
                    if(host.equals("localhost"))return true; 
                    return false; 
                }
            });
            
            //test request 
            List<Integer> list = new ArrayList<Integer>(); 
            list.add(3);list.add(5); list.add(7); 
            
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.connect(); 
            OutputStream out = conn.getOutputStream(); 
            out.write(list.toString().getBytes());
            byte[] buff = new byte[4096];
            InputStream in = conn.getInputStream(); 
            in.read(buff);
            System.out.println(new String(buff));
            dumpFeatures(conn);
            conn.disconnect();
        }catch(IOException ex){
            System.err.println(ex);
        }catch(KeyManagementException ex){
            System.err.println(ex);
        }catch(NoSuchAlgorithmException ex){
            System.err.println(ex);
        }
    }

    private TrustManager[] getTrustManagers() {
        TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null; 
                }

            }
        };
        
        return certs; 
    }

    private void dumpFeatures(HttpsURLConnection conn) {
        try{
            print("Status code: " + conn.getResponseCode());
            print("Cipher suite: " + conn.getCipherSuite());
            Certificate[] certs = conn.getServerCertificates();
            for (Certificate cert : certs) {
                print("\tCert. type: " + cert.getType());
                print("\tHash code: " + cert.hashCode());
                print("\tAlgorithm: " + cert.getPublicKey().getAlgorithm());
                print("\tFormat: " + cert.getPublicKey().getFormat());
                print("");
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    public void print(String s){
        System.out.println(s);
    }
}
