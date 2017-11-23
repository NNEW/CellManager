package com.nnew.cellmanager.userpattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by pch on 17. 11. 18.
 */

public class GetCategoryTask extends AsyncTask<Void, Void, Document> {
    String pName;
    String cName;
    String aName;
    Context appContext;

    GetCategoryTask(String name, Context app) {
        pName = name;
        appContext = app;
    }

    private boolean getdefaultApp() {
        final String[] apps = {"com.android", "com.sec", "com.lge", "com.kt", "com.skt", "com.lgt"};

        for (int i = 0; i < apps.length; i++) {
            if (pName.contains(apps[i])) return true;
        }
        return false;
    }

    @Override
    protected Document doInBackground(Void... nothing) {
        Document doc = null;
        Log.e("AA", pName);
        try {
            doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + pName).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    @Override
    protected void onPostExecute(Document doc) {
        // do something with doc
        if (doc == null) {
            Log.e("BBB", "NULL");
            cName = (String) (getdefaultApp() ? "기본 어플" : "기타");
        } else {
            String a = doc.select(".document-subtitle category").text();
            cName = doc.select("[itemprop=genre]").text();
            //aName = doc.select("[class=id-app-title]").text();
        }
        final PackageManager pm = appContext.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(pName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        aName = (String) (ai != null ? pm.getApplicationLabel(ai) : "어플");

        final DBHelper dbHelper = new DBHelper(appContext, "AppCache.db", null, 1);
        dbHelper.insert(pName, aName, cName);
        Log.e("AAA-C", cName);
        Log.e("AAA-A", aName);
    }
}
