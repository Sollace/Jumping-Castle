package com.minelittlepony.jumpingcastle;

import java.util.HashMap;
import java.util.Map;

import com.minelittlepony.jumpingcastle.api.IChannel;
import com.minelittlepony.jumpingcastle.api.IMessage;
import com.minelittlepony.jumpingcastle.api.IMessageHandler;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.payload.DeserializedPayload;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

class Channel implements IChannel {

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
        JumpingCastleImpl.instance().getBus().sendToServer(name, message.identifier(), message, target);
        return null;
    }

    public void onPayload(DeserializedPayload payload) {
        Entry<? extends IMessage> entry = handlers.get(payload.objectType);

        if (entry != null) {
            entry.onPayload(payload.payload);
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
            handler.onPayload(payload.readBinary(handleType), Channel.this);
        }
    }
}
