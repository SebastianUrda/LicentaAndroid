package com.example.sebi.licentatest.entities;

public class Question {
    private int id;
    private String text;
    private int response;


    public Question() {
    }

    public Question(String text) {
        this.text = text;
    }

    public Question(String text, int response) {
        this.text = text;
        this.response = response;
    }

    public Question(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", response=" + response +
                '}';
    }
}
