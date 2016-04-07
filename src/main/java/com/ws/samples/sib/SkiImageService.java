/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sib;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author jose.rubalcaba
 */
@WebService(serviceName="SkiImageService")
//@BindingType(value = SOAPBinding.SOAP11HTTP_MTOM_BINDING) //this is the defaul
//@HandlerChain(file="handler-chain.xml")
public class SkiImageService {
    
    private static final Logger logger = Logger.getLogger("SkiImageService"); 
    private static final String[ ] names = { "nordic.jpg", "tele.jpg", "alpine.jpg" };
    private Map<String, String> photos;
    private String defaultKey;
    
    
    public SkiImageService(){
        photos = new HashMap<String, String>();
        photos.put("nordic", "nordic.jpg"); 
        photos.put("alpine", "alpine.jpg"); 
        photos.put("telemk", "telemk.jpg"); 
        defaultKey = "nordic";
    }
    
    @WebMethod
    public Image getImage(String name){
        return createImage(name); 
    }
    
    @WebMethod
    public List<Image> getImages(){
        return createImagesList(); 
    }
    
    private Image createImage(String name){
        byte[] bytes = getRawBytes(name);
        ByteArrayInputStream s = new ByteArrayInputStream(bytes);
        Iterator iterators = ImageIO.getImageReadersByFormatName("jpeg"); 
        ImageReader it = (ImageReader)iterators.next(); 
        try{
            ImageInputStream iis = ImageIO.createImageInputStream(s); 
            it.setInput(iis, true); 
            return it.read(0); 
        }catch(IOException ex){
            logger.severe("Exception: " + ex.getMessage());
            return null; 
        }
    }
    
    private List<Image> createImagesList(){
        List<Image> images = new ArrayList<Image>(); 
        Set<String> set = photos.keySet(); 
        for (String key : set) {
            Image image = createImage(key); 
            if(image != null) images.add(image); 
        }
        
        return images; 
    }

    private byte[] getRawBytes(String name) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        FileInputStream fis = null; 
        try{
            String cwd = System.getProperty("user.dir"); 
            String sep = System.getProperty("file.separator"); 
            String baseName = cwd + sep + "jpegs" + sep; 
            String fileName = baseName + name + ".jpg"; 
            fis  = new FileInputStream(fileName);
            
            //send default image if theres none with this name 
            if(fis == null) fis = new FileInputStream(baseName + "nordic.jpg"); 
            byte[] buff = new byte[2048]; 
            int n = 0; 
            while ((n = fis.read(buff)) != -1) 
                baos.write(buff, 0, n); 
            fis.close();
        }catch(IOException ex){
            logger.severe("Exception: " + ex.getMessage()); 
        }
        return baos.toByteArray(); 
    }
    
}
