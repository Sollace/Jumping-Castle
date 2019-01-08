package net.minecraftforge.fml.common.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public abstract class FMLNetworkEvent<T extends INetHandler> {

    public T getHandler() { return null; }

    public static abstract class CustomPacketEvent<S extends INetHandler> extends FMLNetworkEvent<S> {
        public FMLProxyPacket getPacket() { return null; }
    }

    public static abstract class ServerCustomPacketEvent extends CustomPacketEvent<INetHandlerPlayServer> {

    }

    public static abstract class ClientCustomPacketEvent extends CustomPacketEvent<INetHandlerPlayClient> {

    }
}
