package com.example.sebi.licentatest;

import android.telephony.SmsManager;

public class SmsAlert {
    public static void sendSMS(String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("0744301846", null, message, null, null);
    }
}
