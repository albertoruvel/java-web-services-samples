/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.model;

import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MACARENA
 */
@XmlRootElement
public class Skier {
    private Person person; 
    
    private String nationalTeam; 
    private Collection majorAchievements; 

    public Skier(Person person, String nationalTeam, Collection majorAchievements) {
        this.person = person;
        this.nationalTeam = nationalTeam;
        this.majorAchievements = majorAchievements;
    }

    public Skier() {
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getNationalTeam() {
        return nationalTeam;
    }

    public void setNationalTeam(String nationalTeam) {
        this.nationalTeam = nationalTeam;
    }

    public Collection getMajorAchievements() {
        return majorAchievements;
    }

    public void setMajorAchievements(Collection majorAchievements) {
        this.majorAchievements = majorAchievements;
    }
    
    
}
