package com.minelittlepony.jumpingcastle.forge;

import java.io.IOException;
import java.util.UUID;

import com.minelittlepony.jumpingcastle.DeserializedPayload;
import com.minelittlepony.jumpingcastle.JumpingClientImpl;
import com.minelittlepony.jumpingcastle.api.Bus;
import com.minelittlepony.jumpingcastle.api.Message;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.api.payload.BinaryPayload;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

@Mod(
    modid = "@MODID@_forge",
    name = "@NAME@",
    version = "1.12.2",
    acceptedMinecraftVersions = "[1.12.2]"
)
@EventBusSubscriber
public class ForgeModJumpingCastle implements Bus {

    private static ForgeModJumpingCastle instance;

    private FMLEventChannel bus;

    private boolean running;

    public ForgeModJumpingCastle() {
        instance = this;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        running = JumpingClientImpl.instance().setBus(this);

        if (running) {
            bus = NetworkRegistry.INSTANCE.newEventDrivenChannel(JumpingClientImpl.CHANNEL);

            bus.register(this);
        }
    }

    @SubscribeEvent
    public void onServerPacket(ServerCustomPacketEvent event) throws IOException {
        if (instance.running && JumpingClientImpl.CHANNEL.equalsIgnoreCase(event.getPacket().channel())) {
            NetHandlerPlayServer net = (NetHandlerPlayServer)event.getHandler();

            getServer().onPayload(net.player.getGameProfile().getId(), new DeserializedPayload(BinaryPayload.of(event.getPacket().payload())));
        }
    }

    @SubscribeEvent
    public void onClientPacket(ClientCustomPacketEvent event) throws IOException {
        if (instance.running && JumpingClientImpl.CHANNEL.equalsIgnoreCase(event.getPacket().channel())) {
            JumpingClientImpl.instance().onPayload(new DeserializedPayload(BinaryPayload.of(event.getPacket().payload())));
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinWorldEvent event) {
        if (instance.running && event.getEntity() instanceof EntityPlayer) {
            JumpingClientImpl.instance().sayHello(((EntityPlayer)event.getEntity()).getGameProfile().getId());
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerLoggedOutEvent event) {
        if (instance.running) {
            instance.getServer().stopTrackingPlayer(event.player.getGameProfile().getId());
        }
    }

    @Override
    public void sendToServer(String channel, long id, Message message, Target target) {
        bus.sendToServer(new FMLProxyPacket(BinaryPayload.of(new PacketBuffer(Unpooled.buffer()))
                .writeByte(JumpingClientImpl.PROTOCOL)
                .writeString(channel)
                .writeLong(id)
                .writeByte((byte)target.ordinal())
                .writeBinary(message).buff(), JumpingClientImpl.CHANNEL));
    }

    @Override
    public void sendToClient(UUID playerId, BinaryPayload forwarded) {
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerId);

        if (player == null) {
            return;
        }

        bus.sendTo(new FMLProxyPacket(forwarded.buff(), JumpingClientImpl.CHANNEL), player);
    }

    @Override
    public void sendToClient(String channel, long id, Message message, UUID playerId) {
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerId);

        if (player == null) {
            return;
        }

        bus.sendTo(new FMLProxyPacket(BinaryPayload.of(new PacketBuffer(Unpooled.buffer()))
                .writeByte(JumpingClientImpl.PROTOCOL)
                .writeString(channel)
                .writeLong(id)
                .writeByte((byte)Target.CLIENTS.ordinal())
                .writeBinary(message).buff(), JumpingClientImpl.CHANNEL), player);
    }

    @Override
    public Object getMinecraftServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
}
