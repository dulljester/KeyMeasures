package com.example.sj.keymeasures;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BroadcastRecevierDataUploadFinished extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BROADCAST","inside on receive");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(context,"Data uploaded to Firebase for user: "+user.getDisplayName(),Toast.LENGTH_LONG).show();
        Log.d("BROADCAST","Save finished");
    }
}
