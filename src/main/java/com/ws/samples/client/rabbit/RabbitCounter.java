
package com.ws.samples.client.rabbit;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "RabbitCounter", targetNamespace = "http://ch03.fib")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface RabbitCounter {


    /**
     * 
     * @param arg0
     * @return
     *     returns int
     * @throws FibException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "countRabbits", targetNamespace = "http://ch03.fib", className = "com.ws.samples.client.rabbit.CountRabbits")
    @ResponseWrapper(localName = "countRabbitsResponse", targetNamespace = "http://ch03.fib", className = "com.ws.samples.client.rabbit.CountRabbitsResponse")
    @Action(input = "http://ch03.fib/RabbitCounter/countRabbitsRequest", output = "http://ch03.fib/RabbitCounter/countRabbitsResponse", fault = {
        @FaultAction(className = FibException_Exception.class, value = "http://ch03.fib/RabbitCounter/countRabbits/Fault/FibException")
    })
    public int countRabbits(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0)
        throws FibException_Exception
    ;

}
