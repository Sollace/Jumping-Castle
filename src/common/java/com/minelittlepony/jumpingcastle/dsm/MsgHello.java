package com.minelittlepony.jumpingcastle.dsm;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.minelittlepony.jumpingcastle.api.Message;

public class MsgHello implements Message {

    @Expose
    public UUID playerId;

    @Expose
    public List<String> channels;

    public MsgHello() {

    }

    public MsgHello(UUID playerId, Set<String> channels) {
        this.playerId = playerId;
        this.channels = Lists.newArrayList(channels);
    }
}
