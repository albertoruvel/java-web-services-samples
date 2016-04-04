/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.publish;

import com.ws.samples.sib.RabbitCounter;
import javax.xml.ws.Endpoint;

/**
 *
 * @author jose.rubalcaba
 */
public class RabbitCountPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/rc", new RabbitCounter()); 
    }
}
