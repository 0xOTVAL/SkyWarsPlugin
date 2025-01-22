package com.example.skywarsplugin.command.playercommands;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StartArenaGame extends SubCommand {
    SkyWars plugin;
    public StartArenaGame(SkyWars plugin){
        this.plugin=plugin;
    }
    @Override
    public void runCommand(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        Arena arena=plugin.arenaManager.getArenaByName(subCommandArgs[0]);
        if(arena==null){
            sender.sendMessage("Arena "+subCommandArgs[0]+" does not exist");
            return;
        }
        sender.sendMessage(arena.startGame());
    }
    @Override
    public boolean canExecute(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        if(subCommandArgs.length!=2)return false;
        return (subCommandLabel.equals("arena") && subCommandArgs[1].equals("startgame"));
    }
}
