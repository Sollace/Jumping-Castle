package net.minecraftforge.fml.common.gameevent;

import net.minecraft.entity.player.EntityPlayer;

public abstract class PlayerEvent {
    public final EntityPlayer player = null;

    public static abstract class PlayerLoggedOutEvent extends PlayerEvent {

    }

    public static abstract class PlayerLoggedInEvent extends PlayerEvent {

    }
}
