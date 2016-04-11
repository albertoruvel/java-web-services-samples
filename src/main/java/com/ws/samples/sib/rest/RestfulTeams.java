/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sib.rest;

import com.ws.samples.dispatch.NSResolver;
import com.ws.samples.model.Player;
import com.ws.samples.model.Team;
import com.ws.samples.response.HttpResponse;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;

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
        else if(httpVerb.equals("POST"))return doPost(wsContext.getMessageContext()); 
        else if(httpVerb.equals("DELETE"))return doDelete(wsContext.getMessageContext());
        else if(httpVerb.equals("PUT"))return doPut(wsContext.getMessageContext()); 
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

    private ByteArrayInputStream encodeToStream(Object team) {
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

    private Source doPost(MessageContext messageContext) {
        Map<String, List> headers = (Map<String, List>) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS); 
        List<String> cargo = headers.get("Cargo"); 
        if(cargo == null)throw new HTTPException(400); //bad request
        
        String xml = ""; 
        for (String next : cargo) xml += next.trim();
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes()); 
        String teamName = null; 
        try{
            //setup the xpath object to search for the xml elements
            DOMResult result = new DOMResult(); 
            Transformer transformer = TransformerFactory.newInstance().newTransformer(); 
            transformer.transform(new StreamSource(bais), result);
            URI nsUri = new URI("create_team");
            
            XPathFactory xPathFactory = XPathFactory.newInstance(); 
            XPath xp = xPathFactory.newXPath(); 
            
            xp.setNamespaceContext(new NSResolver("", nsUri.toString()));
            teamName = xp.evaluate("create_team/name", result.getNode()); 
            List<Player> teamPlayers = new ArrayList<Player>(); 
            NodeList players = (NodeList)xp.evaluate("player", result.getNode(), 
                    XPathConstants.NODESET); 
            for (int i = 1; i < players.getLength(); i++) {
                String name = xp.evaluate("name", result.getNode()); 
                String nickname = xp.evaluate("nickname", result.getNode()); 
                Player player = new Player(name, nickname);
                teamPlayers.add(player);
            }
             
             Team team = new Team(teamName, teamPlayers); 
             teamsMap.put(team.getName(), team); 
             teams.add(team); 
             serialize(); 
        }catch(TransformerException ex){
            
        }catch(TransformerFactoryConfigurationError ex){
            
        }catch(URISyntaxException ex){
            
        }catch(XPathExpressionException ex){
            
        }
        return responseToClient("Team " + teamName + " created");
    }

    private Source doDelete(MessageContext messageContext) {
        String query = (String)messageContext.get(MessageContext.QUERY_STRING); 
        //disallow deletion of all teams 
        if(query == null)throw new HTTPException(403); //illegal operation 
        else{
            String name = getValueFromQueryString("name", query); 
            if(! teamsMap.containsKey(name))throw new HTTPException(404); 
            
            //remove team from map and list 
            Team team = teamsMap.get(name); 
            teams.remove(team); 
            teamsMap.remove(name); 
            serialize(); 
            //send response 
            return responseToClient(name + " deleted"); 
        }
    }

    private Source doPut(MessageContext messageContext) {
        String query = (String)messageContext.get(MessageContext.QUERY_STRING); 
        String name = null; 
        String newName = null; 
        if(query == null)throw new HTTPException(403);//illegal operation
        else{
            String[] array = query.split("&"); 
            if(array[1] == null || array[0] == null)throw new HTTPException(403); 
            Team team = teamsMap.get(name); 
            if(team == null)throw new HTTPException(404);
            team.setName(newName);
            teamsMap.put(newName, team); 
            serialize(); 
        }
        return responseToClient("Team name " + name + " changed to " + newName); 
    }

    private void serialize() {
        try{
            String path = getFilePath(); 
            BufferedOutputStream st = new BufferedOutputStream(new FileOutputStream(path));
            XMLEncoder encoder = new XMLEncoder(st); 
            encoder.writeObject(teams); 
            encoder.close();
            st.close();
        }catch(IOException ex){
            
        }
    }

    private Source responseToClient(String string) {
        HttpResponse response = new HttpResponse(string); 
        ByteArrayInputStream stream = encodeToStream(response); 
        return new StreamSource(stream); 
    }
    
}
