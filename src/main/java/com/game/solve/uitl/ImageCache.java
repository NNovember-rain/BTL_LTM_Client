package com.game.solve.uitl;


import com.game.solve.model.Item;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {
    private static final Map<String, ImageIcon> cache = new HashMap<>();

    // Phương thức để lấy hình ảnh từ cache hoặc nạp nếu chưa có
    public static ImageIcon getImageIcon(String imagePath) {
        if (!cache.containsKey(imagePath)) {
            ImageIcon icon = InitUtils.createResizedIconFromResource(imagePath, 1100, 1090);
            cache.put(imagePath, icon);  // Đưa vào cache
        }
        return cache.get(imagePath);  // Trả về icon đã cache
    }

    // Phương thức tải toàn bộ hình ảnh vào cache
    public static void preloadImages(ArrayList<Item> items) {
        for (Item item : items) {
            getImageIcon(item.getImagePath());  // Tải và cache hình ảnh
        }
    }
}

