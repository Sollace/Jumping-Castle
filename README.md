# Jumping Castle

## Light and bouncy!

Jumping castle is a packet transport layer for Minecraft clients and servers build on top of various other existing frameworks. It provides a consistent interface for mods to interact with wether using Forge, LiteLoader, Bukkit or Spigot, or even Sponge.

As long as the server is running any of the above with JumpingCastle, your mod will be able to transmit messages to itself either on the server or other clients.

SolNet (Otherwise known as Jumping Castle because, well, it jumps between the client and server) is a network abstraction layer  for Minecraft clients and servers built overtop of any existing frameworks pre-installed on the server. It provides a minimal and consistent interface across all supported platforms that makes it easier for modders to reuse the same networking code across both clients and servers of different implementations, or to communicate with their own mod on other clients connected to the same server without necessarily having to install your mod on the server.

## Messages


Message serialization is handled internally, meaning all that's required is to define the structure of your data.
Each message type has its own class implementing the IMessage interface with an attached annotation to specify the numeric
id of that message when sending and recieving.

```
// unique id to distenguish this message from others
@IMessage.Id(0)
public class MsgHello implements IMessage {
	@Expose
	List<String> channels;
	
	@Expose
	UUID playerId;
}
```

* Note: This is different from other platforms where the id is typically sent in when registering your packet type.

## Registration / Handling Messages

Jumping Castle makes a vailable a single class `JumpingCastle` with a single method `listen`.

To create a channel, you 'listen' on your channel name. The method will then return a channel instance associated with that name.

From there you can add your different messages:

```
        IChannel myChannel = JumpingCastle.subscribeTo("MyChannelName", () -> {
	        // connection established, yay!
	    })
            .listenFor(MsgHello.class, (msg, channel) -> {
		// ...
		// Handle the incomming message
		// ...
		// Send a response to other clients, if you want
		// channel.send(new MyResponse(), Target.CLIENTS);
            })
            .listenFor(MsgGoodbye.class, (msg, channel) -> {
		// ...
		// Handle the incomming message
		// ...
		// Send a response to other clients, if you want
		// channel.send(new MyResponse(), Target.CLIENTS);
             });
```

Of course, that's not the only way way to use it. If you'd rather have messages handle themselves, you can implement the `IMessageHandler<T>` interface directly and simply pass in the message's type when listening for it:

```
       // unique id to distenguish this message from others
      @IMessage.Id(0)
      public class MsgHello implements IMessage, IMessageHandler<MsgHello> {
	    @Expose
	    List<String> channels;
	
	    @Expose
	    UUID playerId;
	    
	    @Override
            public void onPayload(MsgHello message, IChannel channel) {
		// ...
		// Handle the incoming message
		// ...
		// Send a response to other clients, if you want
		// channel.send(new MsgGoodbye(), Target.CLIENTS);
	    }
        }
	
        IChannel myChannel = JumpingCastle.subscribeTo("MyChannelName", () -> {
	        // connection established, yay!
	    })
            .listenFor(MsgHello.class)
            .listenFor(MsgGoodbye.class, (msg, channel) -> {
		// ...
		// Handle the incoming message
		// ...
		// Send a response to other clients, if you want
		// channel.send(new MsgGoodbye(), Target.CLIENTS);
             });
```

## Sending Messages

You can send a message from anywhere that you have a reference to your IChannel object.

### Clients

```
IChannel.send(IMessage message, Target target)
```

Clients will want to use the `send` methods. Simply call the method with an instance of your message and specify the recipients you want it to be recieved on.

CLIENTS - (PlayerSync) Every other client connected to the server gets your message.
SERVER - Only the server gets your message
SERVER_AND_CLIENTS - Stop shouting! The server and every other client will get your message.

### Servers

Servers are clients too!

Not only can you use the send methods above, but there are some dedicated server methods to make communicating _back_ easier!

```
IChannel.respond(IMessage message, UUID recipient)
```
Recipient here is the profile uuid for the target player.

```
IChannel.broadcast(IMessage message)
```
Similar to `send(msg, Target.CLIENTS)` on the client, but for the server.
This sends your message to every connected client.
