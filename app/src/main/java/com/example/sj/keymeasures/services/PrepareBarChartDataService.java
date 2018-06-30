package com.example.sj.keymeasures.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sj.keymeasures.algopuzzles.model.OjGoal;
import com.example.sj.keymeasures.receivers.BarChartDataPreparationBroadcastReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * the purpose of this class is to download
 * the data from the remote DB (Firebase in this case)
 * and parse the HTML of the websites therein,
 * to prepare certain data that can be used
 * in BarChart
 */
public class PrepareBarChartDataService extends Service {

    private ServiceHandler mServiceHandler;
    private Looper mLooper;

    private final class ServiceHandler extends Handler {
        public ServiceHandler( Looper looper ) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("oj_goals");
            ref.orderByChild("ojTag").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    OjGoal goal= dataSnapshot.getValue(OjGoal.class);
                    Log.d("inside onChildAdded",goal.getMe());
                    Intent intent= new Intent("com.example.sj.keymeasures.BAR_CHART_DATA_PREPARATION_FINISHED");
                    /**
                    * https://stackoverflow.com/questions/43800772/does-variable-order-matter-while-parcel-read-write-operation-in-parcelable
                    */
                    intent.putExtra("oj_goal",goal);
                    sendBroadcast(intent);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            //stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread= new HandlerThread("PrepareBarChart");
        thread.start();
        mLooper= thread.getLooper();
        mServiceHandler= new ServiceHandler(mLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg= mServiceHandler.obtainMessage();
        msg.arg1= startId;
        mServiceHandler.sendMessage(msg);
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
