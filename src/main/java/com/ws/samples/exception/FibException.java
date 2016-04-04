/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.exception;

/**
 *
 * @author jose.rubalcaba
 */
public class FibException extends Exception{

    public FibException() {
    }

    public FibException(String message) {
        super(message);
    }

    public FibException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
