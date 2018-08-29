# Jumping Castle

## Light and bouncy!

Jumping castle is a packet transport layer for Minecraft clients and servers build on top of various other existing frameworks. It provides a consistent interface for mods to interact with wether using Forge, LiteLoader, Bukkit or Spigot, or even Sponge.

As long as the server is running any of the above with JumpingCastle, your mod will be able to transmit messages to itself either on the server or other clients.

## Usage

There's two things you need to get started in using JumpingCastle.

First, mods will want to create a class for each of the types of messages they want to handle. JumpingCastle itself implements one of its own:

```
public class MsgHello implements IMessage {
	
	private static final long serialVersionId = 1L;
	
	@Expose
	List<String> channels;
	
	@Expose
	UUID playerId;
}
```

That's all you need! The serialVersionId can be any number you want, but it has to be unique, and any fields marked with @Expose will be transmitted and made available on the recieving end.

Now how do you _handle_ these messages?

In your mod's initialize method you just have to register your message with JumpingCastle:

```
JumpingCastle.listen("MyChannelName").consume(MsgHello.class, (msg, channel) -> {
		// ...
		// Handle the incomming message
		// ...
		// Send a response to other clients, if you want
		// channel.send(new MyResponse(), Target.CLIENTS);
});
```

This is a blank template because setting up gradle is a b*****.
