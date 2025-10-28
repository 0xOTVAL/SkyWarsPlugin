package com.example.skywarsplugin.arena;

import com.example.skywarsplugin.team.Team;
import com.example.skywarsplugin.team.TeamData;
import com.mojang.brigadier.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.io.*;
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
    public Location respawn_loc=new Location(Bukkit.getWorld("world"),0,75,0);
    public String name,worldname;
    public World worldcopy;;
    public Integer gameTime,activeGameTime;

    public ArrayList<Vector3f> islandpos=new ArrayList<>();
    public ArrayList<Player> players=new ArrayList<>();
    public ArrayList<Team> teams=new ArrayList<>();
    public ArrayList<Team> activeTeams=new ArrayList<>();
    public ArrayList<Player> spectators=new ArrayList<>();
    public HashMap<Player, ItemStack[]> playerInventory=new HashMap<>();

    //Термоядерный костыль для нормальной перезагрузки мира
    int worldcopyindex=0;

    public Arena(@NotNull ArenaData data){
        name = data.name;

        worldname=data.world;


        for(String pos:data.islandpos){
            islandpos.add(new Vector3f(Float.parseFloat(pos.split(",")[0]),Float.parseFloat(pos.split(",")[1]),Float.parseFloat(pos.split(",")[2])));
        }
        lobbypos=new Vector3f(Float.parseFloat(data.lobbypos.split(",")[0]),Float.parseFloat(data.lobbypos.split(",")[1]),Float.parseFloat(data.lobbypos.split(",")[2]));

        for(TeamData td: data.teams){
            teams.add(new Team(td));
        }
        hasTeams=!teams.isEmpty();
        //Чистим директории прошлых арен чтобы не возникало ошибок
        clearOldWorldsDirectorys();
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
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
        //Teleport player to lobby
        player.teleport(new Location(worldcopy,lobbypos.x,lobbypos.y,lobbypos.z));
        //Backup player inventory
        playerInventory.put(player,player.getInventory().getContents());
        player.getInventory().clear();
        //Add team banners to players inventory
        for(Team i:teams){
            player.getInventory().addItem(i.banner);
        }
        ItemStack startgameitem=new ItemStack(Material.DIAMOND);
        ItemMeta startgameitemmeta=startgameitem.getItemMeta();
        startgameitemmeta.setDisplayName(ChatColor.BLUE+"Start game");
        startgameitem.setItemMeta(startgameitemmeta);
        ItemStack exititem=new ItemStack(Material.SLIME_BALL);
        ItemMeta exititemmeta=exititem.getItemMeta();
        exititemmeta.setDisplayName(ChatColor.RED+"Exit");
        exititem.setItemMeta(exititemmeta);
        player.getInventory().setItem(7,startgameitem);
        player.getInventory().setItem(8,exititem);
        //Set player gamemode to survival
        player.setGameMode(GameMode.ADVENTURE);
        isFull=(players.size() > islandpos.size());
        return "You joined "+name;
    }
    public String startGame(){
        if(players.isEmpty())return "Can not start arena with no players";
        if(!isStarted)return "Arena is not started";
        ArrayList<Vector3f> tmpislandpos=new ArrayList<>(islandpos);
        for (Player p : players)  {
            p.setGameMode(GameMode.SURVIVAL);
            if(hasTeams){
                Team t = getTeamByPlayer(p);
                if (t == null) {
                    return "There are players with no team";
                } else {

                    if (!activeTeams.contains(t)) activeTeams.add(t);
                }
                if (activeTeams.size() < 2) {
                    activeTeams.clear();
                    return "There must be at least 2 teams with players in them";
                }
            }
        }
        for(Player p: players) {
            p.setSaturation(5);
            p.setHealth(p.getMaxHealth());
            //Cleat players inventory
            p.getInventory().clear();
            //teleport player to its spawn
            Vector3f spawn=tmpislandpos.get(rand.nextInt(tmpislandpos.size()));
            p.teleportAsync(new Location(worldcopy,spawn.x,spawn.y,spawn.z));
            tmpislandpos.remove(spawn);
        }
        isGameStarted=true;
        return "Game on arena "+name+" started successful";
    }

    private void stopGame(){
        isGameStarted=false;
        String msg;
        if(!hasTeams){
            msg="Some player won";
        }
        else{
            msg="Team"+activeTeams.getFirst().name;
        }
        while(!players.isEmpty()){
            Player p=players.getFirst();
            p.showTitle(Title.title(Component.text(msg,TextColor.color(0,0,255)),Component.text("")));

           returnPlayer(p);
        }
        spectators.clear();
        activeTeams.clear();
        //reload world
        if(worldcopy!=null){
            File oldworld=worldcopy.getWorldFolder();
            unloadWorld(worldcopy);
        }
        worldcopyindex++;
        copyWorld(Bukkit.getWorld(worldname),worldname+"_swtmp_"+worldcopyindex);
        worldcopy=Bukkit.getWorld(worldname+"_swtmp_"+worldcopyindex);
        worldcopy.setTime(1000);
        worldcopy.setStorm(false);
        worldcopy.setThundering(false);
        worldcopy.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
        worldcopy.setGameRule(GameRule.DO_MOB_SPAWNING,false);
        worldcopy.setGameRule(GameRule.DO_WEATHER_CYCLE,false);
        worldcopy.setGameRule(GameRule.MOB_GRIEFING,false);
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
        if(worldcopy!=null){
            File oldworld=worldcopy.getWorldFolder();
            unloadWorld(worldcopy);
            deleteDirectory(oldworld);
        }
        copyWorld(Bukkit.getWorld(worldname),worldname+"_swtmp_"+worldcopyindex);
        worldcopy=Bukkit.getWorld(worldname+"_swtmp_"+worldcopyindex);
        isStarted=true;
        worldcopy.setTime(1000);
        worldcopy.setStorm(false);
        worldcopy.setThundering(false);
        worldcopy.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
        worldcopy.setGameRule(GameRule.DO_MOB_SPAWNING,false);
        worldcopy.setGameRule(GameRule.DO_WEATHER_CYCLE,false);
        worldcopy.setGameRule(GameRule.MOB_GRIEFING,false);
        return "Arena "+name+" has started";
    }
    public String stop(){
        if(!players.isEmpty())return "There are players on arena";
        if(worldcopy!=null){
            unloadWorld(worldcopy);
            worldcopy.getWorldFolder().delete();
        }
        isStarted=false;
        return "Arena "+name+" stopped";
    }
    public void killPlayer(Player player){
        player.setGameMode(GameMode.SPECTATOR);
        spectators.add(player);
        if(hasTeams && getTeamByPlayer(player)!=null)getTeamByPlayer(player).removePlayer(player);
        player.teleport(new Location(worldcopy,lobbypos.x,lobbypos.y,lobbypos.z));
        player.showTitle(Title.title(Component.text("You died", TextColor.color(255,0,0)),Component.text("")));

        if(hasTeams)activeTeams.removeIf(t -> t.players.isEmpty());
        if(hasTeams && activeTeams.size() == 1) {
            stopGame();
            return;
        }
        if(spectators.size()>= players.size()-1){
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
        if(players.size()<=1){
            stopGame();
            return;
        }
    }
    private static void copyFileStructure(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean unloadWorld(World world) {
        return world!=null && Bukkit.getServer().unloadWorld(world, false);
    }
    public static void copyWorld(World originalWorld, String newWorldName) {
        copyFileStructure(originalWorld.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorldName));
        new WorldCreator(newWorldName).createWorld();
    }
    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
    private void clearOldWorldsDirectorys(){
        File dir = Bukkit.getWorldContainer();
        File [] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(worldname+"_swtmp");
            }
        });
        for(File f:files){
            deleteDirectory(f);
        }
    }

}
