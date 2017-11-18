package com.nnew.cellmanager.userpattern;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nnew.cellmanager.R;

import java.util.List;

public class UserPattern extends AppCompatActivity {
    private static int usage_table;
    private static int anlysis_table;
    private static int category_table;

    private void AppendUsage() {}
    private void ClearUsage() {}
    private void AnalyzeUsage() {}

    private void AppendAnlysis() {}
    private void ClearAnlysis() {}

    private void CategorizeApp() {}

    private static void set_usage_table() {}
    private static void get_usage_table() {}
    private static void set_anlysis_table() {}
    private static void get_anlysis_table() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(Calendar.DAY_OF_MONTH, 17);
        beginCal.set(Calendar.MONTH, 11);
        beginCal.set(Calendar.YEAR, 2016);

        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.DAY_OF_MONTH, 18);
        endCal.set(Calendar.MONTH, 11);
        endCal.set(Calendar.YEAR, 2019);

        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
        Log.d("####", "results for " + beginCal.getTime().toGMTString() + " - " + endCal.getTime().toGMTString());

        for (UsageStats app : queryUsageStats) {
            GetCategoryTask task = new GetCategoryTask(app.getPackageName());
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            Log.e("OKAY", task.pName + "-" + task.aName + "-" + task.cName + " | " + (float) (app.getTotalTimeInForeground() / 1000));
        }
    }
}