package com.app_neighbrsnook.model;

public class EmojiModel {
    int emoji;
    String name;

    public EmojiModel(int emoji, String name) {
        this.emoji = emoji;
        this.name = name;
    }

    public int getEmoji() {
        return emoji;
    }

    public void setEmoji(int emoji) {
        this.emoji = emoji;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
