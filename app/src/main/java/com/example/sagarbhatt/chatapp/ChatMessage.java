package com.example.sagarbhatt.chatapp;

/**
 * Created by Sagar Bhatt on 21-02-2016.
 */
public class ChatMessage {
    String message;
    String name;

    public ChatMessage() {
    }

    public ChatMessage(String name, String message) {
        this.message = message;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}