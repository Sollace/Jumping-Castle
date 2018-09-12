package com.minelittlepony.jumpingcastle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minelittlepony.jumpingcastle.api.IChannel;
import com.minelittlepony.jumpingcastle.api.IClient;
import com.minelittlepony.jumpingcastle.api.JumpingCastle;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.dsm.MsgAck;
import com.minelittlepony.jumpingcastle.dsm.MsgHello;
import com.minelittlepony.jumpingcastle.payload.DeserializedPayload;

public final class JumpingCastleImpl implements JumpingCastle {

    private static final Logger LOGGER = LogManager.getLogger("JUMPING_CLIENT");

    private static final JumpingCastleImpl INSTANCE = new JumpingCastleImpl();

    public static final String CHANNEL = "JUMPIN";
    public static final byte PROTOCOL = 0;

    public static JumpingCastleImpl instance() {
        return INSTANCE;
    }

    private final Map<String, Channel> channels = new HashMap<>();

    private final List<IClient> clients = new ArrayList<>();

    private IMessageBus bus;

    private IChannel helloChannel;

    private JumpingCastleImpl() {
        helloChannel = listen(CHANNEL, null)
                .consume(MsgHello.class, JumpingServer.instance()::onHello)
                .consume(MsgAck.class, this::connectionEstalished);
    }

    private void connectionEstalished(MsgAck msg, IChannel channel) {
        LOGGER.info("Recieved Ack from server with player");
        clients.forEach(IClient::connectionEstablished);
    }

    public void sayHello(UUID playerId) {
        LOGGER.info("Sending hello to server with player id " + playerId);
        helloChannel.send(new MsgHello(playerId, channels.keySet()), Target.SERVER);
    }

    public boolean setBus(IMessageBus bus) {
        if (this.bus == null) {
            this.bus = bus;
            return true;
        }

        return false;
    }

    public IMessageBus getBus() {
        if (bus == null) {
            throw new IllegalStateException("No message bus");
        }
        return bus;
    }

    public IChannel listen(String channelName, IClient clientHandler) {
        if (clientHandler != null && !clients.contains(clientHandler)) {
            clients.add(clientHandler);
        }
        return channels.computeIfAbsent(channelName, Channel::new);
    }

    public void onPayload(DeserializedPayload payload) {
        if (payload.protocol != PROTOCOL) {
            LOGGER.warn("Protocol version mismatch. Current version is %d. Payload arrived with version %d", PROTOCOL, payload.protocol);
        }
        if (channels.containsKey(payload.channel)) {
            channels.get(payload.channel).onPayload(payload);
        } else {
            LOGGER.warn("Packet for unknown channel \"%s\" was ignored", payload.channel);
        }
    }

}
