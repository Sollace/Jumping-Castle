package org.bukkit.entity;

import java.util.UUID;

import org.bukkit.plugin.Plugin;

public interface Player {
    UUID getUniqueId();

    void sendPluginMessage(Plugin source, String channel, byte[] message);
}
