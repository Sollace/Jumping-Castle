package com.minelittlepony.jumpingcastle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.minelittlepony.jumpingcastle.api.IChannel;
import com.minelittlepony.jumpingcastle.dsm.MsgHello;
import com.minelittlepony.jumpingcastle.payload.DeserializedPayload;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

public final class JumpingServer {

    private static final JumpingServer INSTANCE = new JumpingServer();

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
        if (payload.target.servers) {
            JumpingCastleImpl.instance().onPayload(payload);
        }
        if (payload.target.clients) {
            IBinaryPayload forwarded = payload.payload.reverse();
            IMessageBus bus = JumpingCastleImpl.instance().getBus();

            playerChannelsMapping.values().stream()
                .filter(entry -> entry.subscriptions.contains(payload.channel))
                .forEach(entry -> bus.sendToClient(entry.playerId, forwarded));
        }
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
        }
    }
}
