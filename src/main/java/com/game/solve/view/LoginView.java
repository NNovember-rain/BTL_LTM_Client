package com.game.solve.view;

import com.game.solve.model.DataSending;
import com.game.solve.model.User;
import com.game.solve.model.UserRequest;
import com.game.solve.socket.ManageSocket;
import com.game.solve.uitl.InitUtils;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.Socket;

public class LoginView  extends JPanel{
    private BufferedImage backgroundImage;

    public LoginView(JFrame parentFrame,  ManageSocket socket) {
        setLayout(null); // Sử dụng layout null để dễ dàng tùy chỉnh vị trí các thành phần

        InputStream background = getClass().getResourceAsStream("/images/bgall.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);

        // Thiết lập kích thước và vị trí của form ở giữa màn hình
        int formWidth = 400;
        int formHeight = 300;
        int xCenter = (parentFrame.getWidth() - formWidth) / 2;
        int yCenter = (parentFrame.getHeight() - formHeight) / 2;

        // Tạo label "Username"
        JLabel usernameLabel = InitUtils.creatLabel("Username", xCenter + 50, yCenter-40, 100, 30, 20);
        add(usernameLabel);

        // Tạo ô input cho "Username"
        JTextField usernameField = new JTextField();
        usernameField.setBounds(xCenter + 50, yCenter , 300, 30);
        add(usernameField);

        // Tạo label "Password"
        JLabel passwordLabel = InitUtils.creatLabel("Password", xCenter + 50, yCenter + 40, 100, 30, 20);
        add(passwordLabel);

        // Tạo ô input cho "Password"
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(xCenter + 50, yCenter + 80, 300, 30);
        add(passwordField);

        // Tạo nút "Login"
        JLabel labelLogin= InitUtils.creatLabel("Login",xCenter + 175, yCenter + 225, 120, 40,20);
        add(labelLogin);
        JButton loginButton = InitUtils.createButtonFunctionMenu(xCenter + 110, yCenter + 213, 188, 65,300,280, "/images/yellowbutton.png", labelLogin);
        loginButton.setText("Login");
        add(loginButton);

        // Tạo nút "Register"
        JLabel labelRegister= InitUtils.creatLabel("Register",xCenter + 162, yCenter + 325, 120, 40,20);
        add(labelRegister);
        JButton registerButton = InitUtils.createButtonFunctionMenu(xCenter + 110, yCenter + 313, 188, 65,300,280, "/images/yellowbutton.png", labelRegister);
        registerButton.setText("Register");
        add(registerButton);

        // Sự kiện nút "Login"
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            handleLogin(username,password,socket,parentFrame);
        });

        // Sự kiện nút "Register"
        registerButton.addActionListener(e -> {
            RegisterView registerView = new RegisterView(parentFrame,socket);
            parentFrame.setContentPane(registerView);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

    }

    public void handleLogin(String username, String password, ManageSocket socket,JFrame parentFrame) {
        DataSending<UserRequest> dataSending = new DataSending<>();
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName(username);
        userRequest.setPassword(password);
        dataSending.setData(userRequest);
        dataSending.setRequestType("Login");
        try {
            socket.getWriter().writeObject(dataSending);
            socket.getWriter().flush();
            socket.getWriter().reset();

            DataSending<User> dataSendingUser = (DataSending<User>) socket.getReader().readObject();
            User user= dataSendingUser.getData();
            if(user!=null) showMenuGame(socket,parentFrame,user);
            else JOptionPane.showMessageDialog(parentFrame, "Login Fail !");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showMenuGame(ManageSocket socket,JFrame parentFrame,User user) {
        MenuGameView menuGameView = new MenuGameView(parentFrame, socket,user);
        parentFrame.setContentPane(menuGameView);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
