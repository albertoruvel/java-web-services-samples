
package com.ws.samples.client.rabbit;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "FibException", targetNamespace = "http://ch03.fib")
public class FibException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private FibException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public FibException_Exception(String message, FibException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public FibException_Exception(String message, FibException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.ws.samples.client.rabbit.FibException
     */
    public FibException getFaultInfo() {
        return faultInfo;
    }

}
