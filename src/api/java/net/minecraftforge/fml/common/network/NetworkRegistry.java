package net.minecraftforge.fml.common.network;

public abstract class NetworkRegistry {
    public static NetworkRegistry INSTANCE = null;

    public FMLEventChannel newEventDrivenChannel(String channelName) {
        return null;
    }
}
