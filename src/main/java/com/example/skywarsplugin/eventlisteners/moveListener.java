package com.example.skywarsplugin.eventlisteners;


import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.arena.ArenaManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class moveListener implements Listener{
    public ArenaManager arenaManager;
    public SkyWars plugin;
    public moveListener(SkyWars plugin){
        this.plugin=plugin;
        arenaManager=plugin.arenaManager;
    }
    @EventHandler
    public void PlayerMove(PlayerMoveEvent event) {
        //get arena
        Arena arena = arenaManager.getArenaByPlayer(event.getPlayer());
        if (arena == null) return;
        if (event.getTo().getBlock().getType()== Material.AIR) {
            //if player is outside barrier and game has not started teleport in to lobby
            if (!arena.isGameStarted) {
                Location loc = new Location(arena.world, arena.lobbypos.x, arena.lobbypos.y, arena.lobbypos.z);
                event.getPlayer().teleport(loc);
            }
        }
    }
}