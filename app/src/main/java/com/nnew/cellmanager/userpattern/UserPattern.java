package com.nnew.cellmanager.userpattern;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nnew.cellmanager.R;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;

public class UserPattern extends AppCompatActivity {
    private static int usage_table;
    private static int anlysis_table;
    private static int category_table;

    private void AppendUsage(int ago, int timeRange) {
        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
        Calendar todayCal = new GregorianCalendar();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.add(Calendar.DATE, 1-ago);

        Calendar yesterdayCal = new GregorianCalendar();
        yesterdayCal.set(Calendar.HOUR_OF_DAY, 0);
        yesterdayCal.set(Calendar.MINUTE, 0);
        yesterdayCal.set(Calendar.SECOND, 0);
        yesterdayCal.add(Calendar.DATE, -ago);

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "AppCache.db", null, 1);

        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(timeRange, yesterdayCal.getTimeInMillis(), todayCal.getTimeInMillis());
        Log.e("####", "results daily for " + yesterdayCal.getTime().toLocaleString() + " - " + todayCal.getTime().toLocaleString());

        for (UsageStats app : queryUsageStats) {
            if(dbHelper.getCategory(app.getPackageName()) == "") {
                GetCategoryTask task = new GetCategoryTask(app.getPackageName(), getApplicationContext());
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Log.e("OKAY", task.pName + "-" + task.aName + "-" + task.cName);
            }
            int sec = ((int)app.getTotalTimeInForeground() / 1000);
            int hour = sec / 3600;
            int min = (sec % 3600) / 60;
            sec %= 60;
            Log.e("DOKY", dbHelper.getCategory(app.getPackageName()) + " | "  + hour + "시간 " + min + "분 " + sec + "초");
        }
    }
    private void ClearUsage() {}
    private void AnalyzeUsage() {}

    private void AppendAnlysis() {}
    private void ClearAnlysis() {}

    private static void set_usage_table() {}
    private static void get_usage_table() {}
    private static void set_anlysis_table() {}
    private static void get_anlysis_table() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 1);
        // GET_USAGE_STATS 권한 확인
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) getApplication().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), getApplication().getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (getApplication().checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        Log.e("GRANT", "===== CheckPhoneState isRooting granted = " + granted);

        if (granted == false)
        {
            // 권한이 없을 경우 권한 요구 페이지 이동
            Intent intent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
            getApplication().startActivity(intent);
        }


        //dbHelper.deleteAll();
        for (int i = 0; i < 7; i++)
        AppendUsage(i, 0);
    }
}
