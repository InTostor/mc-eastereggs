package ru.net.explorers;

// java internal
import java.lang.Enum.*;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.lang.Exception;

// minecraft server API
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.event.block.SignChangeEvent;

//external
import org.jetbrains.annotations.NotNull;

// self-made
import ru.net.explorers.*;

public class App extends JavaPlugin implements Listener {
    Plugin plugin = this;
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    String pluginName = "EggCounter";

    String mysql_url = plugin.getConfig().getString("sql-url");
    String mysql_user = plugin.getConfig().getString("sql-user");
    String mysql_password = plugin.getConfig().getString("sql-password");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        File eggDb = new File(plugin.getDataFolder(), "db.sqlite");
        if (!(eggDb.exists() && !eggDb.isDirectory())) {
            this.firstStart(eggDb);
        }

        console.sendMessage(("§6[" + pluginName + "] Plugin loaded!"));
        console.sendMessage(("§6[" + pluginName + "] url:" + mysql_url));

    }

    void firstStart(File eggDb) {
        // make eggDb.json
        console.sendMessage(("§6[" + pluginName + "] Plugin is starting first time, or !"));
        try {
            eggDb.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO connection to mysql and make table

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // this is used only for check is plugin loaded
        Player player = event.getPlayer();
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);
    }

}
