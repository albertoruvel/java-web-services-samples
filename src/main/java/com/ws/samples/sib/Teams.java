/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.sib;

import com.ws.samples.model.Team;
import com.ws.samples.util.TeamsUtility;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author MACARENA
 */
@WebService  
public class Teams {
    private TeamsUtility teamsUtility; 
    
    public Teams(){
        teamsUtility = new TeamsUtility(); 
        teamsUtility.makeTestTeams(); 
    }
    
    @WebMethod
    public Team getTeam(String name){
        return teamsUtility.getTeam(name);
    }
    
    @WebMethod
    public List<Team> getTeams(){
        return teamsUtility.getTeams() ;
    }
}
