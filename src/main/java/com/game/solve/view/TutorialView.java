package com.game.solve.view;

import com.game.solve.model.User;
import com.game.solve.uitl.InitUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class TutorialView extends JPanel {
    private BufferedImage backgroundImage;
    private User user;

    public TutorialView() {
        setLayout(null);  // Sử dụng layout null để dễ dàng tùy chỉnh vị trí các thành phần

        // Tạo label "Tutorial" với tọa độ cố định
        JLabel tutorialLabel = InitUtils.creatLabel("Tutorial", 75, 60, 300, 40, 50);
        add(tutorialLabel);

        JButton buttonGoHome=InitUtils.createButtonFunctionIcon(300,10,50,50,520,510,"/images/menuIcon.png");
        buttonGoHome.addActionListener(e -> showMenu()); // Đóng ứng dụng
        add(buttonGoHome);
        // Load ảnh nền
        InputStream background = getClass().getResourceAsStream("/images/tutori.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);
    }

    public void showMenu() {
        Container parent = this.getParent();
        CardLayout layout = (CardLayout) parent.getLayout();
        layout.show(parent, "MenuGameView");
    }

    public void createUserLabel(){
        JLabel nameUser = InitUtils.creatLabel(" "+this.user.getUsername(), 68, 111, 380, 63, 15);
        add(nameUser);
    }
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

