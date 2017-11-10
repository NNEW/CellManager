package com.nnew.cellmanager.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nnew.cellmanager.R;

public class Widget extends AppCompatActivity {
    private static boolean widget_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
    }

    private void ActiveWidget() {}

    public static void get_widget_status() {}
    public static void set_widget_status() {}
}
