package com.example.skywarsplugin.eventlisteners;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.arena.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class deathListener implements Listener {
    public ArenaManager arenaManager;
    public SkyWars plugin;
    public deathListener(SkyWars plugin){
        this.plugin=plugin;
        arenaManager=plugin.arenaManager;
    }
    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event){
        Arena arena=arenaManager.getArenaByPlayer(event.getPlayer());
        if(arena==null)return;
        event.setCancelled(true);
        if(arena.spectators.contains(event.getPlayer())){
            return;
        }
        if(!arena.isGameStarted){
            return;
        }
        arena.killPlayer(event.getPlayer());
    }
}
