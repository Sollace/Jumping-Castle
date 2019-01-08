package net.minecraftforge.fml.common;

import net.minecraft.server.MinecraftServer;

public abstract class FMLCommonHandler {
    public static FMLCommonHandler instance() { return null; }

    public MinecraftServer getMinecraftServerInstance() { return null; }
}
