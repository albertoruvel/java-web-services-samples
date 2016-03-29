package com.ws.samples.sib;

import java.util.Date;

import javax.jws.WebService;

import com.ws.samples.sei.TimeServer;

/**
 * The @WebService property endpoint interface links the SIB (This class) to the SEI (Interface) 
 * Note that the method implementations are not annotated with @WebMethod
 * 
 * @author jose.rubalcaba
 */

@WebService(endpointInterface = "com.ws.samples.sei.TimeServer")
public class TimeServerImpl implements TimeServer{


	public String getTimeAsString() {
		return new Date().toString(); 
	}

	public long getTimeAsElapsed() {
		return new Date().getTime(); 
	}

    public String getHelloStatement(String name) {
        return "Hello " + name + ", how are you today with web services? :)"; 
    }

}
