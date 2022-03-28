package ru.net.explorers;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerInteractEvent;



public class App : JavaPlugin(),Listener{

override public fun onEnable(){
    Bukkit.getConsoleSender().sendMessage("§6[Easter counter] Plugin loaded!");
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
    saveDefaultConfig();
    reloadConfig();
}

@EventHandler
public fun onPlayerJoin(e: PlayerJoinEvent) {
    val player = e.getPlayer();

    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1f, 1f);
    Bukkit.getConsoleSender().sendMessage("§6[Easter counter] Event")

    }

@EventHandler
public  fun onPlayerInteract(e: PlayerInteractEvent){
    val player = e.getPlayer();
    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1f, 1f);
}




}
