package com.minelittlepony.jumpingcastle.api;

/**
 * Targetting method to control who recieves a message when sent.
 * Senders choose whether a message is recieved on other clients, just the server, or both.
 */
public enum Target {
    /**
     * Message is ignored by the server and forwarded to connected clients.
     */
    CLIENTS(true, false),
    /**
     * Only the server should process this message. Clients ignore it.
     */
    SERVER(false, true),
    /**
     * The server should process this message and forward it to all available clients.
     */
    SERVER_AND_CLIENTS(true, true);

    private final boolean clients;

    private final boolean servers;

    Target(boolean clients, boolean servers) {
        this.clients = clients;
        this.servers = servers;
    }

    /**
     * True if messages with this targetting must be procesed by the client.
     */
    public boolean isClients() {
        return clients;
    }

    /**
     * True if messages with this targetting must be procesed by the server.
     */
    public boolean isServers() {
        return servers;
    }
}
