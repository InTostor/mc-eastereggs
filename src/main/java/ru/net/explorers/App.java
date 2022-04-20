
package ru.net.explorers;

// java internal

import java.sql.*;

// minecraft server API
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        cw.verboseLog(Constants.sqlFirstStart1);
        cw.verboseLog(Constants.sqlFirstStart2);
        if (data_storage != "MYSQL") {

            database.connect();

            try {
                database.statement.execute(Constants.sqlFirstStart1);
                database.statement.execute(Constants.sqlFirstStart2);
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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Egg egg = new Egg(block.getLocation(), database);
            egg.completeInfo();

            if (egg.isExists() == true) {
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

        // * at this point, code turning in a mess
        Integer foundeggs = 0;
        String sql = "select count(*) from " + Constants.playerTable + " where egg_id in ("
                + egg.getStringEggsInGroup() + ") and player='" + player.getName() + "';";

        try {
            database.connect();
            cw.verboseLog("foundeggs" + sql);
            ResultSet rs = database.statement.executeQuery(sql);

            while (rs.next()) {
                foundeggs = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            cw.alarm("Cant execute db query. Check trace");
        } finally {
            database.disconnect();
        }
        String eggmsg = egg.message;
        if (eggmsg == null) {
            eggmsg = ".";
        }

        if (foundeggs < egg.getAmountOf()) {
            foundeggs = foundeggs + 1;
        }

        String foundEgg = "§4Вы нашли секрет:§r " + egg.displayname + " §4 из группы:§r " + egg.groupname + " | §6id:"
                + egg.id + "\n" + eggmsg +
                "\nПрогресс: | " + foundeggs + "/" + egg.getAmountOf() + " |";

        if (egg.isFoundBefore(player)) {
            player.sendMessage(foundEgg);
            player.sendMessage("Но не в первый раз");

        } else {
            player.sendMessage(foundEgg);

            String sql2 = "insert into " + Constants.playerTable + "(player,egg_id)values('" + player.getName()
                    + "',"
                    + egg.id + ");";
            database.connect();
            try {
                database.statement.execute(sql2);
            } catch (SQLException e) {
                e.printStackTrace();
                cw.alarm("Cant execute starting db query. Check trace");
            }
        }

    }

    // todo commands
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch (args[0]) {
            case ("purgeplayer"): {
                purgePlayer(args[1]);
            }
                break;
            case ("addegg"): {
                Player player = (Player) sender;
                boolean res = addEgg(player.getTargetBlock(null, 5).getLocation(), args[1], args[2], args[3]);
                if (res == true) {
                    sender.sendMessage("Пасхалка создана (наверное)");
                } else {
                    sender.sendMessage("Пасхалка не создана потому что она уже существует");
                }
            }
                break;
            case ("delegg"): {
                Player player = (Player) sender;
                Egg segg = new Egg(player.getTargetBlock(null, 5).getLocation(), database);
                segg.completeInfo();
                segg.delete();
            }
                break;
            default: {
                sender.sendMessage(
                        "Такой команды нет, или была но вы ввели её неверно. Читайте гайды.\n Код ошибки: §k0");
            }
        }

        return true;
    }

    void purgePlayer(String playerName) {
        String sql = "delete from " + Constants.playerTable + " where player='" + playerName + "';";
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

    boolean addEgg(Location location, String eggDisplayName, String eggGroupName, String message) {
        // todo inserting new egg to db. coords from <insert text>
        Double locx = location.getX();
        Double locy = location.getY();
        Double locz = location.getZ();
        String wname = location.getWorld().getName();
        locx = (double) locx.intValue();
        locy = (double) locy.intValue();
        locz = (double) locz.intValue();
        message = message.replace("_", " ");
        eggDisplayName = eggDisplayName.replace("_", " ");
        eggGroupName = eggGroupName.replace("_", " ");
        Egg segg = new Egg(location, database);
        segg.completeInfo();
        if (segg.id == null) {

            String loc = String.format("%.0f", locx) + ";"
                    + String.format("%.0f", locy) + ";"
                    + String.format("%.0f", locz)
                    + ";" + wname;

            String sql = "insert into " + Constants.eggTable +
                    "(location,displayname,groupname,msg)values('" +
                    loc + "','" + eggDisplayName + "','" + eggGroupName + "','" + message + "')";
            database.connect();
            try {
                database.statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
                cw.alarm("Cant execute starting db query. Check trace");
            } finally {
                database.disconnect();
            }
            return true;

        } else {
            return false;
        }

    }

}

// ! blah blah blah
// ? blah blah blah
// // blah blah blah
// todo blah blah blah
// * blah blah blah
// + blah blah