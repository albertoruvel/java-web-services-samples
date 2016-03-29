package com.ws.samples.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.ws.samples.sei.TimeServer;

public class TimeClient {

	public static void main(String[] args)throws Exception{
		URL url = new URL("http://localhost:8080/ts?wsdl"); 
		//qualified name of the service 
		//1st arg is the service url 
		//2nd is the service name published  in the WSDL 
		QName qname = new QName("http://sib.samples.ws.com/", "TimeServerImplService"); 
		//create a factory for the service 
		Service service = Service.create(url, qname); 
		//extract the endpoint interface, the service "port" 
		TimeServer server = service.getPort(TimeServer.class); 
		System.out.println(server.getTimeAsString());
		System.out.println(server.getTimeAsElapsed());
	}

}
