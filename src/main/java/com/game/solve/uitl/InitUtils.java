package com.game.solve.uitl;

import com.game.solve.model.Item;
import com.game.solve.view.ClientMainView;
import com.game.solve.view.MenuGameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class InitUtils {

    // TODO: load back ground
    public static BufferedImage initBackgroundImage(InputStream background) {
        try {
            if (background != null) {
                return ImageIO.read(background); // Trả về ảnh nền
            } else {
                System.out.println("Background image not found!");
            }
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }
        return null; // Trả về null nếu không tải được
    }

    // TODO: Tạo ảnh cho item và căn chỉnh lại tỉ lệ ảnh icon
    public static ImageIcon createResizedIconFromResource(String imagePath, int targetWidth, int targetHeight) {
        try {
            InputStream background = ClientMainView.class.getResourceAsStream(imagePath);
            if (background != null) {
                BufferedImage originalImage = ImageIO.read(background);

                // Tính toán tỷ lệ thu phóng hình ảnh
                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();
                double widthRatio = (double) targetWidth / originalWidth;
                double heightRatio = (double) targetHeight / originalHeight;
                double scaleFactor = Math.min(widthRatio, heightRatio); // Giữ tỷ lệ hình ảnh

                int newWidth = (int) (originalWidth * scaleFactor);
                int newHeight = (int) (originalHeight * scaleFactor);

                // Thay đổi kích thước hình ảnh
                Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                return new ImageIcon(resizedImage);
            } else {
                System.out.println("Image not found from path: " + imagePath);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error loading image from resource: " + e.getMessage());
            return null;
        }
    }

    //TODO: Load item game
    public static ArrayList<Item> getItemInGame() {
        ArrayList<Item> items = new ArrayList<>();
        String[] paths = {
                "/images/ca.png",
                "/images/coc.png",
                "/images/ngo.png",
                "/images/dinh.png",
                "/images/sach.png",
                "/images/nhan.png",
                "/images/nuoc.png",
                "/images/dui.png"
        };
        for (int i = 0; i < paths.length; ++i) {
            items.add(new Item(items.size(), "" + (i + 1), paths[i]));
        }
        return items;
    }

    public static JLabel creatLabel(String message,int x,int y,int w,int h,int size){
        JLabel jLabel = new JLabel(message);
        jLabel.setFont(new Font("Bungee", Font.BOLD, size));
        jLabel.setBounds(x, y, w, h);
        jLabel.setForeground(Color.BLACK);
        return jLabel;
    }


    public static JButton createButtonFunctionMenu(int x,int y,int w,int h,int scale_w,int scale_h,String imagePath,JLabel jLabel){
        JButton button = new JButton();
        button.setBounds(x, y, w, h);
        // Tạo icon gốc và icon khi hover sẵn
        ImageIcon icon = InitUtils.createResizedIconFromResource(imagePath, scale_w, scale_h);
        ImageIcon hoverIcon = InitUtils.createResizedIconFromResource(imagePath, scale_w + 10, scale_h + 10);
        button.setIcon(icon);
        return InitUtils.addEvenAndProperty(button,jLabel,scale_w,scale_h,icon,hoverIcon); //TODO add even
    }

    //TODO: thêm nhãn, thuộc tính và sự kiện cho button
    public static JButton addEvenAndProperty(JButton button,JLabel messageLabel,int scale_w,int scale_h,ImageIcon icon,ImageIcon hoverIcon ){
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        // Thêm sự kiện hover để thay đổi con trỏ chuột, icon và màu chữ
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Thay đổi con trỏ thành bàn tay
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                // Phóng to icon một chút

                button.setIcon(hoverIcon);

                // Đổi màu chữ thành màu khác khi hover
                messageLabel.setForeground(Color.PINK);
                messageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Trả lại con trỏ về mặc định
                button.setCursor(Cursor.getDefaultCursor());

                // Trả icon về kích thước ban đầu
                button.setIcon(icon);

                // Đổi màu chữ về màu ban đầu
                messageLabel.setForeground(Color.BLACK);
            }
        });
        return button;
    }


    //TODO: tạo CheckBox
    public static JCheckBox createCustomCheckBox(String text, int x, int y, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setBounds(x, y, 100, 30);
        checkBox.setOpaque(false); // Làm cho checkbox trong suốt
        checkBox.setFont(new Font("Arial", Font.PLAIN, 14)); // Thiết lập font chữ thông thường

        // Đặt checkbox được chọn mặc định nếu 'selected' là true
        checkBox.setSelected(selected);

        // Tùy chỉnh UI của checkbox
        checkBox.setUI(new javax.swing.plaf.basic.BasicCheckBoxUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                JCheckBox cb = (JCheckBox) c;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Kích thước và tọa độ của hình tròn viền
                int size = 21;
                int xCircle = 2; // Đẩy hình tròn sang phải bằng cách tăng giá trị x
                int yCircle = (cb.getHeight() - size) / 2 + 2; // Đẩy hình tròn xuống bằng cách tăng giá trị y
                Ellipse2D circle = new Ellipse2D.Double(xCircle, yCircle, size, size);

                // Vẽ viền hình tròn
                g2.setColor(Color.BLACK); // Màu viền hình tròn
                g2.draw(circle);

                // Nếu checkbox được chọn thì tô màu bên trong hình tròn
                if (cb.isSelected()) {
                    // Tô màu nhỏ hơn viền để tránh bị chớm ra ngoài
                    int innerSize = size - 3; // Giảm kích thước phần bên trong
                    int xInner = xCircle +2; // Đẩy phần màu bên trong sang phải
                    int yInner = yCircle + 2; // Đẩy phần màu bên trong xuống dưới
                    Ellipse2D innerCircle = new Ellipse2D.Double(xInner, yInner, innerSize, innerSize);

                    g2.setColor(Color.BLACK); // Màu tô khi được chọn
                    g2.fill(innerCircle); // Tô màu bên trong hình tròn nhỏ hơn
                }

                // Giữ nguyên màu chữ như checkbox thông thường
                g2.setColor(cb.getForeground());
                g2.setFont(cb.getFont());
                g2.drawString(cb.getText(), size + 12, yCircle + size - 4); // Điều chỉnh vị trí chữ để khớp với chiều cao
            }
        });

        return checkBox;

    }



}
