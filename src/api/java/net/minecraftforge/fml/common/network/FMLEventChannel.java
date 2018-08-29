package net.minecraftforge.fml.common.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public interface FMLEventChannel {
    void register(Object o);

    void sendToServer(FMLProxyPacket pkt);

    void sendTo(FMLProxyPacket pkt, EntityPlayerMP player);
}
