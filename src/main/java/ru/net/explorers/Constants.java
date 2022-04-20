package ru.net.explorers;

public class Constants {

    final static String eggTable = "eggs";
    final static String playerTable = "players";

    final static String loggingLevel = "verbose";

    /**
     * ! Achtung! Changing this string will affect everything in Egg.java
     * ! BE SUPER CAREFUL WITH IT, BECAUSE AUTHOR WAS NOT INTERESTED WITH DEV
     * COMFORT
     * ! query and static indexes (cuz jdbc))
     */
    final static String sqlFirstStart1 = "CREATE TABLE IF NOT EXISTS " + eggTable + " (\n" +
            "id int NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "location varchar(128) NOT NULL,\n" +
            "displayname tinytext,\n" +
            "groupname tinytext NOT NULL,\n" +
            "cmd tinytext,\n" +
            "msg varchar(128),\n" +
            "UNIQUE KEY id_UNIQUE (id),\n" +
            "UNIQUE KEY location_UNIQUE (location));";

    final static String sqlFirstStart2 = "CREATE TABLE IF NOT EXISTS " + playerTable + "(\n" +
            "id int NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "`player` varchar(64) NOT NULL,\n" +
            "`egg_id` varchar(45) DEFAULT NULL,\n" +
            "UNIQUE KEY id_UNIQUE (id));\n";

}
