package com.minelittlepony.jumpingcastle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minelittlepony.jumpingcastle.api.Bus;
import com.minelittlepony.jumpingcastle.api.Channel;
import com.minelittlepony.jumpingcastle.api.Message;
import com.minelittlepony.jumpingcastle.api.Server;
import com.minelittlepony.jumpingcastle.api.payload.BinaryPayload;
import com.minelittlepony.jumpingcastle.dsm.MsgAck;
import com.minelittlepony.jumpingcastle.dsm.MsgHello;

public final class JumpingServerImpl implements Server {

    public static final JumpingServerImpl INSTANCE = new JumpingServerImpl();

    private static final Logger LOGGER = LogManager.getLogger("JUMPING_SERVER");

    private final Map<UUID, Entry> playerChannelsMapping = new HashMap<>();

    private JumpingServerImpl() { }

    void startTrackingPlayer(MsgHello message, Channel channel) {
        playerChannelsMapping.put(message.playerId, new Entry(message));

        channel.respond(new MsgAck(), message.playerId);
    }

    @Override
    public void onPayload(UUID senderId, DeserializedPayload payload) {
        if (payload.target.isServers()) {
            JumpingClientImpl.instance().onPayload(payload);
        }

        if (payload.target.isClients()) {
            BinaryPayload forwarded = payload.payload.reverse();
            Bus bus = JumpingClientImpl.instance().getBus();

            playerChannelsMapping.values().stream()
                .filter(entry -> entry.subscriptions.contains(payload.channel))
                .forEach(entry -> bus.sendToClient(entry.playerId, forwarded));
        }
    }

    void broadcast(String channel, long id, Message message) {
        Bus bus = JumpingClientImpl.instance().getBus();

        playerChannelsMapping.values().stream()
            .filter(entry -> entry.subscriptions.contains(channel))
            .forEach(entry -> bus.sendToClient(channel, id, message, entry.playerId));
    }

    @Override
    public void stopTrackingPlayer(UUID playerId) {
        playerChannelsMapping.remove(playerId);
    }

    private final class Entry {

        private final UUID playerId;

        private final List<String> subscriptions;

        Entry(MsgHello message) {
            playerId = message.playerId;
            subscriptions = message.channels;

            LOGGER.info("Player " + playerId + " subscribed for channels " + Arrays.toString(subscriptions.toArray()));
        }
    }
}
