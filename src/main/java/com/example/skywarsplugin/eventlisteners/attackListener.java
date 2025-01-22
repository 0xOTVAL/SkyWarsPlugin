package com.example.skywarsplugin.eventlisteners;


import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.arena.ArenaManager;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class attackListener implements Listener {
    public ArenaManager arenaManager;
    public SkyWars plugin;
    public attackListener(SkyWars plugin){
        this.plugin=plugin;
        arenaManager=plugin.arenaManager;
    }
    @EventHandler
    public void PrePlayerAttackEntity(PrePlayerAttackEntityEvent event){
        Arena arena=arenaManager.getArenaByPlayer(event.getPlayer());
        if(arena==null)return;
        if(!arena.isGameStarted)return;
        Player attackedPlayer=(Player)event.getAttacked();
        if(arena.hasTeams && arena.getTeamByPlayer(attackedPlayer)==arena.getTeamByPlayer(event.getPlayer())){
            event.setCancelled(true);
            return;
        }
    }
}
