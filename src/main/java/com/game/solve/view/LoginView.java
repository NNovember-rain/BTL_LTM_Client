package com.game.solve.view;

import com.game.solve.socket.ManageSocket;
import com.game.solve.uitl.InitUtils;
import com.game.solve.model.DataSending;
import com.game.solve.model.User;
import com.game.solve.model.UserRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class LoginView extends JPanel {
    private BufferedImage backgroundImage;

    public LoginView(MenuGameView menuGameView) {
        setLayout(null);  // Sử dụng layout null để dễ dàng tùy chỉnh vị trí các thành phần

        JLabel messageLabel = InitUtils.creatLabel("Login", 102, 75, 380, 63, 55);
        add(messageLabel);

        // Load ảnh nền
        InputStream background = getClass().getResourceAsStream("/images/bgall.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);

        // Tạo label "Username" với tọa độ cố định
        JLabel usernameLabel = InitUtils.creatLabel("Username", 35, 230, 120, 30, 20);
        add(usernameLabel);

        // Tạo ô input cho "Username" với tọa độ cố định
        JTextField usernameField = new JTextField();
        usernameField.setBounds(35, 270, 300, 30);
        add(usernameField);

        // Tạo label "Password" với tọa độ cố định
        JLabel passwordLabel = InitUtils.creatLabel("Password", 35, 330, 130, 30, 20);
        add(passwordLabel);

        // Tạo ô input cho "Password" với tọa độ cố định
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(35, 370, 300, 30);
        add(passwordField);

        // Tạo nút "Login" với tọa độ cố định
        JLabel labelLogin = InitUtils.creatLabel("Login", 160, 460, 120, 40, 20);
        add(labelLogin);
        JButton loginButton = InitUtils.createButtonFunctionMenu(87, 450, 210, 65, 340, 300, "/images/yellowbutton.png", labelLogin);
        loginButton.setText("Login");
        add(loginButton);

        // Tạo nút "Register" với tọa độ cố định
        JLabel labelRegister = InitUtils.creatLabel("Register", 144, 555, 120, 40, 20);
        add(labelRegister);
        JButton registerButton = InitUtils.createButtonFunctionMenu(87, 545, 210, 65, 340, 300, "/images/yellowbutton.png", labelRegister);
        registerButton.setText("Register");
        add(registerButton);

        // Sự kiện nút "Login"
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User userLogin=handleLogin(username, password);
            if(userLogin!=null){
                showMenuGame(userLogin, menuGameView);
            }else JOptionPane.showMessageDialog(this, "Login Failed!");

        });

        registerButton.addActionListener(e -> {
            Container parent = this.getParent();
            CardLayout layout = (CardLayout) parent.getLayout();
            layout.show(parent, "RegisterView");
        });
    }

    // Xử lý logic đăng nhập
    public User handleLogin(String username, String password) {
        try {
            // Lấy instance của ManageSocket
            ManageSocket socket = ManageSocket.getInstance(null);

            DataSending<UserRequest> dataSending = new DataSending<>();
            UserRequest userRequest = new UserRequest();
            userRequest.setUserName(username);
            userRequest.setPassword(password);
            dataSending.setData(userRequest);
            dataSending.setRequestType("Login");

            socket.getWriter().writeObject(dataSending);
            socket.getWriter().flush();
            socket.getWriter().reset();

            DataSending<User> dataSendingUser = (DataSending<User>) socket.getReader().readObject();
            User user = dataSendingUser.getData();
            if (user != null) {
                return user;// Nếu đăng nhập thành công, chuyển sang màn hình Menu
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Phương thức để hiển thị MenuGame sau khi đăng nhập thành công
    public void showMenuGame(User user, MenuGameView menuGameView) {
        menuGameView.setUser(user); // Gán đối tượng user cho menuGameView trước khi cập nhật giao diện
        menuGameView.createUserLabel(); // Tạo nhãn chào mừng người dùng
        Container parent = this.getParent();
        CardLayout layout = (CardLayout) parent.getLayout();
        layout.show(parent, "MenuGameView");
        menuGameView.revalidate();
        menuGameView.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
