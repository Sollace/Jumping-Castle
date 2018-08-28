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

import com.minelittlepony.jumpingcastle.IMessageBus;
import com.minelittlepony.jumpingcastle.JumpingCastleImpl;
import com.minelittlepony.jumpingcastle.JumpingServer;
import com.minelittlepony.jumpingcastle.api.IMessage;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.payload.DeserializedPayload;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class JumpingCastlePlugin extends JavaPlugin implements Listener, IMessageBus  {

    private boolean running;

    @Override
    public void onEnable() {
        running = JumpingCastleImpl.instance().setBus(this);

        if (!running) return;

        Bukkit.getPluginManager().registerEvents(this, this);

        Messenger messenger = Bukkit.getMessenger();

        messenger.registerIncomingPluginChannel(this, JumpingCastleImpl.CHANNEL, (channel, player, bytes) -> {
            JumpingServer.instance().onPayload(player.getUniqueId(), new DeserializedPayload(IBinaryPayload.of(Unpooled.wrappedBuffer(bytes))));
        });
        messenger.registerOutgoingPluginChannel(this, JumpingCastleImpl.CHANNEL);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (running) {
            JumpingServer.instance().onPlayerLeave(event.getPlayer().getUniqueId());
        }
    }

    @Override
    public void sendToServer(String channel, long id, IMessage message, Target target) {

    }

    @Override
    public void sendToClient(UUID playerId, IBinaryPayload forwarded) {
        Player player = Bukkit.getPlayer(playerId);

        if (player == null) {
            return;
        }

        ByteBuf buffer = ((ByteBuf)forwarded.buff());

        player.sendPluginMessage(this, JumpingCastleImpl.CHANNEL, Arrays.copyOf(buffer.array(), buffer.writerIndex()));
    }
}
