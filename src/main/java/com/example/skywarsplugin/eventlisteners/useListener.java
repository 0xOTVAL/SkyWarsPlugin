package com.example.skywarsplugin.eventlisteners;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.arena.Arena;
import com.example.skywarsplugin.arena.ArenaManager;
import com.example.skywarsplugin.team.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class useListener implements Listener {
    public ArenaManager arenaManager;
    public SkyWars plugin;
    public useListener(SkyWars plugin){
        this.plugin=plugin;
        arenaManager=plugin.arenaManager;
    }
    @EventHandler
    public void PlayerRightClick(PlayerInteractEvent event){
        if(event.getAction().isLeftClick())return;
        //get arena and player
        Player player=event.getPlayer();
        Arena arena=arenaManager.getArenaByPlayer(player);
        //we don't want to do anything if: 1)player is not on arena 2)arena is not started 3)game is already started
        if(arena==null)return;
        if(!arena.isStarted)return;
        if(arena.isGameStarted)return;
        if(event.getItem()==null)return;
        if(event.getItem().getType()== Material.DIAMOND){
            player.sendMessage(arena.startGame());
            return;
        }
        if(event.getItem().getType()== Material.SLIME_BALL ) {
            arena.logoutPlayer(player);
            return;
        }
        //get previous team and target team
        Team prev_team=arena.getTeamByPlayer(player);
        Team team=arena.getTeamByItem(event.getItem());
        //do nothing if player is already in target team
        if(prev_team==team)return;
        //remove player from previous team
        if(prev_team!=null)prev_team.removePlayer(player);
        //add player to target team
        team.addPlayer(player);
        //set player armor to armor, colored by team color
        player.getInventory().setArmorContents(team.armor);
        //send message to player
        player.sendMessage("You joined "+team.name+" team");
        event.setCancelled(true);
    }
}
