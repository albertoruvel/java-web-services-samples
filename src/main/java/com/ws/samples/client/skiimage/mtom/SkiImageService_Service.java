
package com.ws.samples.client.skiimage.mtom;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SkiImageService", targetNamespace = "http://sib.samples.ws.com/", wsdlLocation = "http://localhost:8080/ski?wsdl")
public class SkiImageService_Service
    extends Service
{

    private final static URL SKIIMAGESERVICE_WSDL_LOCATION;
    private final static WebServiceException SKIIMAGESERVICE_EXCEPTION;
    private final static QName SKIIMAGESERVICE_QNAME = new QName("http://sib.samples.ws.com/", "SkiImageService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/ski?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SKIIMAGESERVICE_WSDL_LOCATION = url;
        SKIIMAGESERVICE_EXCEPTION = e;
    }

    public SkiImageService_Service() {
        super(__getWsdlLocation(), SKIIMAGESERVICE_QNAME);
    }

    public SkiImageService_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), SKIIMAGESERVICE_QNAME, features);
    }

    public SkiImageService_Service(URL wsdlLocation) {
        super(wsdlLocation, SKIIMAGESERVICE_QNAME);
    }

    public SkiImageService_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SKIIMAGESERVICE_QNAME, features);
    }

    public SkiImageService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SkiImageService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SkiImageService
     */
    @WebEndpoint(name = "SkiImageServicePort")
    public SkiImageService getSkiImageServicePort() {
        return super.getPort(new QName("http://sib.samples.ws.com/", "SkiImageServicePort"), SkiImageService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SkiImageService
     */
    @WebEndpoint(name = "SkiImageServicePort")
    public SkiImageService getSkiImageServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://sib.samples.ws.com/", "SkiImageServicePort"), SkiImageService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SKIIMAGESERVICE_EXCEPTION!= null) {
            throw SKIIMAGESERVICE_EXCEPTION;
        }
        return SKIIMAGESERVICE_WSDL_LOCATION;
    }

}
