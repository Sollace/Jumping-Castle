package com.minelittlepony.jumpingcastle.api;

/**
 * A channel for sending and recieving messages.
 */
public interface IChannel {
    /**
     * Registers a handler for a specific message type transmitted over this channel.
     *
     * @param messageType   The message type being recieved.
     * @param handler       A handler instance to handle the message.
     */
    <T extends IMessage> IChannel consume(Class<T> messageType, IMessageHandler<T> handler);

    /**
     * Sends a message over this channel. By default targets all other clients listening on this channel.
     *
     * @param message The message to send.
     */
    default IChannel send(IMessage message) {
        return send(message, Target.CLIENTS);
    }

    /**
     * Sends a message over this channel.
     *
     * @param message The message to send.
     * @param target  Recipients that must handle this message (clients, server, or both)
     */
    IChannel send(IMessage message, Target target);
}
