package com.example.sj.keymeasures.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.sj.keymeasures.algopuzzles.model.OjGoal;
import com.example.sj.keymeasures.services.OjParserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BarChartDataPreparationBroadcastReceiver extends BroadcastReceiver {
    private ArrayList<OjGoal> goals;
    private Map<String,Map<String,Integer>> map= new HashMap<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        if ( intent.getAction().equals("com.example.sj.keymeasures.BAR_CHART_DATA_PREPARATION_FINISHED") ) {
            goals= intent.getParcelableArrayListExtra("oj_goals");
            /**
             * what needs to be done now is:
             * for each goal in goals, start an OjParserService
             */
            for ( OjGoal goal: goals ) {
                Intent xx= new Intent(context,OjParserService.class);
                xx.putExtra("user",goal.getMe());
                xx.putExtra("ojTag",goal.getOjTag());
                context.startService(xx);
                xx= new Intent(context,OjParserService.class);
                xx.putExtra("user",goal.getRival());
                xx.putExtra("ojTag",goal.getOjTag());
                context.startService(xx);
            }
            return ;
        }
        //assert intent.getAction().equals("com.example.sj.keymeasures.OJ_HAVE_BEEN_PARSED");
    }
}
