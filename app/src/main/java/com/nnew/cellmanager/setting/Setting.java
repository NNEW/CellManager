package com.nnew.cellmanager.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nnew.cellmanager.R;

public class Setting extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    private boolean ToggleNotiCheck() {return false;}
    private boolean ToggleAccountCheck() {return false;}
    private boolean ToggleWidgetCheck() {return false;}
}
