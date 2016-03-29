/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.xml;

import com.ws.samples.model.Person;
import com.ws.samples.model.Skier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author MACARENA
 */
public class Marshall {
    public static void main(String[] args) {
        Marshall.runExample(); 
    }
    
    private static void runExample(){
        JAXBContext cxt = null; 
        Marshaller marshaller = null; 
        Unmarshaller unmarshaller = null; 
        try{
             cxt = JAXBContext.newInstance(Skier.class); 
             marshaller = cxt.createMarshaller(); 
             marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
             Skier skier = createSkier(); 
             marshaller.marshal(skier, System.out);//print the xml to the main out stream 
             
             
             FileOutputStream out = new FileOutputStream("marshalled.mar"); 
             marshaller.marshal(skier, out);//write the xml to a file 
             out.close(); 
             
             //unmarshall the skier 
             unmarshaller = cxt.createUnmarshaller(); 
             Skier clone = (Skier)unmarshaller.unmarshal(new File("marshalled.mar")); 
             System.out.println();
             marshaller.marshal(clone, System.out);
        }catch(JAXBException ex){
            System.err.println("Error: " + ex.getMessage());
        }catch(IOException ex){
            System.err.println("Error: " + ex.getMessage());
        }
        
    }

    private static Skier createSkier() {
        Person person = new Person("Alberto Rubalcaba", 25, "Male"); 
        List list = new ArrayList();
        list.add("Hello");
        list.add("Another hello"); 
        list.add("Marshalling"); 
        list.add("Unmarshalling"); 
        return new Skier(person, "FUCK IT!", list); 
    }
}
