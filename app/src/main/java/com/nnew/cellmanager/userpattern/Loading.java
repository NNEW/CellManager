package com.nnew.cellmanager.userpattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nnew.cellmanager.R;
import com.nnew.cellmanager.userpattern.DBHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

public class Loading extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final ImageView img_loading_frame = (ImageView) findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        CategorizeApps();
    }

    private void CategorizeApps() {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "AppCache.db", null, 1);
        int num = 0;
        Log.e("AA", "A");
        GetCategoryTask task = new GetCategoryTask(apps);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public class GetCategoryTask extends AsyncTask<Void, String, Void> {
        List<ApplicationInfo> apps;
        ProgressBar progress_bar;
        TextView progress_text;

        GetCategoryTask(List<ApplicationInfo> apps) {
            this.apps = apps;
        }

        private boolean getdefaultApp(String pName) {
            final String[] apps = {"com.android", "com.sec", "com.lge", "com.kt", "com.skt", "com.lgt"};

            for (int i = 0; i < apps.length; i++) {
                if (pName.contains(apps[i])) return true;
            }
            return false;
        }


        @Override
        protected void onPreExecute() {
            progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
            progress_text = (TextView) findViewById(R.id.progress_num);
        }

        @Override
        protected Void doInBackground(Void... nothing) {
            final DBHelper dbHelper = new DBHelper(getApplicationContext(), "AppCache.db", null, 1);
            int progress_num = 0;
            int apps_num = apps.size();

            progress_bar.setMax(apps_num);
            progress_text.setText("(" + progress_num + " / " + apps_num + ")");

            for (ApplicationInfo app : apps) {
                String pName = app.packageName;
                String cName = "";
                String aName = "";
                Document doc = null;
                progress_bar.incrementProgressBy(1);
                progress_num++;
                publishProgress("(" + progress_num + " / " + apps_num + ")");
                if(dbHelper.getCategory(app.packageName) != "") continue;
                try {
                    doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + pName).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (doc == null) {
                    cName = (String) (getdefaultApp(pName) ? "기본 어플" : "기타");
                } else {
                    String a = doc.select(".document-subtitle category").text();
                    cName = doc.select("[itemprop=genre]").text();
                }
                final PackageManager pm = getApplicationContext().getPackageManager();
                ApplicationInfo ai;
                try {
                    ai = pm.getApplicationInfo(pName, 0);
                } catch (final PackageManager.NameNotFoundException e) {
                    ai = null;
                }
                aName = (String) (ai != null ? pm.getApplicationLabel(ai) : "어플");

                dbHelper.insert(pName, aName, cName);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... str) {
            progress_text.setText(str[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(getApplicationContext(), UserPattern.class);
            startActivity(intent);
            finish();
        }
    }
}