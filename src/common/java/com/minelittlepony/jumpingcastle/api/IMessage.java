package com.minelittlepony.jumpingcastle.api;

import java.io.Serializable;

/**
 * A message for communicating over a channel.
 * Fields marked with @Expose are automatically serialized to the output packet stream
 * and will be made availalbe on the far end of the pipe.
 *
 * It is <i>highly</i> recommended that you include a generated serial version id with any classes that implement this interface:
 *
 * private static final long serialVersionUID = -1L;
 *
 * This id will be used when serializing to and from your custom type.
 */
public interface IMessage extends Serializable {

}
