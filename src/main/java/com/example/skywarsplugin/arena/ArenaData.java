package com.example.skywarsplugin.arena;

import com.example.skywarsplugin.team.TeamData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ArenaData {
    public String name="";
    public String world="";
    public ArrayList<String> islandpos=new ArrayList<>();
    public String lobbypos="";
    public ArrayList<TeamData> teams= new ArrayList<>();

    public ArenaData(String name, String world,String[] islandpos, String lobbypos, TeamData[] teams,int gameTime){
        this.name=name;
        this.world=world;
        this.islandpos=Arrays.stream(islandpos).collect(Collectors.toCollection(ArrayList::new));
        this.lobbypos=lobbypos;
        this.teams= Arrays.stream(teams).collect(Collectors.toCollection(ArrayList::new));
    }
    public ArenaData(){

    }
    public TeamData getTeamDataByName(String name){
        for(TeamData t: teams){
            if(t.name.equals(name))return t;
        }
        return null;
    }
}
