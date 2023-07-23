package com.cbq.quickbot.service;

public class QuickBotService {
    private String name;
    private Integer age;

    public QuickBotService(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "QuickBotService{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
