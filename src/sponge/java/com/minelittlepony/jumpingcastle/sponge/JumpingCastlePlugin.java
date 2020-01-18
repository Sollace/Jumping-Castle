package com.minelittlepony.jumpingcastle.sponge;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.Message;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.plugin.Plugin;

import com.minelittlepony.jumpingcastle.DeserializedPayload;
import com.minelittlepony.jumpingcastle.JumpingClientImpl;
import com.minelittlepony.jumpingcastle.api.Bus;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.api.payload.BinaryPayload;

import io.netty.buffer.Unpooled;

@Plugin(
    id = "@MODID@_sponge",
    name = "@NAME@",
    authors = "@AUTHOR@",
    version = "@VERSION@",
    description = "@DESCRIPTION@"
)
public class JumpingCastlePlugin implements Bus {

    private ChannelBinding.IndexedMessageChannel channel;

    private boolean running;

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        running = JumpingClientImpl.instance().setBus(this);

        if (running) {
            channel = Sponge.getChannelRegistrar().createChannel(this, JumpingClientImpl.CHANNEL);
            channel.registerMessage(PayloadData.class, JumpingClientImpl.PROTOCOL, (message, connection, side) -> {
                if (!(connection instanceof PlayerConnection)) return;

                PlayerConnection conn = (PlayerConnection)connection;

                if (side.isClient()) {
                    JumpingClientImpl.instance().onPayload(message.payload());
                } else {
                    getServer().onPayload(conn.getPlayer().getProfile().getUniqueId(), message.payload());
                }
            });
        }
    }

    @Listener
    public void onPlayerLeave(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player) {
        if (running) {
            getServer().stopTrackingPlayer(player.getProfile().getUniqueId());
        }
    }

    @Override
    public void sendToServer(String channel, long id, com.minelittlepony.jumpingcastle.api.Message message, Target target) {
        this.channel.sendToServer(new PayloadData(BinaryPayload.of(Unpooled.buffer())
                .writeString(channel)
                .writeLong(id)
                .writeByte((byte)target.ordinal())
                .writeBinary(message)));
    }

    @Override
    public void sendToClient(UUID playerId, BinaryPayload forwarded) {
        Sponge.getServer().getPlayer(playerId).ifPresent(player -> {
            channel.sendTo(player, new PayloadData(forwarded));
        });
    }

    @Override
    public void sendToClient(String channel, long id, com.minelittlepony.jumpingcastle.api.Message message, UUID playerId) {
        Sponge.getServer().getPlayer(playerId).ifPresent(player -> {
            this.channel.sendTo(player, new PayloadData(BinaryPayload.of(Unpooled.buffer())
                    .writeString(channel)
                    .writeLong(id)
                    .writeByte((byte)Target.CLIENTS.ordinal())
                    .writeBinary(message)));
        });
    }

    class PayloadData implements Message {

        private BinaryPayload payload;

        public PayloadData() {

        }

        public PayloadData(BinaryPayload data) {
            payload = data;
        }

        @Override
        public void readFrom(ChannelBuf buf) {
            payload = ChannelBufBinaryPayload.of(buf);
        }

        @Override
        public void writeTo(ChannelBuf buf) {
            buf.writeBytes(payload.bytes());
        }

        public DeserializedPayload payload() {
            return new DeserializedPayload(JumpingClientImpl.PROTOCOL, payload);
        }
    }
}
