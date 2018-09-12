package com.minelittlepony.jumpingcastle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minelittlepony.jumpingcastle.api.IChannel;
import com.minelittlepony.jumpingcastle.api.IMessage;
import com.minelittlepony.jumpingcastle.dsm.MsgHello;
import com.minelittlepony.jumpingcastle.payload.DeserializedPayload;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

public final class JumpingServer {

    private static final JumpingServer INSTANCE = new JumpingServer();

    private static final Logger LOGGER = LogManager.getLogger("JUMPING_SERVER");

    public static JumpingServer instance() {
        return INSTANCE;
    }

    private final Map<UUID, Entry> playerChannelsMapping = new HashMap<>();

    private JumpingServer() {
    }

    public void onHello(MsgHello message, IChannel channel) {
        playerChannelsMapping.put(message.playerId, new Entry(message));
    }

    public void onPayload(UUID senderId, DeserializedPayload payload) {
        if (payload.target.isServers()) {
            JumpingCastleImpl.instance().onPayload(payload);
        }

        if (payload.target.isClients()) {
            IBinaryPayload forwarded = payload.payload.reverse();
            IMessageBus bus = JumpingCastleImpl.instance().getBus();

            playerChannelsMapping.values().stream()
                .filter(entry -> entry.subscriptions.contains(payload.channel))
                .forEach(entry -> bus.sendToClient(entry.playerId, forwarded));
        }
    }

    void broadcast(String channel, long id, IMessage message) {
        IMessageBus bus = JumpingCastleImpl.instance().getBus();

        playerChannelsMapping.values().stream()
            .filter(entry -> entry.subscriptions.contains(channel))
            .forEach(entry -> bus.sendToClient(channel, id, message, entry.playerId));
    }

    public void onPlayerLeave(UUID playerId) {
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
