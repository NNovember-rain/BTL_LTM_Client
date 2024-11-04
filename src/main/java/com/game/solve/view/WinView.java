package com.game.solve.view;

import com.game.solve.model.User;
import com.game.solve.uitl.InitUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class WinView extends JPanel {
    private BufferedImage backgroundImage;
    private User user;

    public WinView() {
        setLayout(null);

        JLabel winLabel = InitUtils.creatLabel("Mission", 102, 300, 420, 100, 40);
        add(winLabel);
        JLabel winLabel2 = InitUtils.creatLabel("Compelete", 68, 350, 420, 100, 40);
        add(winLabel2);

        InputStream background = getClass().getResourceAsStream("/images/bgwin.jpg");
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
