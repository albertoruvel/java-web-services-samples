/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.publish;

import com.ws.samples.pooling.SampleThreadPool;
import com.ws.samples.sib.TimeServerImpl;
import javax.xml.ws.Endpoint;

/**
 *
 * @author MACARENA
 */
public class TimePublishMultiThreading {
    private static Endpoint endpoint;
    
    public static void main(String[] args) {
        TimePublishMultiThreading pub = new TimePublishMultiThreading(); 
        endpoint = Endpoint.create(new TimeServerImpl());
        //set executor 
        endpoint.setExecutor(new SampleThreadPool());
        endpoint.publish("http://localhost:8080/ts");
    }
}
