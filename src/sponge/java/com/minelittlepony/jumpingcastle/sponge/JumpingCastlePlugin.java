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

import com.minelittlepony.jumpingcastle.IMessageBus;
import com.minelittlepony.jumpingcastle.JumpingCastleImpl;
import com.minelittlepony.jumpingcastle.JumpingServer;
import com.minelittlepony.jumpingcastle.api.IMessage;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.payload.DeserializedPayload;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;

@Plugin(
    id = "@MODID@",
    name = "@NAME@",
    authors = "@AUTHOR@",
    version = "@VERSION@",
    description = "@DESCRIPTION@"
)
public class JumpingCastlePlugin implements IMessageBus {

    private ChannelBinding.IndexedMessageChannel channel;

    private boolean running;

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        running = JumpingCastleImpl.instance().setBus(this);

        if (running) {
            channel = Sponge.getChannelRegistrar().createChannel(this, JumpingCastleImpl.CHANNEL);
            channel.registerMessage(PayloadData.class, 0, (message, connection, side) -> {
                if (!(connection instanceof PlayerConnection)) return;

                PlayerConnection conn = (PlayerConnection)connection;

                JumpingServer.instance().onPayload(conn.getPlayer().getUniqueId(), new DeserializedPayload(message.payload));
            });
        }
    }

    @Listener
    public void onPlayerLeave(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player) {
        if (running) {
            JumpingServer.instance().onPlayerLeave(player.getUniqueId());
        }
    }

    @Override
    public void sendToServer(String channel, long id, IMessage message, Target target) {
        this.channel.sendToServer(new PayloadData(IBinaryPayload.of(new PacketBuffer(Unpooled.buffer()))
                .writeString(channel)
                .writeLong(id)
                .writeByte((byte)target.ordinal())
                .writeBinary(message).buff()));
    }

    @Override
    public void sendToClient(UUID playerId, IBinaryPayload forwarded) {
        Sponge.getServer().getPlayer(playerId).ifPresent(player -> {
            channel.sendTo(player, new PayloadData(forwarded));
        });
    }

    class PayloadData implements Message {

        IBinaryPayload payload;

        public PayloadData() {

        }

        public PayloadData(IBinaryPayload payload) {
            this.payload = payload;
        }

        @Override
        public void readFrom(ChannelBuf buf) {
            payload = ChannelBufBinaryPayload.of(buf);
        }

        @Override
        public void writeTo(ChannelBuf buf) {
            buf.writeBytes(payload.bytes());
        }

    }
}
