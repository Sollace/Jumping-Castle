package net.minecraftforge.fml.common.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public abstract class FMLEventChannel {
    public abstract void register(Object o);

    public abstract void sendToServer(FMLProxyPacket pkt);

    public abstract void sendTo(FMLProxyPacket pkt, EntityPlayerMP player);
}
