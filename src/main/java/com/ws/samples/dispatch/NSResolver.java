/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.dispatch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author jose.rubalcaba
 */
public class NSResolver implements NamespaceContext{

    private Map<String, String> prefix2uri; 
    private Map<String, String> uri2prefix; 

    public NSResolver() {
        if(prefix2uri == null)prefix2uri = Collections.synchronizedMap(new HashMap<String, String>());
        if(uri2prefix == null)uri2prefix = Collections.synchronizedMap(new HashMap<String, String>());
    }
    
    public NSResolver(String prefix, String uri){
        this(); 
        prefix2uri.put(prefix, uri); 
        uri2prefix.put(uri, prefix); 
    }
    
    public String getNamespaceURI(String prefix) {
        return prefix2uri.get(prefix); 
    }

    public String getPrefix(String namespaceURI) {
        return uri2prefix.get(namespaceURI); 
    }

    public Iterator getPrefixes(String namespaceURI) {
        return uri2prefix.keySet().iterator(); 
    }
    
}
