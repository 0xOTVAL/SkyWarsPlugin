package com.example.skywarsplugin.eventlisteners;


import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.ArenaManager;
import com.example.skywarsplugin.arena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class dropItemListener implements Listener {

    public ArenaManager arenaManager;
    public SkyWars plugin;
    public dropItemListener(SkyWars plugin){
        this.plugin=plugin;
        arenaManager=plugin.arenaManager;
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        Arena arena=plugin.arenaManager.getArenaByPlayer(event.getPlayer());
        if(arena!=null&& !arena.isGameStarted)event.setCancelled(true);
    }
}
