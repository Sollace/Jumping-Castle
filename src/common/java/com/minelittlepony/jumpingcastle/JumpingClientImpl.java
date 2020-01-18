package com.minelittlepony.jumpingcastle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minelittlepony.jumpingcastle.api.Channel;
import com.minelittlepony.jumpingcastle.api.Bus;
import com.minelittlepony.jumpingcastle.api.JumpingCastle;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.dsm.MsgAck;
import com.minelittlepony.jumpingcastle.dsm.MsgHello;

public final class JumpingClientImpl implements JumpingCastle {

    private static final Logger LOGGER = LogManager.getLogger("JUMPING_CLIENT");

    private static final JumpingClientImpl INSTANCE = new JumpingClientImpl();

    public static final String CHANNEL = "JUMPIN";
    public static final byte PROTOCOL = 1;

    public static JumpingClientImpl instance() {
        return INSTANCE;
    }

    private final Map<String, ChannelImpl> channels = new HashMap<>();

    private final List<Client> clients = new ArrayList<>();

    private Bus bus;

    private Channel helloChannel;

    private JumpingClientImpl() {
        helloChannel = subscribeTo(CHANNEL, null)
                .listenFor(MsgHello.class, JumpingServerImpl.INSTANCE::startTrackingPlayer)
                .listenFor(MsgAck.class, this::connectionEstalished);
    }

    private void connectionEstalished(MsgAck msg, Channel channel) {
        LOGGER.info("Recieved Ack from server with player");
        clients.forEach(Client::connectionEstablished);
    }

    public void sayHello(UUID playerId) {
        LOGGER.info("Sending hello to server with player id " + playerId);
        helloChannel.send(new MsgHello(playerId, channels.keySet()), Target.SERVER);
    }

    public boolean setBus(Bus bus) {
        if (this.bus == null) {
            this.bus = bus;
            return true;
        }

        return false;
    }

    public Bus getBus() {
        if (bus == null) {
            throw new IllegalStateException("No message bus");
        }
        return bus;
    }

    public Channel subscribeTo(String channelName, Client clientHandler) {
        if (clientHandler != null && !clients.contains(clientHandler)) {
            clients.add(clientHandler);
        }
        return channels.computeIfAbsent(channelName, ChannelImpl::new);
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
