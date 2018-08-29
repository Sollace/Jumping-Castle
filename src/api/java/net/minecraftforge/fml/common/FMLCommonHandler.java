package net.minecraftforge.fml.common;

import net.minecraft.server.MinecraftServer;

public interface FMLCommonHandler {
    static FMLCommonHandler instance() { return null; }

    MinecraftServer getMinecraftServerInstance();
}
