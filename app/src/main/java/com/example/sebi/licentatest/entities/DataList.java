package com.example.sebi.licentatest.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DataList {
    @SerializedName("dates")
    List<Data> dates =new ArrayList<>();
    public DataList() {
    }

    public List<Data> getDates() {
        return dates;
    }

    public void setDates(List<Data> dates) {
        this.dates = dates;
    }
    public String toJson() {

        Gson gson=new Gson();
        String jo=gson.toJson(this);
        return jo;
    }
}
