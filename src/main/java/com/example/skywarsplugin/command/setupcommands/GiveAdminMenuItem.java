package com.example.skywarsplugin.command.setupcommands;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.ArenaData;
import com.example.skywarsplugin.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class GiveAdminMenuItem extends SubCommand {
    SkyWars plugin;
    public GiveAdminMenuItem(SkyWars plugin){
        this.plugin=plugin;
    }
    @Override
    public void runCommand(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        Player player= Objects.requireNonNull(Bukkit.getPlayer(sender.getName()));
        ItemStack menuItem=new ItemStack(Material.ENDER_EYE);
        ItemMeta meta=menuItem.getItemMeta();
        meta.setItemName("SkyWars admin menu");
        menuItem.setItemMeta(meta);
        player.getInventory().addItem(menuItem);
    }
    @Override
    public boolean canExecute(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        if(subCommandArgs.length!=0)return false;
        return (subCommandLabel.equals("menuitem"));
    }

}
