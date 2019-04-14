package com.example.sebi.licentatest.list;


import com.google.gson.annotations.SerializedName;

public class Answer {
    @SerializedName("date")
    private String date;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("questionId")
    private int questionId;
    @SerializedName("userId")
    private int userId;
    @SerializedName("answer")
    private int answer;

    public Answer() {
    }

    public Answer(String date, String latitude, String longitude, int questionId, int userId, int answer) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.questionId = questionId;
        this.userId = userId;
        this.answer = answer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
