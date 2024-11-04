package com.game.solve.view;

import com.game.solve.model.Item;
import com.game.solve.uitl.ImageCache;
import com.game.solve.uitl.InitUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class AssignmentView extends JPanel {

    private BufferedImage backgroundImage;
    private ArrayList<Item> items = new ArrayList<>();  // Danh sách item hiện tại
    private JPanel itemsPanel;  // Panel để hiển thị item

    public AssignmentView() {
        setLayout(null);

        // Tải ảnh nền từ tài nguyên
        InputStream background = getClass().getResourceAsStream("/images/bgAssign.jpg");
        backgroundImage = InitUtils.initBackgroundImage(background);

        // Tạo panel để hiển thị item
        itemsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        itemsPanel.setBounds(40, 300, 300, 145);
        itemsPanel.setOpaque(false);
        add(itemsPanel);
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
        updateItemsPanel();  // Cập nhật giao diện để hiển thị các item mới
    }

    private void updateItemsPanel() {
        // Xóa tất cả các thành phần hiện tại trên itemsPanel để làm mới
        itemsPanel.removeAll();

        // Hiển thị danh sách các item trong panel với tỷ lệ đúng
        for (Item item : items) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);

            // Lấy icon từ cache và thêm vào label
            ImageIcon icon = ImageCache.getImageIcon(item.getImagePath());
            if (icon != null) {
                label.setIcon(icon);
            }
            itemsPanel.add(label); // Thêm label chứa icon vào itemsPanel
        }

        itemsPanel.revalidate();  // Cập nhật lại giao diện itemsPanel
        itemsPanel.repaint();     // Vẽ lại panel
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
