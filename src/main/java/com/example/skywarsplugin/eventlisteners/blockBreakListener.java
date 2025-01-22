package com.example.skywarsplugin.eventlisteners;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.arena.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class blockBreakListener implements Listener {
    public ArenaManager arenaManager;
    public SkyWars plugin;
    public blockBreakListener(SkyWars plugin){
        this.plugin=plugin;
        arenaManager=plugin.arenaManager;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Arena arena=plugin.arenaManager.getArenaByPlayer(event.getPlayer());
        if(arena!=null && !arena.isGameStarted)event.setCancelled(true);
    }
}
