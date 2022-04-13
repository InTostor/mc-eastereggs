
package ru.net.explorers;

// java internal

import java.sql.*;

// minecraft server API
import org.bukkit.Bukkit;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

//external
// import org.jetbrains.annotations.NotNull;

// self-made

/**
 * Written and tested by InTostor with 🍏
 * github: https://github.com/InTostor
 *
 * Licensed with GPL-3.0 License
 *
 * Author(s):
 * 
 * @author InTostor
 *
 */
public class App extends JavaPlugin implements Listener {
    // setting environment
    Plugin plugin = this;
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    public static String pluginName = "EggCounter";
    static ConsoleWrapper cw = new ConsoleWrapper();

    // sql shit
    Database database = null;

    // Getting whole config
    String data_storage = plugin.getConfig().getString("data-storage");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        Bukkit.getPluginManager().registerEvents(this, this);

        if (data_storage != "JSON") {
            String sql_url = "jdbc:" + data_storage + "://" + plugin.getConfig().getString("sql-url");
            String sql_user = plugin.getConfig().getString("sql-user");
            String sql_password = plugin.getConfig().getString("sql-password");
            this.database = new Database(sql_url, sql_user, sql_password);
            firstStart();
        }
        cw.notify("using url:" + database.sql_url);
        cw.notify("Plugin loaded!");

    }

    private void firstStart() {
        /*
         * This function makes files (if data storage is JSON,SQLITE,etc)
         * or prepares table in MYSQL,MSSQL,POSTGRESQL,etc.
         * (available storages are in config.yml)
         */

        cw.notify(" Plugin is starting first time, or restoring!");
        cw.notify(Constants.sqlFirstStart);
        if (data_storage != "MYSQL") {

            database.connect();

            try {
                database.statement.execute(Constants.sqlFirstStart);
            } catch (SQLException e) {
                e.printStackTrace();
                cw.alarm("Cant execute starting db query. Check trace");
            } finally {
                database.disconnect();
            }

        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // this is used only for check is plugin loaded
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);

        cw.notify("joined");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();
        EquipmentSlot hand = event.getHand();

        if (hand == EquipmentSlot.HAND) {
            RmbBlockClick(player, block);
        }

    }

    private void RmbBlockClick(Player player, Block block) {

        // Throttling this helps with large amount of sql queries.
        // TODO bad code
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Egg egg = new Egg(block.getLocation(), database);
            egg.completeInfo();

            if (egg.isExists() == true) {
                player.sendMessage("You found a secret-" + egg.displayname.toString() + "-" + egg.groupname.toString());
                onEggClick(egg, player);
            }

            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    void onEggClick(Egg egg, Player player) {
        // todo sequence after clicking an egg
        // notifying player that he found a secret, if its found first time - pushing
        // this to db

        if (egg.isFoundBefore(player)) {
            player.sendMessage("You found the secret, but not first time");

        } else {
            player.sendMessage("You found a secret!");
            player.sendMessage("secret:" + egg.displayname + " which is in group:" + egg.groupname);
            String sql = "insert into " + Constants.playerTable + "(player,egg_id)values('" + player.getName() + "',"
                    + egg.id + ");";
            database.connect();
            try {
                database.statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
                cw.alarm("Cant execute starting db query. Check trace");
            } finally {
                database.disconnect();
            }
        }

    }

    // todo commands
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }

    // TODO
    void purgePlayer(Player player) {
        String sql = "delete from " + Constants.playerTable + " where player='" + player.getName() + "';";
        database.connect();
        try {
            database.statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            cw.alarm("Cant execute starting db query. Check trace");
        } finally {
            database.disconnect();
        }
    }

}

// ! blah blah blah
// ? blah blah blah
// // blah blah blah
// todo blah blah blah
// * blah blah blah
// + blah blah