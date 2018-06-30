package com.example.sj.keymeasures.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sj.keymeasures.algopuzzles.model.OJFactory;
import com.example.sj.keymeasures.algopuzzles.model.OjUser;
import com.example.sj.keymeasures.receivers.BarChartDataPreparationBroadcastReceiver;

public class OjParserService extends Service {
    private Looper mLooper;
    private ServiceHandler mHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler( Looper looper ) {
            super(looper);
        }

        @Override
        public void handleMessage( final Message msg ) {
            super.handleMessage(msg);
            Bundle bundle= msg.getData();
            final String username= bundle.getString("user");
            final String ojTag= bundle.getString("ojTag");
            //TODO: howManySolved( String ) should be implemented, too
            Log.d("[myServiceHandler]",username);
            new AsyncTask<Void,Void,Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    return OJFactory.buildOJ(ojTag).howManySolved(new OjUser(username.substring(1)));
                }

                @Override
                protected void onPostExecute(Integer aVoid) {
                    super.onPostExecute(aVoid);
                    Intent intent= new Intent( "com.example.sj.keymeasures.OJ_HAS_BEEN_PARSED");
                    intent.putExtra("ojTag",ojTag);
                    intent.putExtra("user",username);
                    intent.putExtra("solved",Integer.toString(aVoid));
                    Log.d("onPostExecute",username.substring(1)+" has solved "+aVoid);
                    sendBroadcast(intent);
                    stopSelf(msg.arg1);
                }
            }.execute();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("[myServiceHandler]","onCreate");
        HandlerThread thread= new HandlerThread("WebsiteParser");
        thread.start();
        mLooper= thread.getLooper();
        mHandler= new ServiceHandler(mLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg= mHandler.obtainMessage();
        msg.arg1= startId;
        Bundle bundle= new Bundle();
        Log.d("[myServiceHandler]","onStartCommand");
        bundle.putString("user",intent.getStringExtra("user"));
        bundle.putString("ojTag",intent.getStringExtra("ojTag"));
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

