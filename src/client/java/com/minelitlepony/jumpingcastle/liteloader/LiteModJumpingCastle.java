package com.minelitlepony.jumpingcastle.liteloader;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;
import com.minelittlepony.jumpingcastle.JumpingCastleImpl;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mumfrey.liteloader.JoinGameListener;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.PluginChannelListener;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketJoinGame;

public class LiteModJumpingCastle implements LiteMod, JoinGameListener, PluginChannelListener {

    private static final String CHANNEL = "JUMPIN";
    private static final List<String> CHANNEL_IDENTIFIERS = Lists.newArrayList(CHANNEL);
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

	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {

	}

    @Override
    public void onJoinGame(INetHandler netHandler, SPacketJoinGame joinGamePacket, ServerData serverData, RealmsServer realmsServer) {

    }

    @Override
    public List<String> getChannels() {
        return CHANNEL_IDENTIFIERS;
    }

    @Override
    public void onCustomPayload(String channel, PacketBuffer data) {
        if (CHANNEL.equalsIgnoreCase(channel)) {
            JumpingCastleImpl.instance().onPayload(IBinaryPayload.of(data));
        }
    }
}
