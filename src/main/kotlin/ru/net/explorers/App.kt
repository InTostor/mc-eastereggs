package ru.net.explorers;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.inventory.EquipmentSlot;

import java.lang.Enum.*;



public class App : JavaPlugin(),Listener{
    val console=Bukkit.getConsoleSender()

override public fun onEnable(){
    console.sendMessage("§6[Easter counter] Plugin loaded!");
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
    saveDefaultConfig();
    reloadConfig();
}

@EventHandler
public fun onPlayerJoin(e: PlayerJoinEvent) {
    val player = e.getPlayer();

    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1f, 1f);
    console.sendMessage("§6[Easter counter] Event")

    }

@EventHandler
public  fun onPlayerInteract(event: PlayerInteractEvent){    
    val player = event.getPlayer();
    val block = event.getBlock()
    val action = event.getAction()

    if action.isRightClick(){
    
    if (block?.getType() == Material.BEDROCK){
        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 2f, 0.75f);
        console.sendMessage("§6[Easter counter] someone clicked on bedrock");
        console.sendMessage(player.displayName())
        player.sendMessage("You found a secret!");
    }


}


}

}