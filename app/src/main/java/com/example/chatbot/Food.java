package com.example.chatbot;

public class Food {
    private int id;
    private byte[] image;

    public Food(int id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
