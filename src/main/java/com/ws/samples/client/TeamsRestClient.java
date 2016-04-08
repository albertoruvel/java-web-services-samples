/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author jose.rubalcaba
 */
public class TeamsRestClient {
    
    public static final String ENDPOINT_URL = "http://localhost:8080/teams";
    private static final Logger logger = Logger.getLogger("TeamsRESTClient"); 
    
    public static void main(String[] args) {
        new TeamsRestClient().sendRequests(); 
    }

    private void sendRequests() {
        try{
            //GET requests
            HttpURLConnection conn = getConnection(ENDPOINT_URL, "GET"); 
            conn.connect();
            printAndParse(conn, true); 
            conn = getConnection(ENDPOINT_URL + "?name=MarxBrothers", "GET"); 
            conn.connect();
            printAndParse(conn, false);
            
        }catch(IOException ex){
            logger.severe("Exception: " + ex.getMessage());
        }catch(NullPointerException ex){
            logger.severe("Exception: " + ex.getMessage());
        }
    }

    private HttpURLConnection getConnection(String endpoint, String verb) {
        HttpURLConnection conn = null; 
        try{
            URL url = new URL(endpoint); 
            conn = (HttpURLConnection)url.openConnection(); 
            conn.setRequestMethod(verb);
        }catch(IOException ex){
            logger.severe("Exception: " + ex.getMessage());
        }
        return conn; 
    }

    private void printAndParse(HttpURLConnection conn, boolean parse) {
        try{
            String xml = ""; 
            BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())); 
            String next = null; 
            while ((next = reader.readLine()) != null) 
                xml += next;
            logger.info("Raw XML: \n" + xml); 
            if(parse){
                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                parser.parse(new ByteArrayInputStream(xml.getBytes()), new SaxParserHandler());
            }
        }catch(IOException ex){
            
        }catch(ParserConfigurationException ex){
            
        }catch(SAXException ex){
            
        }
    }
    
    class SaxParserHandler extends DefaultHandler{
        char[] buffer = new char[1024]; 
        int n = 0; 

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
             System.arraycopy(ch, start, buffer, 0, length);
             n += length; 
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(Character.isUpperCase(buffer[0]))
                logger.info(new String(buffer));
            clearBuffer(); 
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            clearBuffer();
        }

        private void clearBuffer() {
            Arrays.fill(buffer, '\0'); 
            
        }
        
        
    }
}
