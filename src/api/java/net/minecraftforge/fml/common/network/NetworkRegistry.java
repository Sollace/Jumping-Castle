package net.minecraftforge.fml.common.network;

public interface NetworkRegistry {
    static NetworkRegistry INSTANCE = null;

    FMLEventChannel newEventDrivenChannel(String channelName);
}
