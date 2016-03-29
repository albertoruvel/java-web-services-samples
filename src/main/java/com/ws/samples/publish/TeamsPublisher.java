/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.publish;

import com.ws.samples.sib.Teams;
import javax.xml.ws.Endpoint;

/**
 *
 * @author MACARENA
 */
public class TeamsPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/teams", new Teams());
    }
}
