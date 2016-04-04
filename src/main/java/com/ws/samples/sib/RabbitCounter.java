/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sib;

import com.ws.samples.exception.FibException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * @author jose.rubalcaba
 */
@WebService(targetNamespace = "http://ch03.fib")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED, 
             style = SOAPBinding.Style.DOCUMENT,
             use = SOAPBinding.Use.LITERAL)
public class RabbitCounter {
    private Map<Integer, Integer> caches = Collections.synchronizedMap(new HashMap<Integer, Integer>()); 
    
    public int countRabbits(int n)throws FibException{
        //throw a fault if n is negative 
        if(n < 0) throw new FibException("Negative argument not allowed, n=" + n);
        //easy cases 
        if(n < 2)
            return n; 
        
        //returned cached values if present 
        if(caches.containsKey(n))return caches.get(n); 
        if(caches.containsKey(n - 1) && caches.containsKey(n - 2)){
            caches.put(n, caches.get(n - 1) + caches.get(n - 2));
            return caches.get(n); 
        }
        
        //otherwise, compute from scratch, cache, and return
        int fib = 1, prev = 0; 
        for (int i = 2; i <= n; i++) {
            int tmp = fib; 
            fib += prev; 
            prev = tmp; 
        }
        caches.put(n, fib); 
        return fib; 
    }
}
