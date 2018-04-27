package com.example.sj.keymeasures;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Display;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * When the ListView containing the Directions together with the hours
 * scored in each of them appears, and we add new hours into it
 * (see the above DialogueAddNewSession file), we can click "Refresh"
 * on the left-hand bottom side. It refreshes the ListView to reflect
 * the latest change.
 * TODO: I should probably experiment with Observable interface
 * ServiceUpdateDB is actually called misleadingly: what
 * it does is to again collect the "Direction -- Hours" pairs
 * and sends it back via a Broadcast, using LocalBroadcastManager
 */
public class ServiceUpdateDB extends Service {
    private AppDatabase db;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private void sendMessage(List<Converter01.Model01>lst ) {
        //Toast.makeText(this, "about to send a message with "+lst.size()+" entries", Toast.LENGTH_LONG).show();
        Intent intent= new Intent("the list");
        intent.putParcelableArrayListExtra("the list",(ArrayList< Converter01.Model01>)lst);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        //Toast.makeText(this, "done with sendMessage", Toast.LENGTH_LONG).show();
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler( Looper looper ) {
            super(looper);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage( Message msg ) {
            super.handleMessage(msg);
            db= AppDatabase.getInstance(getApplicationContext());
            //List<Converter01.Model01> lst= db.workSessionDao().summarize((LocalDateTime)msg.obj);
            List<Converter01.Model01> lst= db.workSessionDao().summarize(LocalDateTime.ofEpochSecond(0,0, ZoneOffset.UTC));
            //Bundle bundle= new Bundle();
            //bundle.putParcelableArrayList("data",(ArrayList<Converter01.Model01>)lst);
            //msg.setData(bundle);
            ServiceUpdateDB.this.sendMessage(lst);
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "onCreate Method", Toast.LENGTH_LONG).show();
        HandlerThread thread= new HandlerThread("ServiceStartArguments");
        thread.start();
        mServiceLooper= thread.getLooper();
        mServiceHandler= new ServiceHandler(mServiceLooper);
        //Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        //Toast.makeText(this, "service starting", Toast.LENGTH_LONG).show();
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }
    @Override
    public void onDestroy() {
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}

