package com.minelitlepony.jumpingcastle.liteloader;

import java.io.File;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.minelittlepony.jumpingcastle.IMessageBus;
import com.minelittlepony.jumpingcastle.JumpingCastleImpl;
import com.minelittlepony.jumpingcastle.JumpingServer;
import com.minelittlepony.jumpingcastle.api.IMessage;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.payload.DeserializedPayload;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;
import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mumfrey.liteloader.JoinGameListener;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.PluginChannelListener;
import com.mumfrey.liteloader.ServerPlayerListener;
import com.mumfrey.liteloader.ServerPluginChannelListener;
import com.mumfrey.liteloader.core.ClientPluginChannels;
import com.mumfrey.liteloader.core.ServerPluginChannels;
import com.mumfrey.liteloader.core.PluginChannels.ChannelPolicy;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketJoinGame;

public class LiteModJumpingCastle implements LiteMod, JoinGameListener, PluginChannelListener, ServerPluginChannelListener, ServerPlayerListener, IMessageBus {
    private static final List<String> CHANNEL_IDENTIFIERS = Lists.newArrayList(JumpingCastleImpl.CHANNEL);

    private boolean running;

	@Override
	public String getName() {
		return "@NAME@";
	}

	@Override
	public String getVersion() {
		return "@VERSION@";
	}

	@Override
	public void init(File configPath) {
	    running = JumpingCastleImpl.instance().setBus(this);
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {

	}

    @Override
    public void onJoinGame(INetHandler netHandler, SPacketJoinGame joinGamePacket, ServerData serverData, RealmsServer realmsServer) {
        if (running) {
            JumpingCastleImpl.instance().sayHello(Minecraft.getMinecraft().player.getGameProfile().getId());
        }
    }

    @Override
    public List<String> getChannels() {
        return CHANNEL_IDENTIFIERS;
    }

    @Override
    public void onCustomPayload(String channel, PacketBuffer data) {
        if (running && JumpingCastleImpl.CHANNEL.equalsIgnoreCase(channel)) {
            JumpingCastleImpl.instance().onPayload(new DeserializedPayload(IBinaryPayload.of(data)));
        }
    }

    @Override
    public void onCustomPayload(EntityPlayerMP sender, String channel, PacketBuffer data) {
        if (running && JumpingCastleImpl.CHANNEL.equalsIgnoreCase(channel)) {
            JumpingServer.instance().onPayload(sender.getGameProfile().getId(), new DeserializedPayload(IBinaryPayload.of(data)));
        }
    }

    @Override
    public void sendToServer(String channel, long id, IMessage message, Target target) {
        ClientPluginChannels.sendMessage(JumpingCastleImpl.CHANNEL, IBinaryPayload.of(new PacketBuffer(Unpooled.buffer()))
                .writeByte(JumpingCastleImpl.PROTOCOL)
                .writeString(channel)
                .writeLong(id)
                .writeByte((byte)target.ordinal())
                .writeBinary(message).buff(), ChannelPolicy.DISPATCH);
    }

    @Override
    public void sendToClient(UUID playerId, IBinaryPayload forwarded) {
        EntityPlayerMP player = Minecraft.getMinecraft().getIntegratedServer().getPlayerList().getPlayerByUUID(playerId);

        if (player != null) {
            ServerPluginChannels.sendMessage(player, JumpingCastleImpl.CHANNEL, forwarded.buff(), ChannelPolicy.DISPATCH);
        }
    }

    @Override
    public void onPlayerConnect(EntityPlayerMP player, GameProfile profile) {
    }

    @Override
    public void onPlayerLoggedIn(EntityPlayerMP player) {
    }

    @Override
    public void onPlayerRespawn(EntityPlayerMP player, EntityPlayerMP oldPlayer, int newDimension, boolean playerWonTheGame) {

    }

    @Override
    public void onPlayerLogout(EntityPlayerMP player) {
        if (running) {
            JumpingServer.instance().onPlayerLeave(player.getGameProfile().getId());
        }
    }
}
