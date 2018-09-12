package com.minelittlepony.jumpingcastle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minelittlepony.jumpingcastle.api.IChannel;
import com.minelittlepony.jumpingcastle.api.IMessage;
import com.minelittlepony.jumpingcastle.api.IMessageHandler;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.payload.DeserializedPayload;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

class Channel implements IChannel {

    private static final Logger LOGGER = LogManager.getLogger("JUMPING_CHANNEL");

    private final String name;

    private final Map<Long, Entry<? extends IMessage>> handlers = new HashMap<>();

    public Channel(String channelName) {
        name = channelName;
    }

    @Override
    public <T extends IMessage> IChannel consume(Class<T> messageType, IMessageHandler<T> handler) {
        handlers.put(IMessage.identifier(messageType), new Entry<T>(messageType, handler));
        return this;
    }

    @Override
    public IChannel send(IMessage message, Target target) {
        JumpingCastleImpl.instance().getBus().sendToServer(name, IMessage.identifier(message), message, target);
        return this;
    }

    @Override
    public IChannel respond(IMessage message, UUID recipient) {
        JumpingCastleImpl.instance().getBus().sendToClient(name, IMessage.identifier(message), message, recipient);
        return this;
    }

    @Override
    public IChannel broadcast(IMessage message) {
        JumpingServer.instance().broadcast(name, IMessage.identifier(message), message);
        return this;
    }

    public void onPayload(DeserializedPayload payload) {
        Entry<? extends IMessage> entry = handlers.get(payload.objectType);

        if (entry != null) {
            entry.onPayload(payload.payload);
        } else {
            LOGGER.warn("Packet on channel \"%s\" with unknown handler ignored.", name);
        }
    }

    private class Entry<T extends IMessage> {
        final IMessageHandler<T> handler;

        final Class<T> handleType;

        private Entry(Class<T> handleType, IMessageHandler<T> handler) {
            this.handleType = handleType;
            this.handler = handler;
        }

        private void onPayload(IBinaryPayload payload) {
            Exceptions.logged(() -> {
                handler.onPayload(payload.readBinary(handleType), Channel.this);
            }, LOGGER);
        }
    }
}
