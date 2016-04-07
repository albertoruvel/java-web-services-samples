/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.client;

import com.ws.samples.client.skiimage.SkiImageService;
import com.ws.samples.client.skiimage.SkiImageService_Service;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author jose.rubalcaba
 */
public class SkiImageServiceClient {
    
    private static final Logger logger = Logger.getLogger("SkiImageServiceClient"); 
    public static void main(String[] args) {
        SkiImageService_Service service = new SkiImageService_Service(); 
        SkiImageService port = service.getSkiImageServicePort(); 
        
        List<byte[]> list = port.getImages(); 
        logger.info("Got images: " + list.size());
    }
}
