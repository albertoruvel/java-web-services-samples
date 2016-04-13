/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jose.rubalcaba
 */
public class RabbitCounterServletClient {
    
    private static final String url = "http://localhost:8080/ocewsd-samples/rabbitCounter"; 
    
    public static void main(String[] args) {
        new RabbitCounterServletClient().sendRequests();
    }

    private void sendRequests() {
        try{
            HttpURLConnection conn = null; 
            
            //POST request to create som Fibonacci numbers
            List<Integer> nums = new ArrayList<Integer>();
            for(int i = 1; i < 15; i ++)nums.add(i); 
            String payload = URLEncoder.encode("nums", "UTF-8") + "=" +
                    URLEncoder.encode(nums.toString(), "UTF-8");
            
            System.out.println("Sending POST");
            //send request
            conn = getConnection(url + "?num=12", "POST"); 
            conn.setRequestProperty("accept", "text/xml");
            DataOutputStream out = new DataOutputStream(conn.getOutputStream()); 
            out.writeBytes(payload);
            out.flush();
            getResponse(conn);
            
            //GET to test whether POST worked 
            System.out.println("Sending GET");
            conn = getConnection(url, "GET"); 
            conn.addRequestProperty("accept", "text/xml");
            conn.connect(); 
            getResponse(conn);
            
            System.out.println("Sending GET");
            conn = getConnection(url + "?num=12", "GET");
            conn.addRequestProperty("accept", "text/plain");
            conn.connect();
            getResponse(conn);
            
            //DELETE 
            System.out.println("Sending DELETE");
            conn = getConnection(url + "?num=12", "DELETE"); 
            conn.addRequestProperty("accept", "text/xml");
            conn.connect();
            getResponse(conn);
            
            //GET request to test if DELETE worked
            System.out.println("Sending GET");
            conn = getConnection(url + "?num=12", "GET"); 
            conn.addRequestProperty("accept", "text/html");
            conn.connect();
            getResponse(conn);
        }catch(IOException ex){
            System.err.println(ex);
        }catch(NullPointerException ex){
            System.err.println(ex);
        }
    }

    private HttpURLConnection getConnection(String urlString, String verb) {
        HttpURLConnection conn= null; 
        try{
            URL url = new URL(urlString); 
            conn = (HttpURLConnection)url.openConnection(); 
            conn.setRequestMethod(verb);
            conn.setDoInput(true);
            conn.setDoOutput(true);
        }catch(IOException ex){
            System.err.println(ex);
        }
        return conn; 
    }

    private void getResponse(HttpURLConnection conn) {
        try{
            String xml = ""; 
            BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(conn.getInputStream())); 
            String next = null; 
            while ((next = reader.readLine()) != null) xml += next; 
            System.out.println("Response:\n " + xml);
        }catch(IOException ex){
            System.err.println(ex);
        }
    }
}
