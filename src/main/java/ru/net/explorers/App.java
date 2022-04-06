/*
 *   Written and tested by InTostor with 🍏
 *   github: https://github.com/InTostor
 *
 *   Licensed with GPL-3.0 License
 *
 *
 *
*/

package ru.net.explorers;

// java internal

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// minecraft server API
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

//external
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonParser;

// self-made
import ru.net.explorers.*;

//
//
//

public class App extends JavaPlugin implements Listener {
    // setting environment
    Plugin plugin = this;
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    String pluginName = "EggCounter";

    // sql shit
    Connection con;
    Statement stmt;
    ResultSet rs;

    // Getting whole config
    String data_storage = plugin.getConfig().getString("data-storage");

    String sql_url;
    String sql_user;
    String sql_password;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        if (data_storage != "JSON") {
            this.sql_url = "jdbc:" + data_storage + "://" + plugin.getConfig().getString("sql-url");
            this.sql_user = plugin.getConfig().getString("sql-user");
            this.sql_password = plugin.getConfig().getString("sql-password");
            firstStart();
        }
        notify("using url:" + sql_url);
        notify("Plugin loaded!");

    }

    void firstStart() {
        /*
         * This function makes files (if data storage is JSON,SQLITE,etc)
         * or prepares table in MYSQL,MSSQL,POSTGRESQL,etc.
         * (available storages are in config.yml)
         */

        notify(" Plugin is starting first time, or restoring!");

        if (data_storage != "MYSQL") {
            String sqlStatement = "CREATE TABLE IF NOT EXISTS`eggs`" +
                    "(`id` int NOT NULL AUTO_INCREMENT," +
                    "`name` tinytext," +
                    "`location` varchar(45) NOT NULL," +
                    "`group` tinytext," +
                    "`cmd` tinytext," +
                    "PRIMARY KEY (`id`)," +
                    "UNIQUE KEY `ideggs_UNIQUE` (`id`)," +
                    "UNIQUE KEY `location_UNIQUE` (`location`))";

            sqlConnect();

            // executing SELECT query
            try {
                stmt.execute(sqlStatement);
            } catch (SQLException e) {
                e.printStackTrace();
                alarm("Cant execute starting db query. Check trace");
            }
            sqlDisconnect();

        }

        // todo connection to sql and make table

    }

    // * Work with databases. Wrappers and methods
    // todo make these functions compatible with SQLITE and MSSQL
    void sqlConnect() {
        // This function opens sql db for whole class, make sure you know what you are
        // doing using this.
        // because this can have some vulnerabilities
        try {
            // opening database connection to sql server
            this.con = DriverManager.getConnection(sql_url, sql_user, sql_password);

            // getting Statement object to execute query
            this.stmt = con.createStatement();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            alarm("Something wrong with database connection. Check stack trace, config and other shit");
        }
    }

    void sqlDisconnect() {
        // This disconnects the
        try {
            con.close();
            stmt.close();
        } catch (SQLException se) {
            alarm("Database not closed, but should. Summon soul of communism to solve this");
        }

        this.con = null;
        this.stmt = null;
        this.rs = null;
    }

    // messages, logging and other
    void alarm(String msg) {
        // wrapper for console messages
        console.sendMessage(("§c[" + pluginName + "]" + msg));
    }

    void notify(String msg) {
        // wrapper for console messages
        console.sendMessage(("§6[" + pluginName + "]" + msg));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // this is used only for check is plugin loaded
        Player player = event.getPlayer();
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);
    }

}

// ! blah blah blah
// ? blah blah blah
// // blah blah blah
// todo blah blah blah
// * blah blah blah
// + blah blah