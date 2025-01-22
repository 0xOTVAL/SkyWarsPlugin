package com.example.skywarsplugin.arena;

import com.example.skywarsplugin.team.Team;
import com.example.skywarsplugin.team.TeamData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Arena {
    public Random rand=new Random();
    public Boolean isStarted=false;
    public Boolean isGameStarted=false;
    public Boolean isFull=false;
    public Boolean hasTeams=false  ;

    public Vector3f pos1,pos2,lobbypos;
    public Location respawn_loc=new Location(Bukkit.getWorld("world"),0,200,0);
    public String name;
    public World world;
    public Integer gameTime,activeGameTime;

    public ArrayList<Vector3f> islandpos=new ArrayList<>();
    public ArrayList<Player> players=new ArrayList<>();
    public ArrayList<Team> teams=new ArrayList<>();
    public ArrayList<Team> activeTeams=new ArrayList<>();
    public ArrayList<Player> spectators=new ArrayList<>();
    public HashMap<Player, ItemStack[]> playerInventory=new HashMap<>();


    public Arena(@NotNull ArenaData data){
        name = data.name;
        world = Bukkit.getWorld(data.world);

        for(String pos:data.islandpos){
            islandpos.add(new Vector3f(Float.parseFloat(pos.split(",")[0]),Float.parseFloat(pos.split(",")[1]),Float.parseFloat(pos.split(",")[2])));
        }
        lobbypos=new Vector3f(Float.parseFloat(data.lobbypos.split(",")[0]),Float.parseFloat(data.lobbypos.split(",")[1]),Float.parseFloat(data.lobbypos.split(",")[2]));

        for(TeamData td: data.teams){
            teams.add(new Team(td));
        }
        hasTeams=!teams.isEmpty();
    }
    public Team getTeamByPlayer(Player player){
        for(Team t: teams){
            if(t.players.contains(player))return t;
        }
        return null;
    }
    public Team getTeamByItem(ItemStack item){
        for(Team t: teams){
            if(t.banner.getType()==item.getType())return t;
        }
        return null;
    }
    public String addPlayer(Player player){
        //If arena is not started or game on arena has started or arena is full we cannot add player
        if(!isStarted)return "Arena "+name+" has not started";
        if(isGameStarted)return "Can not join to started game";
        if(isFull)return "Arena "+name+" is full :-(";
        //Add player to array
        players.add(player);
        //Teleport player to lobby
        player.teleport(new Location(world,lobbypos.x,lobbypos.y,lobbypos.z));
        //Backup player inventory
        playerInventory.put(player,player.getInventory().getContents());
        player.getInventory().clear();
        //Add team banners to players inventory
        for(Team i:teams){
            player.getInventory().addItem(i.banner);
        }
        player.getInventory().setItem(7,new ItemStack(Material.DIAMOND));
        player.getInventory().setItem(8,new ItemStack(Material.SLIME_BALL));
        //Set player gamemode to survival
        player.setGameMode(GameMode.SURVIVAL);
        isFull=(players.size() > islandpos.size());
        return "You joined "+name;
    }
    public String startGame(){
        if(players.isEmpty())return "Can not start arena with no players";
        if(!isStarted)return "Arena is not started";
        ArrayList<Vector3f> tmpislandpos=new ArrayList<>(islandpos);
        if(hasTeams) {
            for (Player p : players) {
                Team t = getTeamByPlayer(p);
                if (t == null) {
                    return "There are players with no team";
                } else {

                    if (!activeTeams.contains(t)) activeTeams.add(t);
                }
            }

            if (activeTeams.size() < 2) {
                activeTeams.clear();
                return "There must be at least 2 teams with players in them";
            }
        }
        for(Player p: players) {
            //Cleat players inventory
            p.getInventory().clear();
            //teleport player to its spawn
            Vector3f spawn=tmpislandpos.get(rand.nextInt(tmpislandpos.size()));
            p.teleportAsync(new Location(world,spawn.x,spawn.y,spawn.z));
            tmpislandpos.remove(spawn);
        }
        isGameStarted=true;
        return "Game on arena "+name+" started successful";
    }

    private void stopGame(){
        isGameStarted=false;
        for(Player p : players){
            if(hasTeams)p.showTitle(Title.title(Component.text("Team "+activeTeams.getFirst().name+" won",TextColor.color(activeTeams.getFirst().color.asRGB())),Component.text("")));
            else p.showTitle(Title.title(Component.text("Player "+players.getFirst().name()+" won",TextColor.color(activeTeams.getFirst().color.asRGB())),Component.text("")));
            returnPlayer(p);
        }
        spectators.clear();
        activeTeams.clear();
    }
    public void returnPlayer(Player player){
        if(!players.contains(player))return;
        //restore player inventory
        player.getInventory().setContents(playerInventory.get(player));
        playerInventory.remove(player);
        if(hasTeams && getTeamByPlayer(player)!=null)getTeamByPlayer(player).removePlayer(player);
        //teleport player to respawn location
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(respawn_loc);

        players.remove(player);
    }
    public String start(){
        if(isStarted)return "Arena is already started";
        isStarted=true;
        return "Arena "+name+" has started";
    }
    public String stop(){
        if(!players.isEmpty())return "There are players on arena";
        isStarted=false;
        return "Arena "+name+" stopped";
    }
    public void killPlayer(Player player){
        player.setGameMode(GameMode.SPECTATOR);
        spectators.add(player);
        if(hasTeams && getTeamByPlayer(player)!=null)getTeamByPlayer(player).removePlayer(player);
        player.teleport(new Location(world,lobbypos.x,lobbypos.y,lobbypos.z));
        player.showTitle(Title.title(Component.text("You died", TextColor.color(255,0,0)),Component.text("")));

        if(hasTeams)activeTeams.removeIf(t -> t.players.isEmpty());
        if(hasTeams && activeTeams.size() == 1) {
            stopGame();
            return;
        }
        if(players.size()==1){
            stopGame();
            return;
        }
    }
    public void logoutPlayer(Player player){
        if(hasTeams)activeTeams.removeIf(t -> t.players.isEmpty());
        returnPlayer(player);
        if(hasTeams && activeTeams.size() == 1) {
            stopGame();
            return;
        }
        if(players.size()==1){
            stopGame();
            return;
        }
    }
}
