package com.minelittlepony.jumpingcastle.bukkit;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import com.minelittlepony.jumpingcastle.DeserializedPayload;
import com.minelittlepony.jumpingcastle.JumpingClientImpl;
import com.minelittlepony.jumpingcastle.api.Bus;
import com.minelittlepony.jumpingcastle.api.Message;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.api.payload.BinaryPayload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class JumpingCastlePlugin extends JavaPlugin implements Listener, Bus  {

    private boolean running;

    @Override
    public void onEnable() {
        running = JumpingClientImpl.instance().setBus(this);

        if (!running) return;

        Bukkit.getPluginManager().registerEvents(this, this);

        Messenger messenger = Bukkit.getMessenger();

        messenger.registerIncomingPluginChannel(this, JumpingClientImpl.CHANNEL, (channel, player, bytes) -> {
            getServer().onPayload(player.getUniqueId(), new DeserializedPayload(BinaryPayload.of(Unpooled.wrappedBuffer(bytes))));
        });
        messenger.registerOutgoingPluginChannel(this, JumpingClientImpl.CHANNEL);
    }

    @Override
    public Object getMinecraftServer() {
        return null; // TODO: God dammit, bukkit
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (running) {
            getServer().stopTrackingPlayer(event.getPlayer().getUniqueId());
        }
    }

    @Override
    public void sendToServer(String channel, long id, Message message, Target target) {

    }

    @Override
    public void sendToClient(UUID playerId, BinaryPayload forwarded) {
        Player player = Bukkit.getPlayer(playerId);

        if (player == null) {
            return;
        }

        ByteBuf buffer = forwarded.buff();

        player.sendPluginMessage(this, JumpingClientImpl.CHANNEL, Arrays.copyOf(buffer.array(), buffer.writerIndex()));
    }

    @Override
    public void sendToClient(String channel, long id, Message message, UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);

        if (player == null) {
            return;
        }

        ByteBuf buffer = BinaryPayload.create()
                .writeByte(JumpingClientImpl.PROTOCOL)
                .writeString(channel)
                .writeLong(id)
                .writeByte((byte)Target.CLIENTS.ordinal())
                .writeBinary(message).buff();

        player.sendPluginMessage(this, JumpingClientImpl.CHANNEL, Arrays.copyOf(buffer.array(), buffer.writerIndex()));
    }
}
