package com.nnew.cellmanager.userpattern;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nnew.cellmanager.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserPatternVisualization extends AppCompatActivity implements Serializable{

    private PieChart mChart;
    private ListView listView;

    private ArrayList<String> rawData;
    private HashMap<String,Integer> processedDataGraph;
    private HashMap<Pair<String, String>, Integer> processedDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pattern_visualization);

        Log.e("DOKY", "UserPatternVisualization Start");

        rawData = (ArrayList<String>) getIntent().getStringArrayListExtra("rawData");

        mChart = (PieChart) findViewById(R.id.chart);
        listView = (ListView) findViewById(R.id.listview);

        processedDataGraph = new HashMap<>();
        processedDataList = new HashMap<>();

        processData(rawData);
        VisualizeUserPattern();
    }

    private void processData(ArrayList<String> raw) {
        String packageKey;
        String categoryKey;
        Integer value;
        String[] tokens;

        for(String tmp : rawData) {
            tokens = tmp.split("//");
            packageKey = tokens[0];
            categoryKey = tokens[1];
            value = Integer.parseInt(tokens[2]);

            if(processedDataGraph.containsKey(categoryKey)) {
                processedDataGraph.put(categoryKey, processedDataGraph.get(categoryKey) + value);
            } else {
                processedDataGraph.put(categoryKey, value);
            }

            processedDataList.put(Pair.create(packageKey, categoryKey), value);
        }
    }

    private void VisualizeUserPattern() {

        this.makeGraph();
        this.makeList();
        /*
        Intent intentG = new Intent(getApplicationContext(), GraphView.class);
        intentG.putExtra("dataGraph", processedDataGraph);
        startActivity(intentG);


        Intent intentL = new Intent(getApplicationContext(), ListVIew.class);
        intentL.putStringArrayListExtra("tmpP", tmpP);
        intentL.putStringArrayListExtra("tmpC", tmpC);
        intentL.putExtra("tmpT", tmpT);
        startActivity(intentL);
        */
    }

    private void makeList() {
        ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String,String>>();

        Iterator it = ListVIew.sortByValueList(processedDataList).iterator();

        Pair<String, String> tmp_key;
        Integer tmp_value;

        while(it.hasNext()) {
            HashMap<String, String> tmp_map = new HashMap<String, String>();
            tmp_key = (Pair<String, String>)it.next();
            tmp_value = processedDataList.get(tmp_key);

            int sec = ((int)tmp_value / 1000);
            int hour = sec / 3600;
            int min = (sec % 3600) / 60;
            sec %= 60;

            tmp_map.put("item1",tmp_key.first);
            tmp_map.put("item2",tmp_key.second + "    " + hour + "시간 " + min + "분 " + sec + "초");
            Log.e("DOKY", tmp_key.first + " | " + tmp_key.second + "  " + hour + "시간 " + min + "분 " + sec + "초");
            mList.add(tmp_map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, mList, android.R.layout.simple_list_item_2, new String[] {"item1", "item2"}, new int[] {android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    private void makeGraph() {
        mChart.setBackgroundColor(Color.WHITE);

        moveOffScreen();

        //mChart.setHoleRadius(20f);
        mChart.setTransparentCircleRadius(10f);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawHoleEnabled(true);
        mChart.setMaxAngle(180);
        mChart.setRotationAngle(180);
        mChart.setCenterTextOffset(+10, +20);

        setData(4,100);

        mChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        Legend l = mChart.getLegend();
        l.setFormSize(7f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(5f);
        l.setTextSize(12f);

        mChart.setDrawEntryLabels(false);
    }

    private void setData(int count, int range) {
        ArrayList<PieEntry> values = new ArrayList<>();

        Iterator it = GraphView.sortByValue(processedDataGraph).iterator();
        String temp;

        for(int i = 0; i< count; i++) {
            temp = (String) it.next();
            values.add(new PieEntry(processedDataGraph.get(temp), temp));

            //float val = (float) (Math.random()*range + range/5);
            //values.add(new PieEntry(val, countries[i]));
        }

        PieDataSet dataset = new PieDataSet(values, "");
        dataset.setSelectionShift(5f);
        dataset.setSliceSpace(3f);
        dataset.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataset);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.invalidate();
    }

    private void moveOffScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;

        int offset = (int) (height * 0.13);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mChart.getLayoutParams();
        params.setMargins(0,0,0,-offset);
        mChart.setLayoutParams(params);
    }

    public static List sortByValue(final Map map) {
        List<String> list = new ArrayList<>();
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
