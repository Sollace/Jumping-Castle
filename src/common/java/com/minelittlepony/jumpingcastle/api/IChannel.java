package com.minelittlepony.jumpingcastle.api;

import java.util.UUID;

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
     * Registers a handler for a specific message type transmitted over this channel.
     *
     * @param messageType   The message type being recieved.
     */
    <T extends IMessage & IMessageHandler<T>> IChannel consume(Class<T> messageType);

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

    /**
     * Sends a message back. Use this if you're a server.
     *
     * @param message The message to send.
     * @param recipient  Recipient that must handle this message
     */
    IChannel respond(IMessage message, UUID recipient);

    /**
     * Sends a message back to all clients. Use this if you're a server.
     *
     * @param message The message to send.
     */
    IChannel broadcast(IMessage message);
}
