package com.minelittlepony.jumpingcastle;

import java.util.HashMap;
import java.util.Map;

import com.minelittlepony.jumpingcastle.api.IChannel;
import com.minelittlepony.jumpingcastle.api.JumpingCastle;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

public final class JumpingCastleImpl implements JumpingCastle {

    private static final JumpingCastleImpl INSTANCE = new JumpingCastleImpl();

    public static JumpingCastleImpl instance() {
        return INSTANCE;
    }

    private final Map<String, Channel> channels = new HashMap<>();

    private JumpingCastleImpl() {

    }

    public IChannel listen(String channelName) {
        return channels.computeIfAbsent(channelName, Channel::new);
    }

    public void onPayload(IBinaryPayload payload) {
        String channel = payload.readString();

        if (channels.containsKey(channel)) {
            channels.get(channel).onPayload(payload);
        }
    }
}
