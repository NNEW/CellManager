package com.nnew.cellmanager.userpattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nnew.cellmanager.R;
import com.github.mikephil.charting.charts.PieChart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.CENTER;
import static com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP;

/**
 * Created by Hyun on 2017-11-30.
 */

public class GraphView extends AppCompatActivity implements Serializable{

    private PieChart mChart;
    private HashMap<String, Integer> dataGraph_;

    protected void onCreate(Bundle savedInstanceState) {
    //public void makeGraph() {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pattern_visualization);

        dataGraph_ = (HashMap<String,Integer>) getIntent().getSerializableExtra("dataGraph");
        //dataList_ = (HashMap<Pair<String,String>,Integer>) getIntent().getSerializableExtra("dataList");

        Log.e("DOKY", "GraphView Start");

        mChart = (PieChart) findViewById(R.id.chart);
        mChart.setBackgroundColor(Color.WHITE);

        //moveOffScreen();

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
        //mChart.setEntryLabelColor(Color.WHITE);
        //mChart.setEntryLabelTextSize(10f);
    }

    private void setData(int count, int range) {
        ArrayList<PieEntry> values = new ArrayList<>();

        Iterator it = GraphView.sortByValue(dataGraph_).iterator();
        String temp;

        for(int i = 0; i< count; i++) {
            temp = (String) it.next();
            values.add(new PieEntry(dataGraph_.get(temp), temp));

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

        int offset = (int) (height * 0.6);

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
}
