package com.minelittlepony.jumpingcastle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.minelittlepony.jumpingcastle.api.IChannel;
import com.minelittlepony.jumpingcastle.api.JumpingCastle;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.dsm.MsgHello;
import com.minelittlepony.jumpingcastle.payload.DeserializedPayload;

public final class JumpingCastleImpl implements JumpingCastle {

    private static final JumpingCastleImpl INSTANCE = new JumpingCastleImpl();

    public static final String CHANNEL = "JUMPIN";

    public static JumpingCastleImpl instance() {
        return INSTANCE;
    }

    private final Map<String, Channel> channels = new HashMap<>();

    private IMessageBus bus;

    private IChannel helloChannel;

    private JumpingCastleImpl() {
        helloChannel = JumpingCastle.listen(CHANNEL).consume(MsgHello.class, JumpingServer.instance()::onHello);
    }

    public void sayHello(UUID playerId) {
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

    public IChannel listen(String channelName) {
        return channels.computeIfAbsent(channelName, Channel::new);
    }

    public void onPayload(DeserializedPayload payload) {
        if (channels.containsKey(payload.channel)) {
            channels.get(payload.channel).onPayload(payload);
        }
    }

}
