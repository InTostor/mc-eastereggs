package ru.net.explorers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Egg {
    ConsoleWrapper cw = new ConsoleWrapper();
    Integer id = null;
    String displayname = null;
    String groupname = null;
    String message = null;
    Integer locationX = null;
    Integer locationY = null;
    Integer locationZ = null;
    String world = null;
    String cmd = null;
    Database database = null;
    private String locationStr = null;

    public Egg(Location location, Database database) {
        this.locationX = location.getBlockX();
        this.locationY = location.getBlockY();
        this.locationZ = location.getBlockZ();
        this.world = location.getWorld().getName();
        this.database = database;
    }

    // constructors
    public Egg(int X, int Y, int Z, String world, Database database) {
        this.locationX = X;
        this.locationY = Y;
        this.locationZ = Z;
        this.world = world;
        this.database = database;
        completeInfo();

    }

    public Egg(String egg_name, String egg_group, Database database) {
        // todo get whole info from db or json
        this.database = database;
        completeInfo();
    }

    public Egg(int id, Database database) {
        // todo get whole info by egg key from db
        this.id = id;
        this.database = database;
        completeInfo();
    }

    public boolean isExists() {
        // * this is very rough realistation of isExists. Remake it better
        completeInfo();
        if (locationX != null && locationY != null && locationZ != null && id != null && groupname != null) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isFoundBefore(Player player) {
        String sql = "select egg_id from " + Constants.playerTable + " where(player='" + player.getName() + "');";
        cw.verboseLog(sql);
        database.connect();
        boolean contains = false;
        try {
            ResultSet rs = database.statement.executeQuery(sql);// ! something wrong there
            List<Integer> playerEggs = new ArrayList<Integer>();
            while (rs.next()) {
                playerEggs.add(rs.getInt(1));
            }
            if (playerEggs.contains(this.id)) {
                contains = true;
            } else {
                contains = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            cw.alarm("Cant execute db query. Check trace");
        }
        database.disconnect();
        return contains;
    }

    void completeInfo() {

        // if we have only block location. (prefer this)
        if (this.id == null || this.groupname == null || this.displayname == null) {

            String loc = locationX.toString() + ";" + locationY.toString() + ";" + locationZ.toString() + ";"
                    + world.toString();
            cw.verboseLog(loc);
            String sql = "select * from " + Constants.eggTable + " WHERE location='" + loc + "'";
            cw.verboseLog(sql);

            database.connect();
            try {
                ResultSet rs = database.statement.executeQuery(sql);

                while (rs.next()) {
                    this.id = rs.getInt(1);
                    this.locationStr = rs.getString(2);
                    this.displayname = rs.getString(3);
                    this.groupname = rs.getString(4);
                    this.cmd = rs.getString(5);
                    this.message = rs.getString(6);

                }

            } catch (SQLException e) {
                e.printStackTrace();
                cw.alarm("Cant execute db query. Check trace");
            } finally {
                database.disconnect();
            }

            // if we have only id
        } else if (this.id != null && locationX == null) {
            String sql = "select * from " + Constants.eggTable + " WHERE id='" + id + "'";
            database.connect();
            try {
                ResultSet rs = database.statement.executeQuery(sql);

                while (rs.next()) {
                    this.locationStr = rs.getString(2);
                    this.displayname = rs.getString(3);
                    this.groupname = rs.getString(4);
                    this.cmd = rs.getString(5);
                    this.message = rs.getString(6);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                cw.alarm("Cant execute db query. Check trace");
            } finally {
                database.disconnect();
            }

            this.locationX = Integer.parseInt(locationStr.split(";")[0]);
            this.locationY = Integer.parseInt(locationStr.split(";")[1]);
            this.locationZ = Integer.parseInt(locationStr.split(";")[2]);
            this.world = locationStr.split(";")[3];

        }

    }

    String getStringEggsInGroup() {
        List<Integer> l = new ArrayList<>();
        String sql = "select id from " + Constants.eggTable + " where groupname='" + this.groupname + "'";
        database.connect();
        try {
            ResultSet rs = database.statement.executeQuery(sql);

            while (rs.next()) {
                l.add(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            cw.alarm("Cant execute db query. Check trace");
        } finally {
            database.disconnect();
        }

        String out = l.toString().replace("[", "").replace("]", "");
        return out;
    }

    Integer getAmountOf() {
        // after that, getting amount of eggs in group
        String sql = "SELECT COUNT(*) FROM " + Constants.eggTable + " WHERE groupname='" + this.groupname + "';";
        cw.verboseLog(sql);
        Integer amount = 0;
        database.connect();
        try {
            ResultSet rs = database.statement.executeQuery(sql);

            while (rs.next()) {
                amount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            cw.alarm("Cant execute db query. Check trace");
        } finally {
            database.disconnect();
        }

        return amount;
    }

    public void delete() {
        String sql = "delete from " + Constants.eggTable + " where id=" + this.id;
        cw.verboseLog(sql);
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
