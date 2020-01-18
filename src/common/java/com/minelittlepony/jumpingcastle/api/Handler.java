package com.minelittlepony.jumpingcastle.api;

/**
 * Handler interface for incoming messages.
 *
 * @param <T> The type of message to handle.
 */
@FunctionalInterface
public interface Handler<T extends Message> {
    /**
     * Called when a new message is received.
     *
     * @param message The message received.
     *
     * @param channel The channel used to deliver the message.
     */
    void onPayload(T message, Channel channel);
}
