/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.util;

import com.ws.samples.model.Team;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author MACARENA
 */
public class TeamsUtility {
    private Map<String, Team> teams; 
    
    public TeamsUtility(){
        teams = new HashMap<String, Team>(); 
    }
    
    public List<Team> getTeams(){
        List<Team> list = new ArrayList<Team>(); 
        Set<String> set = teams.keySet(); 
        for(String s : set)
            list.add(teams.get(s)); 
        return list; 
    }
    
    public void makeTestTeams(){
        for(int i = 0; i < 5; i ++){
            teams.put("team" + (i + 1), new Team("TeamName " + (i + 1), "team Nickname " + (i + 1))); 
        }
    }
    
    /**
     * searches for a team
     * @param name
     * @return 
     */
    public Team getTeam(String name){
        for(String s : teams.keySet())
            if(teams.get(s).getName().equals(name))
                return teams.get(s); 
        return null; 
    }
}
