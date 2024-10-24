package com.game.solve.view;

import com.game.solve.socket.ManageSocket;
import com.game.solve.uitl.InitUtils;
import com.game.solve.model.Item;
import com.game.solve.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class EndGameView extends JPanel {

    private final JFrame parentFrame;
    private BufferedImage backgroundImage;

    public EndGameView(JFrame parentFrame, ManageSocket socket,User user) {
        this.parentFrame = parentFrame;
        setLayout(null);  // Sử dụng layout null để dễ dàng điều chỉnh vị trí các nút

        InputStream background = getClass().getResourceAsStream("/images/bgall.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);

        // Tạo một label để hiển thị thông điệp
        JLabel messageLabel = InitUtils.creatLabel("Game Ending",100, 50, 380, 50,30);
        add(messageLabel);


        // Tạo nút "Chơi lại"

        // Tạo label cho chữ "re Play"
        JLabel messageRePlayLabel = InitUtils.creatLabel("Play Again",113, 215, 175, 50,30);
        add(messageRePlayLabel);

        JButton rePlayButton = InitUtils.createButtonFunctionMenu(75, 200, 230, 80,410,360,"/images/yellowbutton.png",messageRePlayLabel);
        rePlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame(parentFrame,socket,user);
            }
        });
        add(rePlayButton);


        // Tạo label cho chữ "Menu"
        JLabel messageMenuLabel = InitUtils.creatLabel("Menu",155, 315, 175, 50,30);
        add(messageMenuLabel);
        // Tạo nút "Về sảnh chính"
        JButton menuButton = InitUtils.createButtonFunctionMenu(75, 300, 230, 80,410,360,"/images/yellowbutton.png",messageMenuLabel);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToMainMenu(parentFrame);
            }
        });
        add(menuButton);


        // Tạo label cho nút "Rank"
        JLabel messageRankLabel = InitUtils.creatLabel("Rank",155, 415, 175, 50,30);
        add(messageRankLabel);
        // Tạo nút "BXH Game"
        JButton rankButton = InitUtils.createButtonFunctionMenu(75, 400, 230, 80,410,360,"/images/yellowbutton.png",messageRankLabel);
        rankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToBXH(parentFrame);
            }
        });
        add(rankButton);

        // Tạo label cho nút "Quit"
        JLabel messageQuitLabel = InitUtils.creatLabel("Quit",160, 515, 175, 50,30);
        add(messageQuitLabel);
        // Tạo nút "Quit Game"
        JButton quitButton = InitUtils.createButtonFunctionMenu(75, 500, 230, 80,410,360,"/images/yellowbutton.png",messageQuitLabel);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Đóng ứng dụng
            }
        });
        add(quitButton);
    }

    // Phương thức để khởi động lại game
    private void restartGame(JFrame parentFrame,ManageSocket socket,User user) {
        // Khởi động lại game bằng cách gọi lại ClientView
        ArrayList<Item> items = InitUtils.getItemInGame();
        Collections.shuffle(items);
        AssignmentView assignmentView = new AssignmentView(items,socket,user);
        parentFrame.setContentPane(assignmentView);
        parentFrame.revalidate();
        parentFrame.repaint();

        // Sử dụng Timer để trì hoãn việc hiển thị ClientMainView
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sau 3 giây, chuyển sang ClientMainView
                ClientMainView clientMainView = new ClientMainView(user, items, parentFrame,socket);
                parentFrame.setContentPane(clientMainView);
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        });
        timer.setRepeats(false); // Chỉ chạy một lần
        timer.start(); // Bắt đầu bộ đếm thời gian
    }

    // Phương thức để trở về sảnh chính
    private void returnToMainMenu(JFrame parentFrame) {
        // Xử lý trở về sảnh chính (có thể mở một menu chính hoặc giao diện khác)
        JOptionPane.showMessageDialog(parentFrame, "Về sảnh chính");
    }

    // Phương thức xem BXH
    private void returnToBXH(JFrame parentFrame) {
        // Xử lý trở về sảnh chính (có thể mở một menu chính hoặc giao diện khác)
        JOptionPane.showMessageDialog(parentFrame, "Về sảnh BXH");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
