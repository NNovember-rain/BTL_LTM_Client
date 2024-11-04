package com.game.solve.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ManageSocket {
    private static ManageSocket instance; // Biến lưu trữ instance duy nhất
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    // Private constructor để ngăn việc khởi tạo từ bên ngoài
    private ManageSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new ObjectOutputStream(socket.getOutputStream());
        this.reader = new ObjectInputStream(socket.getInputStream());
    }

    // Phương thức để lấy instance duy nhất của ManageSocket
    public static synchronized ManageSocket getInstance(Socket socket) throws IOException {
        if (instance == null) { // Chỉ khởi tạo nếu instance chưa tồn tại
            instance = new ManageSocket(socket);
        }
        return instance;
    }

    // Phương thức để lấy ObjectOutputStream
    public ObjectOutputStream getWriter() {
        return writer;
    }

    // Phương thức để lấy ObjectInputStream
    public ObjectInputStream getReader() {
        return reader;
    }

    // Phương thức để đóng kết nối
    public void closeConnection() throws IOException {
        if (socket != null) {
            socket.close();
            instance = null; // Đặt instance về null để có thể khởi tạo lại sau khi đóng kết nối
        }
    }
}
