package com.example.skywarsplugin.eventlisteners;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.arena.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class logoutListener implements Listener {
    public ArenaManager arenaManager;
    public SkyWars plugin;
    public logoutListener(SkyWars plugin){
        this.plugin=plugin;
        arenaManager=plugin.arenaManager;
    }
    @EventHandler
    public void playerLogout(PlayerQuitEvent event){
        Arena arena =arenaManager.getArenaByPlayer(event.getPlayer());
        if(arena!=null)arena.logoutPlayer(event.getPlayer());
    }
}
