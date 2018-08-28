package com.minelittlepony.jumpingcastle.api;

public enum Target {
    CLIENTS(true, false),
    SERVER(false, true),
    SERVER_AND_CLIENTS(true, true);

    private final boolean clients;

    private final boolean servers;

    Target(boolean clients, boolean servers) {
        this.clients = clients;
        this.servers = servers;
    }

    public boolean isClients() {
        return clients;
    }

    public boolean isServers() {
        return servers;
    }
}
