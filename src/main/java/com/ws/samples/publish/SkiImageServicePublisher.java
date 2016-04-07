/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.publish;

import com.ws.samples.sib.SkiImageService;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

/**
 *
 * @author jose.rubalcaba
 */
public class SkiImageServicePublisher {
    private Endpoint endpoint; 
    
    public static void main(String[] args) {
        SkiImageServicePublisher publisher = new SkiImageServicePublisher(); 
        publisher.createEndpoint();
        publisher.configEndpoint(); 
        publisher.publish(); 
    }
    
    public void createEndpoint(){
        endpoint = Endpoint.create(new SkiImageService());
    }

    private void configEndpoint() {
        SOAPBinding binding = (SOAPBinding)endpoint.getBinding();
        binding.setMTOMEnabled(true);
    }

    private void publish() {
        endpoint.publish("http://localhost:8080/ski");
    }
}
