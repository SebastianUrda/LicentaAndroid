package com.example.sebi.licentatest.entities;

import com.google.gson.annotations.SerializedName;

public class Device {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public Device() {
    }

    public Device(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
