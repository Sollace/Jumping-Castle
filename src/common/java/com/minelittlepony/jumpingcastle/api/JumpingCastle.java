package com.minelittlepony.jumpingcastle.api;

import com.minelittlepony.jumpingcastle.JumpingCastleImpl;

public interface JumpingCastle {
    static IChannel listen(String channelName) {
        return JumpingCastleImpl.instance().listen(channelName);
    }
}
