package com.minelittlepony.jumpingcastle.api;

@FunctionalInterface
public interface IMessageHandler<T extends IMessage> {
    void handleMessage(T message, IChannel channel);
}
