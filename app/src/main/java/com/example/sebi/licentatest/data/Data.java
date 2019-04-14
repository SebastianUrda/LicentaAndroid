package com.example.sebi.licentatest.data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("userId")
    private int userId;
    @SerializedName("deviceId")
    private int deviceId;
    @SerializedName("date")
    private String date;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("lpg")
    private double lpg;
    @SerializedName("co")
    private double co;
    @SerializedName("smoke")
    private double smoke;
    @SerializedName("co2")
    private double co2;
    @SerializedName("backTemp")
    private double backTemp;
    @SerializedName("humidity")
    private double humidity;
    @SerializedName("dust")
    private double dust;
    @SerializedName("pressure")
    private double pressure;
    @SerializedName("frontTemp")
    private double frontTemp;
    @SerializedName("vis")
    private double vis;
    @SerializedName("ir")
    private double ir;
    @SerializedName("uv")
    private double uv;

    public Data(String date, String latitude, String longitude, double lpg, double co, double smoke, double co2, double backTemp, double humidity, double dust, double pressure, double frontTemp, double vis, double ir, double uv) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lpg = lpg;
        this.co = co;
        this.smoke = smoke;
        this.co2 = co2;
        this.backTemp = backTemp;
        this.humidity = humidity;
        this.dust = dust;
        this.pressure = pressure;
        this.frontTemp = frontTemp;
        this.vis = vis;
        this.ir = ir;
        this.uv = uv;
    }

    public Data() {
    }

    public Data(int userId, int deviceId, String date, String latitude, String longitude, double lpg, double co, double smoke, double co2, double backTemp, double humidity, double dust, double pressure, double frontTemp, double vis, double ir, double uv) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lpg = lpg;
        this.co = co;
        this.smoke = smoke;
        this.co2 = co2;
        this.backTemp = backTemp;
        this.humidity = humidity;
        this.dust = dust;
        this.pressure = pressure;
        this.frontTemp = frontTemp;
        this.vis = vis;
        this.ir = ir;
        this.uv = uv;
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

    public void setLatitude(String lattitude) {
        this.latitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public double getLpg() {
        return lpg;
    }

    public void setLpg(double lpg) {
        this.lpg = lpg;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }

    public double getSmoke() {
        return smoke;
    }

    public void setSmoke(double smoke) {
        this.smoke = smoke;
    }

    public double getCo2() {
        return co2;
    }

    public void setCo2(double co2) {
        this.co2 = co2;
    }


    public double getBackTemp() {
        return backTemp;
    }

    public void setBackTemp(double backTemp) {
        this.backTemp = backTemp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getDust() {
        return dust;
    }

    public void setDust(double dust) {
        this.dust = dust;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getFrontTemp() {
        return frontTemp;
    }

    public void setFrontTemp(double frontTemp) {
        this.frontTemp = frontTemp;
    }

    public double getVis() {
        return vis;
    }

    public void setVis(double vis) {
        this.vis = vis;
    }

    public double getIr() {
        return ir;
    }

    public void setIr(double ir) {
        this.ir = ir;
    }

    public double getUv() {
        return uv;
    }

    public void setUv(double uv) {
        this.uv = uv;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String toJson() {
        Gson gson=new Gson();
        String jo=gson.toJson(this);
        return jo;
       }

    @Override
    public String toString() {
        return "Data{" +
                "userId=" + userId +
                ", deviceId=" + deviceId +
                ", date='" + date + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", lpg=" + lpg +
                ", co=" + co +
                ", smoke=" + smoke +
                ", co2=" + co2 +
                ", backTemp=" + backTemp +
                ", humidity=" + humidity +
                ", dust=" + dust +
                ", pressure=" + pressure +
                ", frontTemp=" + frontTemp +
                ", vis=" + vis +
                ", ir=" + ir +
                ", uv=" + uv +
                '}';
    }
}
