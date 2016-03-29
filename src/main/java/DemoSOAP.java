    import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;



public class DemoSOAP {

	private static final String LocalName = "TimeRequest"; 
	private static final String Namespace = "http://sib.samples.ws.com/"; 
	private static final String NamespacePrefix = "ms";
	
	private ByteArrayOutputStream out; 
	private ByteArrayInputStream in; 
	
	//private final Logger logger = Logger.getLogger("DEMO SOAP CLASS", DemoSOAP.class.getName()); 
	
	public static void main(String[] args) {
		new DemoSOAP().request();
	}
	
	public void request(){
		//build a SOAP message to send to an output stream
		SOAPMessage message = createMessage();
                
		//inject the appropriate info to the message 
		//in this case, only the message header is used 
		//and the body is empty
		SOAPEnvelope envelope = null; 
		SOAPHeader header = null; 
		SOAPMessage response = null; 
		Name lookupName = null; 
		try{
			envelope = message.getSOAPPart().getEnvelope(); 
			header = envelope.getHeader();
			//add an element to the SOAP header 
			lookupName = createQName(message);
			header.addHeaderElement(lookupName).addTextNode("time_request"); 
			
			//simulate sending the soap message to a remote system by writing it to an output stream 
			out = new ByteArrayOutputStream(); 
			message.writeTo(out);
			trace("The sent SOAP message: ", message);
			
			 response = processRequest(); 
			 extractContentAndPrint(response); 
			 
		}catch(SOAPException se){
			System.err.println("SOAP EXCEPTION: " + se.getLocalizedMessage()); 
		}catch(IOException ioex){
			System.err.println("IO EXCEPTION: " + ioex.getLocalizedMessage());
		}
		
		
		
		
	}

	private void extractContentAndPrint(SOAPMessage response) {
		try{
			SOAPBody body = response.getSOAPBody();
			Name lookupName = createQName(response);
			Iterator it = body.getChildElements(lookupName);
			Node next = (Node)it.next(); 
			
			String value = next == null ? "Error" : next.getValue(); 
			System.out.println("Returned from server: " + value);
		}catch(SOAPException ex){
			System.err.println("Error: " + ex.getLocalizedMessage());
		}
	}

	private SOAPMessage processRequest() {
		processIncomingSoap(); 
		coordinateStreams(); 
		
		return createSoapMessage(in); 
	}

	private SOAPMessage createSoapMessage(ByteArrayInputStream in2) {
		SOAPMessage message = null; 
		try{
			MessageFactory factory = MessageFactory.newInstance();
			message = factory.createMessage(null, in2); 
		}catch(SOAPException ex){
			System.err.println("Error: " + ex.getLocalizedMessage());
		}catch(IOException ioex){
			System.err.println("Error: " + ioex.getLocalizedMessage());
		}
		return message; 
	}

	private void coordinateStreams() {
		in = new ByteArrayInputStream(out.toByteArray());
		out.reset();
	}

	private void processIncomingSoap() {
		try{
			//copy output stream to input stream to simulate 
			//coordinated streams over a network connection
			coordinateStreams();
			
			//create the received soap message from 
			//the input stream 
			SOAPMessage message = createSoapMessage(in);
			
			//inspect the soap header for the keword 'time_request' 
			//and process the request if the keyword occurs 
			Name lookupName = createQName(message);
			
			SOAPHeader header = message.getSOAPHeader(); 
			Iterator iterator = header.getChildElements(lookupName);
			Node next = (Node)iterator.next(); 
			String value = next == null ? "Error" : next.getValue(); 
			
			//if soap message contains request for the time, 
			//create a new soap message with the current time in the body
			if(value.toLowerCase().equals("time_request")){
				//extract the body and add the current time as an element 
				String now = new Date().toString(); 
				SOAPBody body = message.getSOAPBody(); 
				body.addBodyElement(lookupName).addTextNode(now);
				message.saveChanges(); 
				
				//write to the output stream 
				message.writeTo(out);
				trace("The received/processed SOAP message: ", message);
			}
		}catch(SOAPException se){
			System.err.println("Error: " + se.getLocalizedMessage());
		}catch(IOException io){
			System.err.println("Error: " + io.getLocalizedMessage());
		}
	}

	private void trace(String string, SOAPMessage message) {
		System.out.println("\n");
		System.out.println(string);
		try{
			message.writeTo(System.out);
		}catch(SOAPException soapex){
			System.err.println("Error: " + soapex.getLocalizedMessage());
		}catch(IOException ioex){
			System.err.println("Error: " + ioex.getLocalizedMessage());
		}
	}

	private Name createQName(SOAPMessage message) {
		Name name = null; 
		try{
			SOAPEnvelope env = message.getSOAPPart().getEnvelope(); 
			name = env.createName(LocalName, NamespacePrefix, Namespace);
		}catch(SOAPException ex){
			System.err.println("Error: " + ex.getLocalizedMessage()); 
		}
		return name; 
	}

	private SOAPMessage createMessage(){
		SOAPMessage message = null; 
		try{
			MessageFactory factory = MessageFactory.newInstance();
			message = factory.createMessage(); 
		}catch(SOAPException ex){
			System.err.println("Error: " + ex.getLocalizedMessage());
		}
		return message; 
	}
}
