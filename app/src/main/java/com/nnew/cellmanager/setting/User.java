package com.nnew.cellmanager.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nnew.cellmanager.R;

public class User extends AppCompatActivity {
    private String user_id;
    private String user_password;
    private static boolean account_linked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    private void SignUpAccount() {}
    private void SignInAccount() {}
    private void SignOutAccount() {}
    private void LinkAccount() {}

    public static boolean get_account_link() {return false;}
    public static boolean set_account_link() {return false;}
}
