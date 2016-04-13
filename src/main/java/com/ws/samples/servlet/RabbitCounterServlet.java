/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.servlet;
//import javax.se

import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;
/**
 *
 * @author jose.rubalcaba
 */

public class RabbitCounterServlet extends HttpServlet{

    private Map<Integer, Integer> cache; 
    
    public RabbitCounterServlet() {
    }
    
    

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("num"); 
        //only one fibonacci number may be deleted at a time
        if(key == null)throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST); 
        try{
            int n = Integer.parseInt(key.trim()); 
            cache.remove(n);
            sendTypedResponse(req, resp, n + " deleted");
        }catch(NumberFormatException ex){
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nums = req.getParameter("num"); 
        if(nums == null)throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST); 
        //extract integers from a string such as "[1, 2, 3]"
        nums = nums.replace('[', '\0'); 
        nums = nums.replace(']', '\0'); 
        String[] parts = nums.split(",");
        List<Integer> list = new ArrayList<Integer>(); 
        for (String next : parts) {
            int n = Integer.parseInt(next.trim());
            cache.put(n, countRabbits(n)); 
            list.add(cache.get(n)); 
        }
        sendTypedResponse(req, resp, list + " added");
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String num = req.getParameter("num"); 
        if(num == null){
            Collection<Integer> fibs = cache.values(); 
            sendTypedResponse(req, resp, fibs); 
        }else{
            try{
                Integer key = Integer.parseInt(num.trim()); 
                Integer fib = cache.get(key); 
                if(fib == null)fib = -1; 
                sendTypedResponse(req, resp, fib); 
            }catch(NumberFormatException ex){
                sendTypedResponse(req, resp, -1); 
            }
        }
    }

    @Override
    public void init() throws ServletException {
        cache = Collections.synchronizedMap(new HashMap<Integer, Integer>()); 
    }

    private void sendTypedResponse(HttpServletRequest req, HttpServletResponse resp, Object data) {
        String desiredType = req.getHeader("accept"); 
        if(desiredType.contains("text/plain"))
            sendPlain(resp, data); 
        else if(desiredType.contains("text/html"))
            sendHtml(resp, data); 
        else sendXml(resp, data);
    }


    private Integer countRabbits(int n) {
        if(n < 0) throw new HTTPException(HttpServletResponse.SC_FORBIDDEN); 
        //easy cases  
        if(n < 2)return n; 
        //return cached value if present 
        if(cache.containsKey(n))return cache.get(n); 
        if(cache.containsKey(n - 1) && cache.containsKey(n - 2)){
            cache.put(n, cache.get(n - 1) + cache.get(n - 2)); 
            return cache.get(n);
        }
        //otherwise, compute from scratch, cache, and return 
        int fib = 1, prev = 0; 
        for (int i = 2; i <= n; i++) {
            int tmp = fib; 
            fib += tmp; 
            prev = tmp; 
        }
        cache.put(n, fib); 
        return fib;
    }

    private void sendPlain(HttpServletResponse resp, Object data) {
        try{
            OutputStream out = resp.getOutputStream(); 
            out.write(data.toString().getBytes());
            out.flush();
        }catch(IOException ex){
            throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
        }
    }

    private void sendHtml(HttpServletResponse resp, Object data) {
        String htmlStart = "<html><head><title>send html response</title></head><body><div>";
        String htmlEnd = "</div></body></html>"; 
        String doc = htmlStart + data.toString() + htmlEnd; 
        sendPlain(resp, doc); 
    }

    private void sendXml(HttpServletResponse resp, Object data) {
        try{
            XMLEncoder enc = new XMLEncoder(resp.getOutputStream()); 
            enc.writeObject(data.toString()); 
            enc.close(); 
        }catch(IOException ex){
            throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
        }
    }
    
}
