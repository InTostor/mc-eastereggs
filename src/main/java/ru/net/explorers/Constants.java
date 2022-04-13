package ru.net.explorers;

public class Constants {

    static String eggTable = "eggs";
    static String playerTable = "players";

    static String loggingLevel = "verbose";

    /**
     * ! Achtung! Changing this string will affect Egg.CompleteInfo &
     * isFoundLater(cuz there sql
     * ! query and static indexes (cuz jdbc))
     */
    static String sqlFirstStart = "CREATE TABLE IF NOT EXISTS `" + eggTable + "` (" + // eggs table
            "id int NOT NULL AUTO_INCREMENT PRIMARY KEY," +
            "location varchar(128) NOT NULL," +
            "displayname tinytext," +
            "groupname tinytext NOT NULL," +
            "cmd tinytext," +
            "UNIQUE KEY id_UNIQUE (id)," +
            "UNIQUE KEY location_UNIQUE (location));" +
            "CREATE TABLE IF NOT EXISTS `players`(" +
            "id varchar(45) not null key AUTO_INCREMENT," +
            "  `player` varchar(64) NOT NULL," +
            "`egg_id` varchar(45) DEFAULT NULL);";

}
