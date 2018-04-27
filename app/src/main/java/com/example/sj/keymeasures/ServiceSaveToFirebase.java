package com.example.sj.keymeasures;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceSaveToFirebase extends Service {
    private ServiceHandler mServiceHandler;
    private Looper mServiceLooper;
    private FirebaseDatabase database;
    private AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        database= FirebaseDatabase.getInstance();
        db= AppDatabase.getInstance(this);
        HandlerThread thread= new HandlerThread("ServiceStartArgs");
        thread.start();

        mServiceLooper= thread.getLooper();
        mServiceHandler= new ServiceHandler(mServiceLooper);
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler( Looper looper ) {
            super(looper);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage( Message msg ) {
            super.handleMessage(msg);
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference ref= database.getReference();
            DatabaseReference usersRef= ref.child("users/"+user.getDisplayName());
            List<Converter01.Model01> lst= db.workSessionDao().summarize(LocalDateTime.ofEpochSecond(0,0, ZoneOffset.UTC));
            Map<String,Double> workSessions= new HashMap<>();
            for ( Converter01.Model01 entry: lst )
                workSessions.put(entry.getActivityName(),entry.getTimeInvested());
            usersRef.setValue(workSessions);

            /**
             *
             */
            Intent intent= new Intent(getApplicationContext(),BroadcastRecevierDataUploadFinished.class);
            intent.setAction("com.example.sj.keymeasures.SAVE_TO_FIREBASE_FINISHED");
            Log.d("SERVICE","sendBroadcast()");
            sendBroadcast(intent);

            stopSelf(msg.arg1);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg= mServiceHandler.obtainMessage();
        msg.arg1= startId;
        mServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
