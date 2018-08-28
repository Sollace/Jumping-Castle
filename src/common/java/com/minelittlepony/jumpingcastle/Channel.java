package com.minelittlepony.jumpingcastle;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.minelittlepony.jumpingcastle.api.IChannel;
import com.minelittlepony.jumpingcastle.api.IMessage;
import com.minelittlepony.jumpingcastle.api.IMessageHandler;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

class Channel implements IChannel {

    private final String name;

    private final Map<Long, Entry<? extends IMessage>> handlers = new HashMap<>();

    public Channel(String channelName) {
        name = channelName;
    }

    @Override
    public <T extends IMessage> IChannel consume(Class<T> messageType, IMessageHandler<T> handler) {
        handlers.put(computeUniqueId(messageType), new Entry<T>(messageType, handler));
        return this;
    }

    @Override
    public IChannel send(IMessage message, Target target) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onPayload(IBinaryPayload payload) {
        @SuppressWarnings("unchecked")
        Entry<IMessage> entry = (Entry<IMessage>)handlers.get(payload.readLong());
        if (entry != null) {
            entry.handler.handleMessage(payload.readBinary(entry.handleType), this);
        }
    }

    private class Entry<T extends IMessage> {
        final IMessageHandler<T> handler;

        final Class<T> handleType;

        private Entry(Class<T> handleType, IMessageHandler<T> handler) {
            this.handleType = handleType;
            this.handler = handler;
        }
    }

    private static long computeUniqueId(Class<?> type) {
        try {
            Field f = type.getField("serialVersionUID");
            f.setAccessible(true);
            return (long)f.get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            return -1;
        }
    }
}
