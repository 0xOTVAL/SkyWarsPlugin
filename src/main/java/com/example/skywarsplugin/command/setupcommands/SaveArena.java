package com.example.skywarsplugin.command.setupcommands;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.ArenaData;
import com.example.skywarsplugin.arena.ArenaManager;
import com.example.skywarsplugin.command.SubCommand;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.nio.charset.Charset;

public class SaveArena extends SubCommand {
    SkyWars plugin;
    public SaveArena(SkyWars plugin){
        this.plugin=plugin;
    }

    @Override
    public void runCommand(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        try {
            Gson g = new Gson();
            for(ArenaData d: plugin.arenaManager.arenaDataList){
                if(d.name.isEmpty() ||  d.world.isEmpty() || d.islandpos.isEmpty()|| d.lobbypos.isEmpty() ){
                    sender.sendMessage("Arena "+d.name+" is incomplete, not saving");
                    return;
                }
            }
            String out= g.toJson(plugin.arenaManager.arenaDataList);
            File arenas = new File(plugin.getDataFolder(), "arena_list.json");
            FileUtils.writeStringToFile(arenas,out, Charset.defaultCharset());
            plugin.arenaManager.updateArenasData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean canExecute(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        if(subCommandArgs.length!=1)return false;
        return (subCommandLabel.equals("arena") && subCommandArgs[0].equals("save"));
    }
}
