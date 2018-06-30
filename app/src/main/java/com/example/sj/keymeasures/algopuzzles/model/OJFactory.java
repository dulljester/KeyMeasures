package com.example.sj.keymeasures.algopuzzles.model;

public class OJFactory {
    public static OnlineJudge buildOJ( String tag ) {
        String s= tag.toLowerCase();
        if ( s.indexOf("olimp") != -1 )
            return SguRuOlimpOnlineJudge.getInstance();
        if ( s.indexOf("uva") != -1 )
            return UvaOnlineJudge.getInstance();
        if ( s.indexOf("spoj") != -1 )
            return Spoj.getInstance();
        if ( s.indexOf("timus") != -1 )
            return TimusOnlineJudge.getInstance();
        if ( s.indexOf("sgu") != -1 )
            return SguRuOnlineJudge.getInstance();
        return null;
    }
}
