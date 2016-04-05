/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.util;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author jose.rubalcaba
 */
public class MapDump {
    public static void mapDump(Map map, String indent){
        Set set = map.keySet(); 
        for (Object obj : set) {
            System.out.println(indent + obj + "==>" + map.get(obj));
            if(map.get(obj) instanceof Map)
                mapDump((Map)map.get(obj), indent += "  "); 
        }
    }
}
