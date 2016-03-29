/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client;

/**
 *
 * @author MACARENA
 */
public class TimeServerWDL {
    public static void main(String[] args) {
        //service implementation
        TimeServerImplService service = new TimeServerImplService(); 
        
        //get SEI from service 
        TimeServer server = service.getTimeServerImplPort();
        
        //execute methods 
        System.out.println(server.getTimeAsString());
        System.out.println(server.getTimeAsElapsed());
    }
}
