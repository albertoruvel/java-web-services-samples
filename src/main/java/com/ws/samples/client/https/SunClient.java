/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client.https;

import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author jose.rubalcaba
 */
public class SunClient {
    private static final String URL = "https://java.sun.com:443"; 
    public static void main(String[] args) {
        new SunClient().doIt(); 
    }

    private void doIt() {
        try{
            URL url = new URL(URL);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.connect();
            dumpFeatures(conn);
            
        }catch(IOException ex){
            System.err.println(ex);
        }
    }

    private void dumpFeatures(HttpsURLConnection conn) {
        try{
            print("Status code: " + conn.getResponseCode()); 
            print("Cipher suite: " + conn.getCipherSuite()); 
            Certificate[] certs = conn.getServerCertificates(); 
            for(Certificate cert : certs){
                print("\tCert. Type: " + cert.getType()); 
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
