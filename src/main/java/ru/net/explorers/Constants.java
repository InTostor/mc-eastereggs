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
    final static String sqlFirstStart = "CREATE TABLE IF NOT EXISTS `" + eggTable + "` (\n" + // eggs table
            "id int NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "location varchar(128) NOT NULL,\n" +
            "displayname tinytext,\n" +
            "groupname tinytext NOT NULL,\n" +
            "cmd tinytext,\n" +
            "UNIQUE KEY id_UNIQUE (id),\n" +
            "UNIQUE KEY location_UNIQUE (location));\n" +
            "CREATE TABLE IF NOT EXISTS `players`(\n" +
            "id varchar(45) not null key AUTO_INCREMENT,\n" +
            "  `player` varchar(64) NOT NULL,\n" +
            "`egg_id` varchar(45) DEFAULT NULL);\n";

}
