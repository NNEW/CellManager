package com.nnew.cellmanager.userpattern;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pch on 17. 11. 18.
 */


public class GetCategoryTask extends AsyncTask<Void, Void, Document> {
    String pName;
    String cName;
    String aName;

    GetCategoryTask(String name) {
        pName = name;
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
        String a = doc.select(".document-subtitle category").text();
        cName = doc.select("[itemprop=genre]").text();
        aName = doc.select("[class=id-app-title]").text();

        Log.e("AAA", cName);
        Log.e("AAA", aName);
    }
}
