package com.example.skywarsplugin.arena;

import com.example.skywarsplugin.team.TeamData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ArenaData {
    public String name="";
    public String pos1="";
    public String pos2="";
    public String world="";
    public ArrayList<String> islandpos=new ArrayList<>();
    public String lobbypos="";
    public int gameTime=-1;

    public ArenaData(String name, String world,String[] islandpos, String lobbypos, TeamData[] teams,int gameTime){
        this.name=name;
        this.pos1=pos1;
        this.pos2=pos2;
        this.world=world;
        this.islandpos=Arrays.stream(islandpos).collect(Collectors.toCollection(ArrayList::new));
        this.lobbypos=lobbypos;
        this.gameTime=gameTime;
    }
    public ArenaData(){

    }
}
