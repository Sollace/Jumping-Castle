package net.minecraftforge.fml.common.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public interface FMLNetworkEvent<T> {

    T getHandler();

    interface CustomPacketEvent<S extends INetHandler> extends FMLNetworkEvent<S> {
        FMLProxyPacket getPacket();
    }

    interface ServerCustomPacketEvent extends CustomPacketEvent<INetHandlerPlayServer> {

    }

    interface ClientCustomPacketEvent extends CustomPacketEvent<INetHandlerPlayClient> {

    }
}
