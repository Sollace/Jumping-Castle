package com.minelittlepony.jumpingcastle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minelittlepony.jumpingcastle.api.Channel;
import com.minelittlepony.jumpingcastle.api.Message;
import com.minelittlepony.jumpingcastle.api.Handler;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.api.payload.BinaryPayload;

class ChannelImpl implements Channel {

    private static final Logger LOGGER = LogManager.getLogger("JUMPING_CHANNEL");

    private final String name;

    private final Map<Long, Entry<? extends Message>> handlers = new HashMap<>();

    public ChannelImpl(String channelName) {
        name = channelName;
    }

    @Override
    public <T extends Message> Channel listenFor(Class<T> messageType, Handler<T> handler) {
        handlers.put(Message.identifier(messageType), new Entry<T>(messageType, handler));
        return this;
    }

    @Override
    public <T extends Message & Handler<T>> Channel listenFor(Class<T> messageType) {
        return listenFor(messageType, null);
    }


    @Override
    public Channel send(Message message, Target target) {
        JumpingClientImpl.instance().getBus().sendToServer(name, Message.identifier(message), message, target);
        return this;
    }

    @Override
    public Channel respond(Message message, UUID recipient) {
        JumpingClientImpl.instance().getBus().sendToClient(name, Message.identifier(message), message, recipient);
        return this;
    }

    @Override
    public Channel broadcast(Message message) {
        JumpingServerImpl.INSTANCE.broadcast(name, Message.identifier(message), message);
        return this;
    }

    public void onPayload(DeserializedPayload payload) {
        Entry<? extends Message> entry = handlers.get(payload.objectType);

        if (entry != null) {
            entry.onPayload(payload.payload);
        } else {
            LOGGER.warn("Packet on channel \"%s\" with unknown handler ignored.", name);
        }
    }

    private class Entry<T extends Message> {
        private final Handler<T> handler;

        private final Class<T> handleType;

        private Entry(Class<T> handleType, Handler<T> handler) {
            this.handleType = handleType;
            this.handler = handler;
        }

        @SuppressWarnings("unchecked")
        private void onPayload(BinaryPayload payload) {
            Exceptions.logged(() -> {
                T message = payload.readBinary(handleType);

                if (message instanceof Handler) {
                    ((Handler<T>) message).onPayload(message, ChannelImpl.this);
                } else {
                    handler.onPayload(message, ChannelImpl.this);
                }
            }, LOGGER);
        }
    }
}
