package com.ws.samples.sei;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * The @WebService signals that this is the SEI (Service Endpoint Interface). 
 * @WebMethod signals that each method is a service operation
 * 
 * The @SOAPBinding annotation impacts the under-the-hood construction 
 * of the service contract. The WSDL (Web Service Definition Language) document. 
 * Style.RPC simplifies the contract and make deploying easier
 * 
 * @author jose.rubalcaba
 */
@WebService
//@SOAPBinding(style = Style.RPC) //changed to Style.Document style
public interface TimeServer {
	@WebMethod
	public String getTimeAsString(); 
	
	@WebMethod
	public long getTimeAsElapsed(); 
        
        @WebMethod
        public String getHelloStatement(String name); 
}
