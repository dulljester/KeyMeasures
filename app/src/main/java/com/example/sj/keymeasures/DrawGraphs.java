package com.example.sj.keymeasures;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sj.keymeasures.algopuzzles.model.OjGoal;
import com.example.sj.keymeasures.services.OjParserService;
import com.example.sj.keymeasures.services.PrepareBarChartDataService;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This fragment activity, when called, collects all the total
 * minutes scored per activity -- i.e. the same information
 * as contained in the ListView -- and draws a bar chart.
 * EDIT: this is a decoupled version of FragmentDrawGraph: now
 * it tries to prepare two fragments -- one holding an animation,
 * another holding the graph being populated. Once the graph is done,
 * it is going to replace one fragment with the other
 */
public class DrawGraphs extends FragmentActivity {
    private PieChart pieChart;
    private AppDatabase db;
    private PlaceholderFragment placeholderFragment;
    private BarChartFragment barChartFragment;
    private Handler handler= null;
    private Runnable runnable;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alternative_layout_for_draw_graph);

        if ( savedInstanceState != null )
            return ;

        placeholderFragment= new PlaceholderFragment();
        barChartFragment= new BarChartFragment();

        /**
         * https://stackoverflow.com/questions/34535752/way-to-automatically-change-fragments-after-delay
         */
        getSupportFragmentManager().beginTransaction().add(R.id.containerFrameLayout,barChartFragment).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.containerFrameLayout,placeholderFragment,"PLACEHOLDER").commit();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //Second fragment after 5 seconds appears
                /*
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerFrameLayout,barChartFragment)
                        .commit();
                      */
                /*Fragment fr= getSupportFragmentManager().findFragmentByTag("PLACEHOLDER");
                if ( fr != null )
                    getSupportFragmentManager().beginTransaction().remove(fr).commit();*/
            }
        };
        //handler.postDelayed(runnable, 30000);

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
                List<PieEntry> dp= new ArrayList<>();
                if ( hoursFlown.isEmpty() ) {
                }
                else {
                    for ( int i= 0; i < hoursFlown.size(); ++i ) {
                        dp.add(new PieEntry((float)(double)hoursFlown.get(i),i));
                    }
                    pieChart= findViewById(R.id.pieChart);
                    pieChart.setUsePercentValues(true);
                    pieChart.setRotationAngle(0f);
                    pieChart.setRotationEnabled(true);
                    pieChart.setDescription(null);
                    PieDataSet dataSet= new PieDataSet(dp,"Time share");
                    dataSet.setSliceSpace(3);
                    dataSet.setSelectionShift(5);

                    ArrayList<Integer> colors = new ArrayList<>();

                       /*
                       for (int c : ColorTemplate.VORDIPLOM_COLORS)
                           colors.add(c);

                       for (int c : ColorTemplate.JOYFUL_COLORS)
                           colors.add(c);

                       for (int c : ColorTemplate.COLORFUL_COLORS)
                           colors.add(c);

                       for (int c : ColorTemplate.LIBERTY_COLORS)
                           colors.add(c);

                       for (int c : ColorTemplate.PASTEL_COLORS)
                           colors.add(c);

                       colors.add(ColorTemplate.getHoloBlue());
                       */

                    colors.add(getResources().getColor(R.color.thunderCloud));
                    colors.add(getResources().getColor(R.color.meadow));
                    colors.add(getResources().getColor(R.color.stone));
                    colors.add(getResources().getColor(R.color.autumnFoliage));
                    colors.add(getResources().getColor(R.color.deepAqua));
                    colors.add(getResources().getColor(R.color.ocean));
                    colors.add(getResources().getColor(R.color.cherry));
                    colors.add(getResources().getColor(R.color.blueBerry));
                    Collections.shuffle(colors);

                    dataSet.setColors(colors);

                    PieData data= new PieData(dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextSize(11f);
                    data.setValueTextColor(Color.GRAY);
                    pieChart.setData(data);
                    pieChart.highlightValues(null);
                    pieChart.spin( 500,0,-360f, Easing.EasingOption.EaseInOutQuad);
                    Legend legend= pieChart.getLegend();
                    List<LegendEntry> entries= new ArrayList<>();
                    int i= 0;
                    for ( String x: names ) {
                        LegendEntry entry= new LegendEntry();
                        entry.label= x;
                        entry.formColor= data.getColors()[i++];
                        entries.add(entry);
                    }
                    legend.setCustom(entries);
                    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                    legend.setDrawInside(true);
                    legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    legend.setXEntrySpace(1f);
                    legend.setTextColor(getResources().getColor(R.color.matteBlack));
                    pieChart.invalidate();
                }
            }
        }.execute();


    }

   @Override
    protected void onResume() {
        super.onResume();
        //this.registerReceiver(this.receiver,intentFilter2);
        //this.registerReceiver(this.serviceReceiver,intentFilter);
        //drawBarChart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //drawBarChart();
    }

    @Override
    protected void onDestroy() {
        if ( handler != null )
            handler.removeCallbacks(runnable);
        super.onDestroy();
        //this.unregisterReceiver(this.serviceReceiver);
        //this.unregisterReceiver(this.receiver);
    }

    public static class BarChartFragment extends Fragment {
        private BarChart ojProgressBarChart;
        private IntentFilter intentFilter, intentFilter2;
        private ConcurrentMap<String,ConcurrentMap<String,Integer>> map;
        // https://stackoverflow.com/questions/4823133/send-data-from-service-back-to-my-activity
        private BroadcastReceiver serviceReceiver, receiver;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            map= new ConcurrentHashMap<>();

            Intent intent = new Intent(getContext().getApplicationContext(), PrepareBarChartDataService.class);
            getActivity().startService(intent);

            intentFilter2= new IntentFilter("com.example.sj.keymeasures.OJ_HAS_BEEN_PARSED");
            receiver= new BroadcastReceiver() {
                @Override
                public void onReceive (Context context, Intent intent ){
                    String user = intent.getStringExtra("user");
                    String ojTag = intent.getStringExtra("ojTag");
                    String solved = intent.getStringExtra("solved");
                    Log.d("INSIDE SECOND RECEIVER", user.substring(1) + " solved " + solved + " at " + ojTag);
                    if (map.containsKey(ojTag))
                        map.get(ojTag).put(user, Integer.parseInt(solved));
                    else {
                        map.put(ojTag, new ConcurrentHashMap<String, Integer>());
                        map.get(ojTag).put(user, Integer.parseInt(solved));
                    }
                    drawBarChart();
                }
            };
            intentFilter= new IntentFilter("com.example.sj.keymeasures.BAR_CHART_DATA_PREPARATION_FINISHED");
            serviceReceiver= new BroadcastReceiver() {
                @Override
                public void onReceive ( final Context context, final Intent intent ){
                    OjGoal goal = intent.getParcelableExtra("oj_goal");
                    Log.d("RECEIVED", goal.getMe());
                    if (!map.containsKey(goal.getOjTag()))
                        map.put(goal.getOjTag(), new ConcurrentHashMap<String, Integer>());
                    map.get(goal.getOjTag()).put("2BIG_GOAL", goal.getGoalNumber());
                    Intent intent1 = new Intent(context, OjParserService.class);
                    intent1.putExtra("user", "0" + goal.getMe());
                    intent1.putExtra("ojTag", goal.getOjTag());
                    context.startService(intent1);
                    intent1 = new Intent(context, OjParserService.class);
                    intent1.putExtra("user", "1" + goal.getRival());
                    intent1.putExtra("ojTag", goal.getOjTag());
                    context.startService(intent1);
                }
            };
        }

        @Override
        public void onResume() {
            super.onResume();
            this.getActivity().registerReceiver(this.receiver,intentFilter2);
            this.getActivity().registerReceiver(this.serviceReceiver,intentFilter);
            //drawBarChart();
        }

        @Override
        public void onStart() {
            super.onStart();
            //drawBarChart();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            this.getActivity().unregisterReceiver(this.serviceReceiver);
            this.getActivity().unregisterReceiver(this.receiver);
        }

        private void drawBarChart() {
            Log.d("drawBarChart()", "drawBarChart: "+map.size());
            List<BarEntry> entriesGroup0= new ArrayList<>();
            List<BarEntry> entriesGroup1= new ArrayList<>();
            int i,j,k= 0, groupCount= map.size();
            List<String> xVals= new ArrayList<>();

            for ( Map.Entry<String,ConcurrentMap<String,Integer>> entry: map.entrySet() ) {
                double me=0, other=1, biggoal=1;
                for ( Map.Entry<String,Integer> item: entry.getValue().entrySet() ) {
                    if ( item.getKey().charAt(0) == '0' )
                        me= item.getValue();
                    else if ( item.getKey().charAt(0) == '1' )
                        other= item.getValue();
                    else biggoal= item.getValue();
                }
                xVals.add(entry.getKey());
                entriesGroup0.add(new BarEntry((float)k,100*(float)(me/other)));
                entriesGroup1.add(new BarEntry((float)k,100*(float)(me/biggoal)));
                Log.d("BAR_CHART",me+" "+other+" "+biggoal);
                ++k;
            }

            int[] colors = new int[] {Color.GREEN, Color.YELLOW, Color.GREEN, Color.BLUE, Color.GRAY, Color.BLACK};

            XAxis xAxis= ojProgressBarChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setCenterAxisLabels(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisMaximum(groupCount);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

            BarDataSet set0= new BarDataSet(entriesGroup0,"Relative to friend");
            BarDataSet set1= new BarDataSet(entriesGroup1,"In absolute terms");
            set0.setColor(getResources().getColor(R.color.indigoInk));
            set1.setColor(getResources().getColor(R.color.brickRed));

            float groupSpace = 0.4f;
            float barSpace = 0f; // x2 dataset
            float barWidth = 0.3f; // x2 dataset
            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

            BarData data = new BarData(set0, set1);
            data.setBarWidth(barWidth); // set the width of each bar
            ojProgressBarChart.setData(data);
            ojProgressBarChart.getXAxis().setAxisMinimum(0);
            ojProgressBarChart.getXAxis().setAxisMaximum(0 + ojProgressBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
            ojProgressBarChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
            ojProgressBarChart.invalidate(); // refresh
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view= inflater.inflate(R.layout.layout_barchart,container,false);
            ojProgressBarChart= view.findViewById(R.id.bchart);
            ojProgressBarChart.setDescription(null);
            ojProgressBarChart.setPinchZoom(false);
            ojProgressBarChart.setScaleEnabled(false);
            ojProgressBarChart.setDrawBarShadow(false);
            ojProgressBarChart.setDrawGridBackground(false);
            //drawBarChart();
            return view;
        }
    }

    /**
     * this fragment is for holding the animation while the graph is being loaded
     * https://stackoverflow.com/questions/10876468/android-frame-by-frame-animation-on-fragment
     */
    public static class PlaceholderFragment extends Fragment {
        private Drawable drawable;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                drawable= getContext().getDrawable(R.drawable.avd_circular_indeterminate_progress_bar);
                if ( drawable instanceof AnimatedVectorDrawable) {
                    Log.d("DrawGraphs: ","inside animate");
                    ((AnimatedVectorDrawable)drawable).start();
                }
            }
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view= inflater.inflate(R.layout.layout_placeholder_fragment,container,false);
            view.setBackground(drawable);
            return view;
        }
    }
}
