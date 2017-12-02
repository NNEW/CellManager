package com.nnew.cellmanager.userpattern;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nnew.cellmanager.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyun on 2017-11-30.
 */

public class ListVIew extends AppCompatActivity implements Serializable{

    private HashMap<Pair<String, String>, Integer> dataList_;

    ArrayList<String> tmpP;
    ArrayList<String> tmpC;
    ArrayList<Integer> tmpT;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pattern_visualization);

        dataList_ = new HashMap<>();

        tmpP = (ArrayList<String>) getIntent().getStringArrayListExtra("tmpP");
        tmpC = (ArrayList<String>) getIntent().getStringArrayListExtra("tmpC");
        tmpT = (ArrayList<Integer>) getIntent().getSerializableExtra("tmpT");

        Map tmp = new HashMap<Pair<String, String>, Integer>();
        Log.e("DOKY", tmpP.size() + " " + tmpC.size() + " " + tmpT.size());
        for(int i = 0; i < tmpP.size(); i++) {
            dataList_.put(Pair.create(tmpP.get(i), tmpC.get(i)), tmpT.get(i));
            Log.e("DOKY", tmpP.get(i) + " | " + tmpC.get(i) + " | " + tmpT.get(i) + " i : " + i);
        }

        Log.e("DOKY", "ListView Start");

        ListView listView = (ListView) findViewById(R.id.listview);

        ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String,String>>();

        Iterator it = ListVIew.sortByValueList(dataList_).iterator();

        HashMap<String, String> tmp_map = new HashMap<String, String>();
        Pair<String, String> tmp_key;
        Integer tmp_value;

        while(it.hasNext()) {
            tmp_key = (Pair<String, String>)it.next();
            tmp_value = dataList_.get(tmp_key);

            int sec = ((int)tmp_value / 1000);
            int hour = sec / 3600;
            int min = (sec % 3600) / 60;
            sec %= 60;

            tmp_map.put("item1",tmp_key.first);
            tmp_map.put("item2",tmp_key.second + "    " + hour + "시간 " + min + "분 " + sec + "초");
            Log.e("DOKY", tmp_key.first + " | " + tmp_key.second + "  " + hour + "시간 " + min + "분 " + sec + "초");
            mList.add(tmp_map);

            tmp_map.clear();
        }

        SimpleAdapter adapter = new SimpleAdapter(this, mList, android.R.layout.simple_list_item_2, new String[] {"item1", "item2"}, new int[] {android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    public static List sortByValueList(final Map map) {
        List<Pair<String,String>> list = new ArrayList<>();
        list.addAll(map.keySet());

        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);

                return ((Comparable) v1).compareTo(v2);
            }
        });

        Collections.reverse(list);
        return list;
    }
}
