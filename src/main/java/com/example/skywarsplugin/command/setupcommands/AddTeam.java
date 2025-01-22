package com.example.skywarsplugin.command.setupcommands;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.ArenaData;
import com.example.skywarsplugin.command.SubCommand;
import com.example.skywarsplugin.team.TeamData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddTeam extends SubCommand {
    SkyWars plugin;
    public AddTeam(SkyWars plugin){
        this.plugin=plugin;
    }
    @Override
    public void runCommand(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        ArenaData arenaData=plugin.arenaManager.getArenaDataByName(subCommandArgs[0]);
        if(arenaData==null){
            sender.sendMessage("Arena "+subCommandArgs[0]+" does not exist");
            return;
        }
        TeamData teamData=new TeamData(subCommandArgs[2].split(",")[0],subCommandArgs[2].split(",")[1],"");
        for(TeamData t:arenaData.teams){
            if(t.name.equals(teamData.name)){
                sender.sendMessage("Team "+t.name+" already exists");
                return;
            }
        }
        arenaData.teams.add(teamData);
    }

    @Override
    public boolean canExecute(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        if(subCommandArgs.length!=3)return false;
        return ((sender instanceof Player) && subCommandLabel.equals("arena") && subCommandArgs[1].equals("addteam"));
    }
}
