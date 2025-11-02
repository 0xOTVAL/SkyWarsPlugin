package com.example.skywarsplugin.command.setupcommands;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.ArenaData;
import com.example.skywarsplugin.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetGameTime extends SubCommand {
    SkyWars plugin;
    public SetGameTime(SkyWars plugin){
        this.plugin=plugin;
    }
    @Override
    public void runCommand(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        ArenaData arenaData=plugin.arenaManager.getArenaDataByName(subCommandArgs[0]);
        if(arenaData==null){
            sender.sendMessage("Arena "+subCommandArgs[0]+" does not exist");
            return;
        }
        arenaData.gameTime=Integer.parseInt(subCommandArgs[2]);
        sender.sendMessage("Set game time "+subCommandArgs[2]+" on arena "+arenaData.name);
    }
    @Override
    public boolean canExecute(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        if(subCommandArgs.length!=3)return false;
        return (subCommandLabel.equals("arena") && subCommandArgs[1].equals("settime"));
    }

}
