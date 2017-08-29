package com.android.chat.chat.Chat.Socket;

import java.net.URISyntaxException;

import io.socket.client.IO;


public class Socket {
    private io.socket.client.Socket servSocket;
    {
        try {
            servSocket = IO.socket("http://10.204.219.145:3000");
        } catch (URISyntaxException e) {}
    }
    public Socket(){

    }

    public io.socket.client.Socket getServSocket() {
        return servSocket;
    }
}
