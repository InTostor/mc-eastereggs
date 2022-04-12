package ru.net.explorers;

// For unknown (yet) reason, message functions adding letter B in the start of line, before [PluginName]

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import ru.net.explorers.*;

public class ConsoleWrapper {
    static String pluginName = App.pluginName;
    static ConsoleCommandSender console = Bukkit.getConsoleSender();

    // messages, logging and other

    public void notify(String msg) {
        // wrapper for console messages
        console.sendMessage((ChatColor.GOLD + "[" + pluginName + "] " + msg));
    }

    public void alarm(String msg) {
        // wrapper for console messages
        console.sendMessage(ChatColor.RED + "[" + pluginName + "] " + msg);
    }

    public void debug(String msg) {
        // wrapper for console messages
        console.sendMessage(ChatColor.AQUA + "[" + pluginName + "][DEBUG] " + msg);
    }

    public void verboseLog(String msg) {
        if (Constants.loggingLevel == "verbose") {
            console.sendMessage(ChatColor.AQUA + "[" + pluginName + "][DEBUG] " + msg);
        }

    }
}
