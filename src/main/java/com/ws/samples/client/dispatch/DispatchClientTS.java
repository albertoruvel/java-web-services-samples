/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client.dispatch;

import com.ws.samples.dispatch.NSResolver;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 *
 * @author jose.rubalcaba
 */
public class DispatchClientTS {

    public static void main(String[] args) {
        new DispatchClientTS().sendAndReceiveSOAP();
    }

    private void sendAndReceiveSOAP() {
        URI uri = null;
        try {
            uri = new URI("http://localhost:8080/ts"); //from WSDL  
        } catch (URISyntaxException ex) {
            System.err.println(ex);
        }

        QName serviceName = new QName("tns", uri.toString());
        QName port = new QName("tsPort", uri.toString());
        String endpoint = "http://localhost:8080/ts";
        //now create a service proxy or dispatcher

        Service service = Service.create(serviceName);
        service.addPort(port, HTTPBinding.HTTP_BINDING, endpoint);
        Dispatch<Source> dispatch = service.createDispatch(port, Source.class,
                Service.Mode.PAYLOAD);
        //send a request
        String soapRequest
                = "<?xml version='1.0' encoding='UTF-8'?> "
                + "<soap:Envelope "
                + "soap:encodingStyle='http://schemas.xmlsoap.org/soap/encoding/' "
                + "xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/' "
                + "xmlns:soapenc='http://schemas.xmlsoap.org/soap/encoding/' "
                + "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "xmlns:tns='http://ts.ch01/' "
                + "xmlns:xsd='http://www.w3.org/2001/XMLSchema'> "
                + "<soap:Body>"
                + "<tns:getTimeAsElapsed xsi:nil='true'/>"
                + "</soap:Body>"
                + "</soap:Envelope>";

        Map<String, Object> requestContext = dispatch.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
        StreamSource src = makeStreamSource(soapRequest);
        Source result = dispatch.invoke(src);
        displayResult(result, uri.toString());
    }

    private StreamSource makeStreamSource(String soapRequest) {
        ByteArrayInputStream stream = new ByteArrayInputStream(soapRequest.getBytes());
        return new StreamSource(stream);
    }

    private void displayResult(Source result, String uri) {
        DOMResult dom_result = new DOMResult();
        try {
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(result, dom_result);
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xp = xpf.newXPath();
            xp.setNamespaceContext(new NSResolver("tns", uri));
// In original version, "//time_result" instead
            String result_string = xp.evaluate("//return", dom_result.getNode());
            System.out.println(result_string);
        } catch (TransformerConfigurationException e) {
            System.err.println(e);
        } catch (TransformerException e) {
            System.err.println(e);
        } catch (XPathExpressionException e) {
            System.err.println(e);
        }
    }
}
