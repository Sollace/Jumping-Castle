package com.minelittlepony.jumpingcastle.api;

@FunctionalInterface
public interface IMessageHandler<T extends IMessage> {
    void onPayload(T message, IChannel channel);
}
