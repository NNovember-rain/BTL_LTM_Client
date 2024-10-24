package com.game.solve.model;

public class Item {
    private int id;
    private String name;
    private String imagePath; // Đường dẫn cục bộ tới hình ảnh

    public Item(int id, String name, String imagePath) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    // Thêm các phương thức getter và setter nếu cần thiết
}
