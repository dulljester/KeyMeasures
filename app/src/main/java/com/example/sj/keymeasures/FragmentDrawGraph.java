package com.example.sj.keymeasures;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * This fragment activity, when called, collects all the total
 * minutes scored per activity -- i.e. the same information
 * as contained in the ListView -- and draws a bar chart.
 * I used "GraphView" library, but haven't been able to beautify
 * the graphs too much. For one, they miss the legends.
 * Also, GraphView seems to miss pie-charts.
 */
public class FragmentDrawGraph extends FragmentActivity {
    private GraphView barChart;
    private AppDatabase db;
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view_bar_chart);
        db= AppDatabase.getInstance(this);
        new AsyncTask<Void,Void,List<Converter01.Model01>>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                protected List<Converter01.Model01> doInBackground( Void ... voids ) {
                    return db.workSessionDao().summarize(LocalDateTime.ofEpochSecond(0,0, ZoneOffset.UTC));
                }
                @Override
                protected void onPostExecute(List<Converter01.Model01> list) {
                    super.onPostExecute(list);
                    List<Double> hoursFlown= new ArrayList<>();
                    List<String> names= new ArrayList<>();
                    for (Converter01.Model01 m: list) {
                        hoursFlown.add(m.getTimeInvested()/60.00);
                        names.add(m.getActivityName());
                    }
                    DataPoint []dp= new DataPoint[hoursFlown.size()];
                    if ( hoursFlown.isEmpty() ) {
                    }
                    else {
                       for ( int i= 0; i < hoursFlown.size(); ++i ) {
                           dp[i] = new DataPoint(i, hoursFlown.get(i));
                       }
                       BarGraphSeries<DataPoint> series= new BarGraphSeries<>(dp);
                       series.setDrawValuesOnTop(true);
                       barChart= (GraphView) findViewById(R.id.barChart);
                       series.setSpacing(25);
                       barChart.addSeries(series);
                       series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                           @Override
                           public int get(DataPoint data) {
                               return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                           }
                       });
                       series.setValuesOnTopColor(Color.RED);
                    }
                }
            }.execute();
    }
}

