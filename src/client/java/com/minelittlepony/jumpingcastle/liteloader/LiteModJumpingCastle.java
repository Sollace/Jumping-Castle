package com.minelittlepony.jumpingcastle.liteloader;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.minelittlepony.jumpingcastle.DeserializedPayload;
import com.minelittlepony.jumpingcastle.JumpingClientImpl;
import com.minelittlepony.jumpingcastle.api.Bus;
import com.minelittlepony.jumpingcastle.api.Message;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.api.payload.BinaryPayload;
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
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketJoinGame;

public class LiteModJumpingCastle implements LiteMod, JoinGameListener, PluginChannelListener, ServerPluginChannelListener, ServerPlayerListener, Bus {
    private static final List<String> CHANNEL_IDENTIFIERS = Lists.newArrayList(JumpingClientImpl.CHANNEL);

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
	    running = JumpingClientImpl.instance().setBus(this);
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {

	}

    @Override
    public void onJoinGame(INetHandler netHandler, SPacketJoinGame joinGamePacket, ServerData serverData, RealmsServer realmsServer) {
        if (running && netHandler instanceof NetHandlerPlayClient) {
            JumpingClientImpl.instance().sayHello(((NetHandlerPlayClient)netHandler).getGameProfile().getId());
        }
    }

    @Override
    public List<String> getChannels() {
        return CHANNEL_IDENTIFIERS;
    }

    @Override
    public void onCustomPayload(String channel, PacketBuffer data) {
        if (running && JumpingClientImpl.CHANNEL.equalsIgnoreCase(channel)) {
            JumpingClientImpl.instance().onPayload(new DeserializedPayload(BinaryPayload.of(data)));
        }
    }

    @Override
    public void onCustomPayload(EntityPlayerMP sender, String channel, PacketBuffer data) {
        if (running && JumpingClientImpl.CHANNEL.equalsIgnoreCase(channel)) {
            getServer().onPayload(sender.getGameProfile().getId(), new DeserializedPayload(BinaryPayload.of(data)));
        }
    }

    @Override
    public void sendToServer(String channel, long id, Message message, Target target) {
        ClientPluginChannels.sendMessage(JumpingClientImpl.CHANNEL, BinaryPayload.of(new PacketBuffer(Unpooled.buffer()))
                .writeByte(JumpingClientImpl.PROTOCOL)
                .writeString(channel)
                .writeLong(id)
                .writeByte((byte)target.ordinal())
                .writeBinary(message).buff(), ChannelPolicy.DISPATCH_ALWAYS);
    }

    @Override
    public void sendToClient(UUID playerId, BinaryPayload forwarded) {
        EntityPlayerMP player = Minecraft.getMinecraft().getIntegratedServer().getPlayerList().getPlayerByUUID(playerId);

        if (player != null) {
            ServerPluginChannels.sendMessage(player, JumpingClientImpl.CHANNEL, forwarded.buff(), ChannelPolicy.DISPATCH_ALWAYS);
        }
    }

    @Override
    public void sendToClient(String channel, long id, Message message, UUID playerId) {
        EntityPlayerMP player = Minecraft.getMinecraft().getIntegratedServer().getPlayerList().getPlayerByUUID(playerId);

        if (player != null) {
            ServerPluginChannels.sendMessage(player, JumpingClientImpl.CHANNEL, BinaryPayload.of(new PacketBuffer(Unpooled.buffer()))
                    .writeByte(JumpingClientImpl.PROTOCOL)
                    .writeString(channel)
                    .writeLong(id)
                    .writeByte((byte)Target.CLIENTS.ordinal())
                    .writeBinary(message).buff(), ChannelPolicy.DISPATCH_ALWAYS);
        }
    }

    @Nullable
    @Override
    public Object getMinecraftServer() {
        return Minecraft.getMinecraft().getIntegratedServer();
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
            getServer().stopTrackingPlayer(player.getGameProfile().getId());
        }
    }
}
