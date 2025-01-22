package com.example.skywarsplugin.command.playercommands;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.*;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.arena.ArenaData;
import com.example.skywarsplugin.command.SubCommand;
import com.example.skywarsplugin.team.Team;
import com.example.skywarsplugin.team.TeamData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ArenaList  extends SubCommand {
    SkyWars plugin;
    public ArenaList(SkyWars plugin){
        this.plugin=plugin;
    }
    @Override
    public void runCommand(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        for(ArenaData d: plugin.arenaManager.arenaDataList){
            sender.sendMessage(d.name+" "+d.world+" "+d.lobbypos);
            for(TeamData td: d.teams){
                sender.sendMessage(td.name+" "+td.color+" "+td.spawnpos);
            }
        }
        for(Arena a:plugin.arenaManager.arenas){
            for(Team t:a.teams){
                sender.sendMessage(t.banner.displayName());
            }
        }
    }
    @Override
    public boolean canExecute(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        if(subCommandArgs.length!=1)return false;
        return (subCommandLabel.equals("arena") && subCommandArgs[0].equals("list"));
    }
}
