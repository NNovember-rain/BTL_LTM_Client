package com.game.solve.view;

import com.game.solve.uitl.InitUtils;
import com.game.solve.model.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class EndGameView extends JPanel {

    private BufferedImage backgroundImage;
    private boolean checkWin =false;

    public EndGameView( ClientMainView clientMainView,AssignmentView assignmentView,ArrayList<Item> items) {
        setLayout(null);  // Sử dụng layout null để dễ dàng điều chỉnh vị trí các nút

        InputStream background = getClass().getResourceAsStream("/images/bgall.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);

        // Tạo một label để hiển thị thông điệp
        if(!this.checkWin) {
            JLabel messageLabel = InitUtils.creatLabel("Game Ending", 20, 75, 380, 63, 48);
            add(messageLabel);
        }else{
            JLabel messageLabel = InitUtils.creatLabel("Congratulation !", 100, 50, 380, 50, 30);
            add(messageLabel);
        }

        // Tạo nút "Chơi lại"
        JLabel messageRePlayLabel = InitUtils.creatLabel("Play Again", 108, 215, 175, 50, 30);
        add(messageRePlayLabel);

        JButton rePlayButton = InitUtils.createButtonFunctionMenu(75, 200, 230, 80, 410, 360, "/images/yellowbutton.png", messageRePlayLabel);
        rePlayButton.addActionListener(e -> restartGame( clientMainView, this,items,assignmentView));
        add(rePlayButton);

        // Tạo nút "Menu"
        JLabel messageMenuLabel = InitUtils.creatLabel("Menu", 153, 315, 175, 50, 30);
        add(messageMenuLabel);

        JButton menuButton = InitUtils.createButtonFunctionMenu(75, 300, 230, 80, 410, 360, "/images/yellowbutton.png", messageMenuLabel);
        menuButton.addActionListener(e -> returnToMainMenu(clientMainView));
        add(menuButton);

        // Tạo nút "Rank"
        JLabel messageRankLabel = InitUtils.creatLabel("Rank", 155, 415, 175, 50, 30);
        add(messageRankLabel);

        JButton rankButton = InitUtils.createButtonFunctionMenu(75, 400, 230, 80, 410, 360, "/images/yellowbutton.png", messageRankLabel);
        rankButton.addActionListener(e -> returnToBXH());
        add(rankButton);

        // Tạo nút "Quit"
        JLabel messageQuitLabel = InitUtils.creatLabel("Quit", 160, 515, 175, 50, 30);
        add(messageQuitLabel);

        JButton quitButton = InitUtils.createButtonFunctionMenu(75, 500, 230, 80, 410, 360, "/images/yellowbutton.png", messageQuitLabel);
        quitButton.addActionListener(e -> System.exit(0)); // Đóng ứng dụng
        add(quitButton);


    }

    // Phương thức để khởi động lại game
    private void restartGame(ClientMainView clientMainView, EndGameView endGameView, ArrayList<Item> items, AssignmentView assignmentView) {
        Container parent = this.getParent();
        clientMainView.resetItems();
        CardLayout layout = (CardLayout) parent.getLayout();
        layout.show(parent, "AssignmentView");  // Chuyển sang AssignmentView
        Collections.shuffle(items);
        assignmentView.setItems(items);

        // Tạo Timer để sau 4 giây chuyển sang ClientMainView
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clientMainView.setItems(items);
                // Chuyển sang ClientMainView sau 4 giây
                layout.show(parent, "ClientView");
                clientMainView.startCountdown();
            }
        }, 4000); // 4000ms = 4 giây
    }

    // Phương thức để trở về sảnh chính
    private void returnToMainMenu(ClientMainView clientMainView) {
        // Trở về Menu chính bằng cách chuyển qua CardLayout
        Container parent = this.getParent();
        clientMainView.resetItems();
        if (parent instanceof JPanel) {
            CardLayout layout = (CardLayout) parent.getLayout();
            layout.show(parent, "MenuGameView");  // Chuyển sang AssignmentView
        }
    }

    public void setCheckWin(Boolean checkWin) {
        this.checkWin = checkWin;
    }

    public Boolean isCheckWin() {
        return this.checkWin;
    }

    // Phương thức xem BXH
    private void returnToBXH() {
        JOptionPane.showMessageDialog(this, "Hiển thị bảng xếp hạng");
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
