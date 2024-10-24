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
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class MenuGameView extends JPanel {
     private BufferedImage backgroundImage;

    public MenuGameView(JFrame parentFrame, ManageSocket socket,User user) {
        setLayout(null);  // Sử dụng layout null để dễ dàng điều chỉnh vị trí các nút

        // Tạo một label để hiển thị thông điệp
        JLabel messageLabel = InitUtils.creatLabel("Welcome to the Game",27,100,380,50,30);
        add(messageLabel);

        //Background
        InputStream background = getClass().getResourceAsStream("/images/bgall.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);



        // Tạo label cho chữ "Play"
        JLabel messagePlayLabel =InitUtils.creatLabel("Play",164, 265, 100, 50,30);
        add(messagePlayLabel);

        // Tạo nút "Play"
        JButton playButton = InitUtils.createButtonFunctionMenu(67, 250, 230, 80,410,360,"/images/greenbutton.png",messagePlayLabel);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playGame(parentFrame,socket,user);
            }
        });
        add(playButton);

        
        // Tạo label cho nút"Rank"
        JLabel messageRankLabel = InitUtils.creatLabel("Rank",152, 365, 100, 50,30);
        add(messageRankLabel);
        // Tạo nút "BXH Game"
        JButton bxhButton = InitUtils.createButtonFunctionMenu(75, 350, 230, 80,410,360,"/images/purplebutton.png",messageRankLabel);
        bxhButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToBXH(parentFrame);
            }
        });
        add(bxhButton);


        // Tạo label cho nút "Quit game"
        JLabel messageQuitLabel = InitUtils.creatLabel("Quit Game",120, 465, 150, 50,30);
        add(messageQuitLabel);
        // Tạo nút "Quit Game"
        JButton quitButton = InitUtils.createButtonFunctionMenu(75, 450, 230, 80,410,360,"/images/redbutton.png",messageQuitLabel);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Đóng ứng dụng
            }
        });
        add(quitButton);
    }

    // Phương thức để khởi động lại game
    private void playGame(JFrame parentFrame, ManageSocket socket,User user) {
    // Khởi tạo AssignmentView và hiển thị nó
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
