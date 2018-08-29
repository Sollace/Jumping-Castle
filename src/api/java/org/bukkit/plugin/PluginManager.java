package org.bukkit.plugin;

import org.bukkit.event.Listener;

public interface PluginManager {
    void registerEvents(Listener var1, Plugin var2);
}
