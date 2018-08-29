package org.bukkit.plugin.messaging;

import org.bukkit.plugin.Plugin;

public interface Messenger {
    void registerOutgoingPluginChannel(Plugin plugin, String channel);

    PluginMessageListenerRegistration registerIncomingPluginChannel(Plugin plugin, String channel, PluginMessageListener listener);
}
