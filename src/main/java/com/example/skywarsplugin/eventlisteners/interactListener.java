package com.example.skywarsplugin.eventlisteners;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.arena.ArenaManager;
import io.papermc.paper.event.player.PlayerOpenSignEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class interactListener implements Listener {
    public ArenaManager arenaManager;
    public SkyWars plugin;
    public interactListener(SkyWars plugin){
        this.plugin=plugin;
        arenaManager=plugin.arenaManager;
    }
    @EventHandler
    public void PlayerInteract(PlayerOpenSignEvent event){
        //get lines of sign
        String firstline= ((TextComponent)event.getSign().getSide(event.getSide()).lines().getFirst()).content();
        String secondline= ((TextComponent)event.getSign().getSide(event.getSide()).lines().get(1)).content();
        //first line is indicator of sign being arena entry point
        if(!firstline.equals("SkyWars"))return;
        //get arena and add player to it
        Arena arena=plugin.arenaManager.getArenaByName(secondline);
        if(arena!=null)event.getPlayer().sendMessage(arena.addPlayer(event.getPlayer()));
        event.setCancelled(true);
    }
}
