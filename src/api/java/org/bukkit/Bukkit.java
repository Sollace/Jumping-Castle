package org.bukkit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;

public interface Bukkit {
    static PluginManager getPluginManager() { return null; }

    static Messenger getMessenger() { return null; }

    static Player getPlayer(UUID id) { return null; }
}
