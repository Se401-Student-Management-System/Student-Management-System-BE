package com.example.studentmanagement.designpattern.observer;

public interface Subscriber {
    void update(String eventType, Object data, String message);
}