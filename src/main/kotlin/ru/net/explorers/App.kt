package ru.net.explorers;

// java internal
import java.lang.Enum.*;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.sql.SQLException;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.Statement;
import java.sql.*;
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
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.event.block.SignChangeEvent;




public class App : JavaPlugin(),Listener{
    val console=Bukkit.getConsoleSender()
    var pluginName: String = "NA";


override public fun onEnable(){
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
    val plugin = this;
    this.pluginName = "EggCounter";
    saveDefaultConfig();
    reloadConfig();
    


    console.sendMessage(("§6["+pluginName+"] Plugin loaded!"));
    
    


    val eggDb = File(plugin.getDataFolder(), "db.sqlite");
    if( !(eggDb.exists() && !eggDb.isDirectory()) ){
    this.firstStart(eggDb);
}



}

@EventHandler
public fun onPlayerJoin(e: PlayerJoinEvent) {
    val player = e.getPlayer();

    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1f, 1f);


    }

@EventHandler
public  fun onPlayerInteract(event: PlayerInteractEvent){    
    val player = event.getPlayer();
    val action = event.getAction();
    val block = event.getClickedBlock();

    
    if (block?.getType() == Material.BEDROCK){
        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 2f, 0.75f);
        console.sendMessage("§6[Easter counter] someone clicked on bedrock");
        console.sendMessage(player.displayName())
        player.sendMessage("You found a secret!");
    }




}



fun isEggExists(){

}


fun isEggFoundLater(){

}


fun onEggClick(){
    
}


fun firstStart(eggDb: File){
    // make eggDb.json
    console.sendMessage(("§6["+pluginName+"] Plugin is starting first time, or !"))
    try {
        eggDb.createNewFile();
    } catch (e: IOException) {
        e.printStackTrace();
    }

    dbConnect(eggDb);



}

public fun dbConnect(database: File) {
    var conn: Connection;
    var url: String = "jdbc:sqlite:"+ database.toString();
    conn = DriverManager.getConnection(url);
    val stmnt=conn.createStatement();


    conn.close();
    console.sendMessage("connected and disconnected")
            
}


} 
// end of main class


// egg object
public class Egg{
    var id: String = "Null";
    var group_id: String = "Null";
    var locationX: Double = 0.0;
    var locationY: Double = 0.0;
    var locationZ: Double = 0.0;
    var locationWorld: String = "Null";

    fun isExists(){
        
    }

}

public class sqliteDb(var file: File){
    var url: String = "jdbc:sqlite:"+ this.file.toString();
    var conn: Connection = DriverManager.getConnection(url);
    var stmt: Statement = conn.createStatement();

    
    fun exec(statement: String,operationType: String){
        val rs: ResultSet = stmt.executeQuery(statement); //Type mismatch: inferred type is ResultSet but Unit was expected
        return rs;
    }



    fun close(){
        this.conn.close();
    }
    

}


