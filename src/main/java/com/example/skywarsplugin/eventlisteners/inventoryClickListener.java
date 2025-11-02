package com.example.skywarsplugin.eventlisteners;

import com.example.skywarsplugin.SkyWars;
import com.example.skywarsplugin.menu.AdminMenu;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;

public class inventoryClickListener implements Listener {
    public SkyWars plugin;
    public inventoryClickListener(SkyWars plugin){
        this.plugin=plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getInventory().getSize()!=54)return;
        if(event.getCurrentItem()==null || event.getCurrentItem().getItemMeta()==null)return;
        event.setCancelled(true);
        AdminMenu m=new AdminMenu(plugin);
        String itemName= PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().itemName());
        Material itemType=event.getCurrentItem().getType();
        event.getInventory().clear();
        switch (itemName){
            case "Арены":
                m.setupArenaPage();
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(m.menuInventory);
                return;
            case "Остановить арену":
                event.getWhoClicked().closeInventory();
                plugin.arenaManager.getArenaByName(event.getView().getTitle()).stop();
                return;
            case "Запустить арену":
                event.getWhoClicked().closeInventory();
                plugin.arenaManager.getArenaByName(event.getView().getTitle()).start();
                return;
            case "Начать игру":
                event.getWhoClicked().closeInventory();
                plugin.arenaManager.getArenaByName(event.getView().getTitle()).startGame();
                return;
            case "Команды":
                m.setupTeamsPage();
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(m.menuInventory);
                return;
            case "Импортировать":
                event.getWhoClicked().closeInventory();
                plugin.teamManager.fromFile(new File(plugin.getDataFolder(),"teams.json"));
                return;
        }
        switch (itemType){
            case GREEN_WOOL, BLUE_WOOL,RED_WOOL:
                m.setupArenaActionsPage(itemName);
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(m.menuInventory);
                break;
            case PLAYER_HEAD:
                //Пиздец
                plugin.arenaManager.getArenaByName(event.getView().getTitle()).addTeam(plugin.teamManager.getTeamByName(itemName));
                m.setupArenaActionsPage(event.getView().getTitle());
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(m.menuInventory);
                break;
            case ZOMBIE_HEAD:
                plugin.arenaManager.getArenaByName(event.getView().getTitle()).delTeam(plugin.teamManager.getTeamByName(itemName));
                m.setupArenaActionsPage(event.getView().getTitle());
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(m.menuInventory);
                break;
            case SKELETON_SKULL:
                m.setupArenaActionsPage(event.getView().getTitle());
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(m.menuInventory);
                break;
        }

    }
}
