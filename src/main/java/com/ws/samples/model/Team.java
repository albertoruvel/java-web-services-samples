/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.model;

import java.util.List;

/**
 *
 * @author MACARENA
 */
public class Team {
    private String name; 
    private String nickname; 
    private List<Player> players; 

    public Team(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }

    public Team(String name, List<Player> players) {
        this.name = name;
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    
}
