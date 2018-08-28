package com.minelittlepony.jumpingcastle;

public enum Target {
    ALL_CLIENTS(true, false),
    SERVER(false, true),
    SERVER_AND_CLIENTS(true, true);

    protected final boolean clients;

    protected final boolean servers;

    Target(boolean clients, boolean servers) {
        this.clients = clients;
        this.servers = servers;
    }
}
