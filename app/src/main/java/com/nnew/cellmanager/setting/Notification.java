package com.nnew.cellmanager.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nnew.cellmanager.R;

public class Notification extends AppCompatActivity {
    private static boolean noti_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    private void NotifyInfo() {}

    public static boolean get_noti_status() {return false;}
    public static boolean set_noti_status() {return false;}
}
