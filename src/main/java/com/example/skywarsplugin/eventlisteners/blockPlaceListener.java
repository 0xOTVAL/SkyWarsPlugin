package com.example.skywarsplugin.eventlisteners;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class blockPlaceListener implements Listener {
    public ArenaManager arenaManager;
    public SkyWars plugin;
    public blockPlaceListener(SkyWars plugin){
        this.plugin=plugin;
        arenaManager=plugin.arenaManager;
    }
    @EventHandler
    void PlaceBlock(BlockPlaceEvent event){
        Arena arena=arenaManager.getArenaByPlayer(event.getPlayer());
        if(arena==null)return;
        if(!arena.isStarted){
            return;
        }
        if(!arena.isGameStarted){
            event.setCancelled(true);
            return;
        }
    }
}
