package com.example.skywarsplugin.command;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.command.playercommands.ArenaList;
import com.example.skywarsplugin.command.playercommands.JoinArena;
import com.example.skywarsplugin.command.playercommands.LeaveArena;
import com.example.skywarsplugin.command.playercommands.StartArenaGame;
import com.example.skywarsplugin.command.setupcommands.*;

public class MainCommand {
    private final SkyWars plugin;
    public CommandExecutorBase base;
    public MainCommand(SkyWars plugin){
        this.plugin=plugin;
        this.base=new CommandExecutorBase();
        this.initCommands();
    }
    private void initCommands(){
        base.addSubCommand(new JoinArena(plugin));
        base.addSubCommand(new LeaveArena(plugin));
        base.addSubCommand(new StartArena(plugin));
        base.addSubCommand(new StartArenaGame(plugin));
        base.addSubCommand(new StopArena(plugin));
        base.addSubCommand(new ArenaList(plugin));
        base.addSubCommand(new AddIsland(plugin));
        base.addSubCommand(new CreateArena(plugin));
        base.addSubCommand(new SaveArena(plugin));
        base.addSubCommand(new AddTeam(plugin));
        base.addSubCommand(new SetLobbypos(plugin));
        base.addSubCommand(new SetPos1(plugin));
        base.addSubCommand(new SetPos2(plugin));
        base.addSubCommand(new SetGameTime(plugin));
        base.addSubCommand(new GiveAdminMenuItem(plugin));
        base.addSubCommand(new GiveSpecMenuItem(plugin));
    }
}
