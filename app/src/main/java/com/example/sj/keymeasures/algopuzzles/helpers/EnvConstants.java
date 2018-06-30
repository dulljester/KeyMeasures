package com.example.sj.keymeasures.algopuzzles.helpers;

import android.content.Context;
import android.content.res.Resources;

import com.example.sj.keymeasures.R;
import com.example.sj.keymeasures.algopuzzles.model.OJFactory;
import com.example.sj.keymeasures.algopuzzles.model.OnlineJudge;
import com.example.sj.keymeasures.algopuzzles.model.SguRuOlimpOnlineJudge;
import com.example.sj.keymeasures.algopuzzles.model.SguRuOnlineJudge;
import com.example.sj.keymeasures.algopuzzles.model.Spoj;
import com.example.sj.keymeasures.algopuzzles.model.TimusOnlineJudge;
import com.example.sj.keymeasures.algopuzzles.model.UvaOnlineJudge;

public class EnvConstants {
    public static final OnlineJudge []supportedOJs= {SguRuOlimpOnlineJudge.getInstance(),SguRuOnlineJudge.getInstance(),Spoj.getInstance(),UvaOnlineJudge.getInstance(),TimusOnlineJudge.getInstance()};
    private static final String []arr;

    static {
        arr = new String[]{"UVa", "Timus", "acm.sgu.ru", "olimp", "spoj"};
    }

    public static String findTag( String uniqueSubstring ) {
        for ( String x: arr )
            if ( x.indexOf(uniqueSubstring) != -1 )
                return new String(x);
        return null;
    }
    public static String findUrlByTag( String uniqueSubstring ) {
        return OJFactory.buildOJ(uniqueSubstring).getUrl();
    }
}
