package ru.net.explorers;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class tests
        extends JavaPlugin
        implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§6[Joiner] Plugin loaded!");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        reloadConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);

    }

    @Override
    public void onDisable() {

    }
}
