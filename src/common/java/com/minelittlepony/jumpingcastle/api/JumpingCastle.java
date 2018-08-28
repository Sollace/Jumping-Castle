package com.minelittlepony.jumpingcastle.api;

import com.minelittlepony.jumpingcastle.JumpingCastleImpl;

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
    static IChannel listen(String channelName) {
        return JumpingCastleImpl.instance().listen(channelName);
    }
}
