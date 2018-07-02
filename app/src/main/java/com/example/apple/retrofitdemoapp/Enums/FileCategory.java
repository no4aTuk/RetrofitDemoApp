package com.example.apple.retrofitdemoapp.Enums;

public enum FileCategory {
    Avatar ("avatar"),
    Image("image"),
    Audio("audio"),
    Video("video"),
    Document("document");

    private final String name;

    FileCategory(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
