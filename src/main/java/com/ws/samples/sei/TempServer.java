/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sei;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author jose.rubalcaba
 */
@WebService
public interface TempServer {
    
    @WebMethod float c2f(float c); 
    @WebMethod float f2c(float f); 
    
}
