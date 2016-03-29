/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.validation;

import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 *
 * @author MACARENA
 */
public class XMLValidation {
    public static void main(String[] args) {
        try{
            final String schemaUri = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(schemaUri); 
            Schema schema = factory.newSchema(new StreamSource(args[1])); 
            //validate xml with xsd 
            Validator validator = schema.newValidator(); 
            validator.validate(new StreamSource(args[0]));
            System.out.println(args[0] + " validated against " + args[1] + " Schema");
        }catch(SAXException ex){
            System.err.println("Error: " + ex.getMessage());
        }catch(IOException ex){
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
