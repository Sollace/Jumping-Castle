package com.minelittlepony.jumpingcastle.api;

import com.minelittlepony.jumpingcastle.Target;

public interface IChannel {
    <T extends IMessage> IChannel consume(Class<T> messageType, IMessageHandler<T> handler);

    default IChannel send(IMessage message) {
        return send(message, Target.CLIENTS);
    }

    IChannel send(IMessage message, Target target);
}
