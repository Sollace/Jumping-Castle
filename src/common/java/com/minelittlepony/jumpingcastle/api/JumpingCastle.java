package com.minelittlepony.jumpingcastle.api;

import com.minelittlepony.jumpingcastle.JumpingClientImpl;

/**
 * Jumping Castle main interface.
 * <p>
 *{@code
 * JumpingCastle.listen("My Channel").consume(MyMessage.class, (msg, channel) -> {
 *   ...
 * });
 *
 */
public interface JumpingCastle {
    /**
     * Gets or creates a new channel indexed by the given identifier.
     *
     * @param channelName   The channel name
     *
     * @return An instance of IChannel.
     */
    static Channel subscribeTo(String channelName, Client clientHandler) {
        return JumpingClientImpl.instance().subscribeTo(channelName, clientHandler);
    }

    @FunctionalInterface
    public interface Client {
        void connectionEstablished();
    }
}
