package com.ws.samples.publish;

import javax.xml.ws.Endpoint; 

import com.ws.samples.sib.TimeServerImpl;
/**
 * This application publishes the web service whose
 * SIB is com.ws.samples.sib.TimeServerImpl. For now, the
 * service is published at network address 127.0.0.1.,
 * which is localhost, and at port number 9876, as this
 * port is likely available on any desktop machine. The
 * publication path is /ts, an arbitrary name.
 *
 * The Endpoint class has an overloaded publish method.
 * In this two-argument version, the first argument is the
 * publication URL as a string and the second argument is
 * an instance of the service SIB, in this case
 * ch01.ts.TimeServerImpl.
 *
 * The application runs indefinitely, awaiting service requests.
 * It needs to be terminated at the command prompt with control-C
 * or the equivalent.
 *
 * Once the applicatation is started, open a browser to the URL
 * to view the service contract, the WSDL document. This is an
 * easy test to determine whether the service has deployed
 * successfully. If the test succeeds, a client then can be
 * executed against the service.
 * 
**/
public class TimeServerPublisher {

	public static void main(String[] args){
		Endpoint.publish("http://localhost:8080/ts", new TimeServerImpl()); 
	}

}
