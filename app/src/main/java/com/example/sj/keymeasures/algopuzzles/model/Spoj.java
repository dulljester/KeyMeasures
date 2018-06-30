/*
 * One can benefit from reading this:
 * https://stackoverflow.com/questions/14568401/jsoup-getting-class-element-with-whitespace-in-name
 * When trying to access a class with whitespace in them, what we do? Read up!
 */
package com.example.sj.keymeasures.algopuzzles.model;

import com.example.sj.keymeasures.algopuzzles.helpers.EnvConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Spoj extends OnlineJudge {
    private static Spoj instance;

    public static Spoj getInstance() {
        if ( instance == null )
            instance= new Spoj("spoj","www.spoj.com","http://www.spoj.com/users/USERNAME_HERE/");
        return instance;
    }
    private Spoj(String ID, String url, String accessPattern) {
        super(ID, url, accessPattern);
    }

    @Override
    public int howManySolved(OjUser user) {
        String requestUrl= this.accessPattern.replace("USERNAME_HERE",user.getUserId());
        try {
            Document doc= Jsoup.connect(requestUrl).get();
            Elements elements= doc.select("dl.dl-horizontal.profile-info-data.profile-info-data-stats");
            if ( elements != null && elements.size() > 0 ) {
                //System.out.println(elements.toString());
                Element e = elements.select("dd").get(0);
                return Integer.parseInt(e.text());
            }
        } catch ( IOException ioe ) {
        }
        return 0;
    }

    @Override
    public int total() {
        return 0;
    }
}
