/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sib.rest;

import com.ws.samples.model.Team;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

/**
 *
 * @author jose.rubalcaba
 */

/**
 * The service must implement the Provider interface instead implementing a SEI 
 * @author jose.rubalcaba
 */
@WebServiceProvider


/**
 * There are two service modes: 
 * <b>Payload</b> means the service needs to access only the payload of the request
 * <b>Message</b> means the service needs to access the whole message of the request
 */
@ServiceMode(Service.Mode.MESSAGE)

/**
 * The BindingType 
 */
@BindingType(value = HTTPBinding.HTTP_BINDING)
public class RestfulTeams implements Provider<Source>{

    
    @Resource
    private WebServiceContext wsContext; 
    
    private Map<String, Team> teamsMap;  //for easy lookups 
    private List<Team> teams;            //serialized/deserialized
    private byte[] teamBytes;            //from the persistence file
    
    private static final String FILE_NAME = "teams.ser"; 
    private static final Logger logger = Logger.getLogger("RESTfulTeamsService"); 

    public RestfulTeams() {
        readTeamsFromFile();  //read the raw bytes from teams.ser
        deserialize();        //deserialize to a list<team>
    }
    
    
    
    
    
    public Source invoke(Source request) {
        if(wsContext == null) throw new RuntimeException("No WebServiceContext found"); 
        //grab the message context and extract the request verb 
        String httpVerb = (String)wsContext.getMessageContext()
                .get(MessageContext.HTTP_REQUEST_METHOD); 
        httpVerb = httpVerb.trim().toUpperCase(); 
        //act on the verb 
        if(httpVerb.equals("GET")) return doGet(wsContext.getMessageContext()); 
        else throw new HTTPException(405); //method not allowed 
    }

    private void readTeamsFromFile() {
       try{
           String cwd = System.getProperty("user.dir");
           String sep = System.getProperty("file.separator"); 
           String path = getFilePath(); 
           int length = (int)new File(path).length();
           teamBytes = new byte[length]; 
           new FileInputStream(path).read(teamBytes); 
       }catch(IOException ex){
           logger.severe("Exception: " + ex.getMessage()); 
       }
    }

    private void deserialize() {
        //decode the xml 
        XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(teamBytes)); 
        teams = (List<Team>)decoder.readObject(); 
        
        //create a map for quick lookup of teams 
        teamsMap = Collections.synchronizedMap(new HashMap<String, Team>()); 
        for (Team team : teams) 
            teamsMap.put(team.getName(), team); 
    }

    private Source doGet(MessageContext messageContext) {
        //parse query string 
        String query = (String)messageContext.get(MessageContext.QUERY_STRING); 
        //get all teams 
        if(query == null)
            return new StreamSource(new ByteArrayInputStream(teamBytes)); 
        //get a named team 
        else{
            String name = getValueFromQueryString("name", query); 
            //check if named team exists 
            Team team = teamsMap.get(name); 
            if(team == null)throw new HTTPException(404);
            //generates XML and return
            return new StreamSource(encodeToStream(team)); 
        }
    }

    private String getValueFromQueryString(String name, String query) {
        String[] array = query.split("="); 
        if(! array[0].equalsIgnoreCase(name))
            throw new HTTPException(400); //bad request
        return array[1].trim(); 
    }

    private InputStream encodeToStream(Object team) {
        //serialize object to XML 
        ByteArrayOutputStream out = new ByteArrayOutputStream(); 
        XMLEncoder encoder = new XMLEncoder(out);
        encoder.writeObject(team);
        encoder.close();
        return new ByteArrayInputStream(out.toByteArray()); 
    }

    private String getFilePath() {
        String cwd = System.getProperty("user.dir"); 
        String sep = System.getProperty("file.separator"); 
        return cwd + sep + "rest" + sep + "team" + sep + FILE_NAME; 
    }
    
}
