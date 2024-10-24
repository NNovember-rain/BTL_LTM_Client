package com.game.solve.view;

import com.game.solve.model.User;
import com.game.solve.socket.ManageSocket;
import com.game.solve.uitl.InitUtils;
import com.game.solve.model.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class AssignmentView extends JPanel {  

    private BufferedImage backgroundImage;  

    public AssignmentView(ArrayList<Item> items, ManageSocket socket, User user) {
        setLayout(null);

//        JLabel messageLabel = InitUtils.creatLabel("Assignment",70, 60, 380, 50,40);
//        add(messageLabel);

        // Tải ảnh nền từ tài nguyên
        InputStream background = getClass().getResourceAsStream("/images/bgAssigndog.png");
        backgroundImage = InitUtils.initBackgroundImage(background);

        // Hiển thị danh sách các item ở giữa màn hình với tỷ lệ đúng
        JPanel itemsPanel = new JPanel(new GridLayout(2, 4, 10, 10));  
        itemsPanel.setBounds(40, 300, 300, 145);
        itemsPanel.setOpaque(false);  

        // Thêm các item vào panel
        for (Item item : items) {  
            JLabel label = new JLabel();  
            label.setHorizontalAlignment(JLabel.CENTER);  
            ImageIcon icon = InitUtils.createResizedIconFromResource(item.getImagePath(), 1100, 1090);  
            if (icon != null) {  
                label.setIcon(icon);  
            }
            itemsPanel.add(label);  
        }

        add(itemsPanel);  
    }

    @Override
    protected void paintComponent(Graphics g) {  
        super.paintComponent(g);  
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);  
        }  
    }  
}
