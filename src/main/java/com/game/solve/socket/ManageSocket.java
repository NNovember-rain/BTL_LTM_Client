package com.game.solve.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ManageSocket {
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    public ManageSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new ObjectOutputStream(socket.getOutputStream());
        this.reader = new ObjectInputStream(socket.getInputStream());
    }

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public ObjectInputStream getReader() {
        return reader;
    }

    public void closeConnection() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
