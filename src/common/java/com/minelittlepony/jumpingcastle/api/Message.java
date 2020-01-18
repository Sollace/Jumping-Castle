package com.minelittlepony.jumpingcastle.api;

/**
 * A message for communicating over a channel.
 * Fields marked with @Expose are automatically serialized to the output packet stream
 * and will be made availalbe on the far end of the pipe.
 *
 */
public interface Message {
    static long identifier(Message msg) {
        return identifier(msg.getClass());
    }

    static long identifier(Class<? extends Message> cls) {
        return cls.getCanonicalName().hashCode();
    }
}
