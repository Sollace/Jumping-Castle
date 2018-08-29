package com.minelittlepony.jumpingcastle.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A message for communicating over a channel.
 * Fields marked with @Expose are automatically serialized to the output packet stream
 * and will be made availalbe on the far end of the pipe.
 *
 */
public interface IMessage {
    /**
     * Identifier used to distenguish different messages within the same channel.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Id {
        long value();
    }

    static long identifier(IMessage msg) {
        return identifier(msg.getClass());
    }

    static long identifier(Class<? extends IMessage> cls) {
        return cls.getAnnotation(Id.class).value();
    }
}
