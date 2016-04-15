/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client.https;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 *
 * @author MACARENA
 */
public class SunTrustingClient {
    
    private static final String URL = "https://sun.java.com:443"; 
    
    public static void main(String[] args) {
        new SunTrustingClient().doIt(); 
    }

    private void doIt() {
        try{
            SSLContext cxt = SSLContext.getInstance("SSL"); 
            TrustManager[] mgr = getTrustManager(); 
            cxt.init(null,              //key manager
                    mgr,                //trust manager
                    new SecureRandom());//random number generator
            
            HttpsURLConnection.setDefaultSSLSocketFactory(cxt.getSocketFactory());
            URL url = new URL(URL);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection(); 
            
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            dumpFeatures(conn); 
            
        }catch(KeyManagementException ex){
            System.err.println(ex);
        }catch(NoSuchAlgorithmException ex){
            System.err.println(ex);
        }catch(MalformedURLException ex){
            System.err.println(ex);
        }catch(IOException ex){
            System.err.println(ex);
        }
    }

    private TrustManager[] getTrustManager() {
        TrustManager[] mgr = new TrustManager[]{
            new X509TrustManager(){
                
                @Override
                public X509Certificate[] getAcceptedIssuers(){
                    return null;
                }
                
                @Override
                public void checkClientTrusted(X509Certificate[] c, String s){
                    
                }
                
                @Override
                public void checkServerTrusted(X509Certificate[] c, String s){
                    
                }
            }
        };
        return mgr; 
    }

    private void dumpFeatures(HttpsURLConnection conn) {
        try{
            print("Status code: " + conn.getResponseCode()); 
            print("Cipher suite: " + conn.getCipherSuite()); 
            Certificate[] certs = conn.getServerCertificates(); 
            for (Certificate cert : certs) {
                print("\tCert Type: " + cert.getType()); 
                print("\tHash code: " + cert.hashCode()); 
                print("\tAlgorithm: " + cert.getPublicKey().getAlgorithm()); 
                print("\tFormat: " + cert.getPublicKey().getFormat()); 
                print(""); 
            }
        }catch(IOException ex){
            System.err.println(ex);
        }
    }

    private void print(String string) {
        System.out.println(string);
    }
}   
