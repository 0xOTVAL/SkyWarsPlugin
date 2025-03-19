package com.example.skywarsplugin.command.playercommands;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class JoinArena extends SubCommand {
    SkyWars plugin;
    public JoinArena(SkyWars plugin){
        this.plugin=plugin;
    }
    @Override
    public void runCommand(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        Player player= Objects.requireNonNull(Bukkit.getPlayer(sender.getName()));
        Arena arena=plugin.arenaManager.getArenaByName(subCommandArgs[0]);
        sender.sendMessage(Bukkit.getWorld(  plugin.arenas_list.getFirst().world).getName());
        if(arena==null){
            sender.sendMessage("Arena "+subCommandArgs[0]+" does not exist");
            return;
        }
        sender.sendMessage(arena.addPlayer(player));
    }
    @Override
    public boolean canExecute(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        if(subCommandArgs.length!=1)return false;
        return ((sender instanceof Player) && subCommandLabel.equals("join"));
    }
}
