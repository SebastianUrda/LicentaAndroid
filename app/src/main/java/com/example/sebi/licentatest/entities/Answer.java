package com.example.sebi.licentatest.entities;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Answer {
    @SerializedName("date")
    private Date date;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("questionId")
    private int questionId;
    @SerializedName("userId")
    private int userId;
    @SerializedName("answer")
    private int answer;

    public Answer() {
    }

    public Answer(Date date, Double latitude, Double longitude, int questionId, int userId, int answer) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.questionId = questionId;
        this.userId = userId;
        this.answer = answer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "Answer{" +
                "date='" + date + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", questionId=" + questionId +
                ", userId=" + userId +
                ", answer=" + answer +
                '}';
    }
}
