package com.game.solve.view;

import com.game.solve.socket.ManageSocket;
import com.game.solve.uitl.InitUtils;
import com.game.solve.model.DataSending;
import com.game.solve.model.UserRequest;
import com.game.solve.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class RegisterView extends JPanel {
    private BufferedImage backgroundImage;

    // Constructor
    public RegisterView(MenuGameView menuGameView) {
        setLayout(null); // Sử dụng layout null để dễ dàng tùy chỉnh vị trí các thành phần

        JLabel messageLabel = InitUtils.creatLabel("Register", 65, 60, 380, 63, 55);
        add(messageLabel);

        // Load ảnh nền
        InputStream background = getClass().getResourceAsStream("/images/bgall.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);

        // Tạo label "Username" với tọa độ cố định
        JLabel usernameLabel = InitUtils.creatLabel("Username", 35, 170, 140, 30, 20);
        add(usernameLabel);
        // Tạo ô input cho "Username" với tọa độ cố định
        JTextField usernameField = new JTextField();
        usernameField.setBounds(35, 210, 300, 30);
        add(usernameField);

        // Tạo label "Password" với tọa độ cố định
        JLabel passwordLabel = InitUtils.creatLabel("Password", 35, 260, 130, 30, 20);
        add(passwordLabel);
        // Tạo ô input cho "Password" với tọa độ cố định
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(35, 300, 300, 30);
        add(passwordField);

        // Tạo label "Confirm Password" với tọa độ cố định
        JLabel confirmPasswordLabel = InitUtils.creatLabel("Confirm Password", 35, 350, 210, 30, 20);
        add(confirmPasswordLabel);
        // Tạo ô input cho "Confirm Password" với tọa độ cố định
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(35, 390, 300, 30);
        add(confirmPasswordField);

        //TODO: Tạo "Gender"
        JLabel genderLabel = InitUtils.creatLabel("Gender",  35,  430, 100, 30, 20);
        add(genderLabel);
        JCheckBox maleCheckBox = InitUtils.createCustomCheckBox("Male",  35, 460, true);
        add(maleCheckBox);
        JCheckBox femaleCheckBox = InitUtils.createCustomCheckBox("Female",  150,  460, false);
        add(femaleCheckBox);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleCheckBox);
        genderGroup.add(femaleCheckBox);

        // Tạo nút "Register" với tọa độ cố định
        JLabel labelRegister = InitUtils.creatLabel("Register", 140, 530, 120, 40, 20);
        add(labelRegister);
        JButton registerButton = InitUtils.createButtonFunctionMenu(85, 520, 210, 65, 340, 300, "/images/yellowbutton.png", labelRegister);
        registerButton.setText("Register");
        add(registerButton);

        JLabel labelBack = InitUtils.creatLabel("Back To Login", 113, 620, 160, 40, 20);
        add(labelBack);
        JButton backLoginButton = InitUtils.createButtonFunctionMenu(80, 610, 210, 65, 350, 300, "/images/yellowbutton.png", labelBack);
        registerButton.setText("backlogin");
        add(backLoginButton);

        // Sự kiện nút "Register"
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            if (password.equals(confirmPassword)) {
                handleRegister(username, password, menuGameView);
            } else {
                JOptionPane.showMessageDialog(this, "Passwords do not match!");
            }
        });

        backLoginButton.addActionListener(e -> {
            Container parent = this.getParent();
            CardLayout layout = (CardLayout) parent.getLayout();
            layout.show(parent, "LoginView");
        });
    }

    // Xử lý logic đăng ký
    public void handleRegister(String username, String password, MenuGameView menuGameView) {
        try {
            // Lấy instance của ManageSocket
            ManageSocket socket = ManageSocket.getInstance(null);

            DataSending<UserRequest> dataSending = new DataSending<>();
            UserRequest userRequest = new UserRequest();
            userRequest.setUserName(username);
            userRequest.setPassword(password);
            dataSending.setData(userRequest);
            dataSending.setRequestType("Register");

            socket.getWriter().writeObject(dataSending);
            socket.getWriter().flush();
            socket.getWriter().reset();

            DataSending<String> dataSendingUser = (DataSending<String>) socket.getReader().readObject();
            String message = dataSendingUser.getData();
            if (message.equals("Success")) {
                JOptionPane.showMessageDialog(this, message);
            } else {
                JOptionPane.showMessageDialog(this, message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
