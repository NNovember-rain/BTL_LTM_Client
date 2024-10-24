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



public class RegisterView extends JPanel {
    private BufferedImage backgroundImage;

    public RegisterView(JFrame parentFrame, ManageSocket socket){
        setLayout(null); // Sử dụng layout null để dễ dàng tùy chỉnh vị trí các thành phần

        // Tải hình nền cho form đăng ký
        InputStream background = getClass().getResourceAsStream("/images/bgall.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);

        // Thiết lập kích thước và vị trí của form ở giữa màn hình
        int formWidth = 400;
        int formHeight = 480;
        int xCenter = (parentFrame.getWidth() - formWidth) / 2;
        int yCenter = (parentFrame.getHeight() - formHeight) / 2;

        // TODO: Tạo "Username"
        JLabel usernameLabel = InitUtils.creatLabel("Username", xCenter + 42, yCenter + 5, 100, 30, 20);
        add(usernameLabel);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(xCenter + 42, yCenter + 40, 300, 30);
        add(usernameField);

        // TODO: Tạo label "Password"
        JLabel passwordLabel = InitUtils.creatLabel("Password", xCenter + 42, yCenter + 85, 100, 30, 20);
        add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(xCenter + 42, yCenter + 120, 300, 30);
        add(passwordField);


        //TODO: Tạo label "Confirm Password"
        JLabel confirmPasswordLabel = InitUtils.creatLabel("Confirm Password", xCenter + 42, yCenter + 165, 190, 30, 20);
        add(confirmPasswordLabel);
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(xCenter + 42, yCenter + 200, 300, 30);
        add(confirmPasswordField);

        //TODO: Tạo "Gender"
        JLabel genderLabel = InitUtils.creatLabel("Gender", xCenter + 42, yCenter + 245, 100, 30, 20);
        add(genderLabel);
        JCheckBox maleCheckBox = InitUtils.createCustomCheckBox("Male", xCenter + 42, yCenter + 280, true);
        add(maleCheckBox);
        JCheckBox femaleCheckBox = InitUtils.createCustomCheckBox("Female", xCenter + 150, yCenter + 280, false);
        add(femaleCheckBox);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleCheckBox);
        genderGroup.add(femaleCheckBox);

        //TODO: Tạo "Register"
        JLabel labelRegister = InitUtils.creatLabel("Register", xCenter + 157, yCenter + 325, 130, 40, 20);
        add(labelRegister);
        JButton registerButton = InitUtils.createButtonFunctionMenu(xCenter + 110, yCenter + 313, 190, 65, 300, 280, "/images/yellowbutton.png", labelRegister);
        registerButton.setText("Register");
        add(registerButton);

        // TODO: nút "Back to Login"
        JLabel labelBack = InitUtils.creatLabel("Back To Login", xCenter + 130, yCenter + 405, 150, 40, 20);
        add(labelBack);
        JButton backButton = InitUtils.createButtonFunctionMenu(xCenter + 110, yCenter + 395, 190, 65, 300, 280, "/images/yellowbutton.png", labelBack);
        backButton.setText("Back");
        add(backButton);

        //TODO: Sự kiện nút "Register"
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword()); // Chuyển đổi mảng char[] thành chuỗi
            String confirmPassword = new String(confirmPasswordField.getPassword()); // Chuyển đổi mảng char[] thành chuỗi


            // Lấy giới tính đã chọn
            String gender = null;
            if (maleCheckBox.isSelected()) {
                gender = "Male";
            } else if (femaleCheckBox.isSelected()) {
                gender = "Female";
            }

            // Kiểm tra xem mật khẩu có khớp không
            if (password.compareTo(confirmPassword) == 0) {
                registerUser(username, password, gender, socket, parentFrame);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Passwords do not match!");
            }
        });

        //TODO: Sự kiện nút "Back to Login"
        backButton.addActionListener(e -> {
            parentFrame.setContentPane(new LoginView(parentFrame, socket)); // Quay lại form đăng nhập
            parentFrame.revalidate();
            parentFrame.repaint();
        });
    }

    public void registerUser(String username, String password, String gender, ManageSocket socket, JFrame parentFrame) {
        DataSending<UserRequest> dataSending = new DataSending<>();
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName(username);
        userRequest.setPassword(password);
        userRequest.setGender(gender);
        dataSending.setData(userRequest);
        dataSending.setRequestType("Register");
        try {
            socket.getWriter().writeObject(dataSending);
            socket.getWriter().flush();
            socket.getWriter().reset();

            DataSending<String> response = (DataSending<String>) socket.getReader().readObject();
            String message = response.getData();
            if(response.getData().equals("Success")) JOptionPane.showMessageDialog(parentFrame, "Register success");
            else JOptionPane.showMessageDialog(parentFrame, "Register failed");



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public static void showCustomDialog(JFrame parentFrame, String message, ManageSocket socket, User user) {
//        // Tạo JDialog
//        JDialog dialog = new JDialog(parentFrame, "", true);
//        dialog.setLayout(null); // Sử dụng layout null để tự do đặt vị trí các thành phần
//
//        // Tạo một JLabel chứa ảnh nền
//        ImageIcon backgroundIcon = new ImageIcon(RegisterView.class.getResource("/images/bgall.jpg")); // Đường dẫn đến ảnh nền
//        JLabel backgroundLabel = new JLabel(backgroundIcon);
//        backgroundLabel.setBounds(0, 0, 300, 200); // Đặt kích thước ảnh nền phù hợp với kích thước dialog
//        dialog.setContentPane(backgroundLabel); // Đặt backgroundLabel làm content pane của dialog
//        backgroundLabel.setLayout(null); // Sử dụng layout null để tự do đặt vị trí các thành phần bên trong
//
//        // Tạo nội dung tin nhắn (Label) và đặt vị trí
//        JLabel messageLabel = new JLabel("<html><div style='text-align: center; font-size: 14px; color: white;'>" + message + "</div></html>");
//        if(message.equals("Success")) messageLabel.setBounds(110, 20, 200, 50); // Đặt vị trí cho label chứa message
//        else messageLabel.setBounds(50, 20, 200, 50);
//        backgroundLabel.add(messageLabel); // Thêm messageLabel vào backgroundLabel
//
//        // Tạo các nút và đặt vị trí
//        JButton button1 = new JButton("Go to game lobby");
//        button1.setBounds(30, 100, 110, 30); // Đặt vị trí cho nút 1
//        button1.addActionListener(e -> {
//            dialog.dispose();
//            MenuGameView menuGameView = new MenuGameView(parentFrame, socket,user);
//            parentFrame.setContentPane(menuGameView);
//            parentFrame.revalidate();
//            parentFrame.repaint();
//        });
//
//        JButton button2 = new JButton("Exit");
//        button2.setBounds(160, 100, 100, 30); // Đặt vị trí cho nút 2
//        button2.addActionListener(e -> {
//            dialog.dispose();
//            parentFrame.dispose();
//        });
//
//        // Thêm các nút vào backgroundLabel (thay vì thêm trực tiếp vào dialog)
//        backgroundLabel.add(button1);
//        backgroundLabel.add(button2);
//
//        // Cài đặt kích thước và hiển thị dialog
//        dialog.setSize(300, 200);
//        dialog.setLocationRelativeTo(parentFrame); // Hiển thị ở giữa màn hình
//        dialog.setVisible(true);
//    }







    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}


