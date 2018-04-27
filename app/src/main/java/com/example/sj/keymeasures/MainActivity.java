package com.example.sj.keymeasures;

import android.app.DialogFragment;
import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * MainActivity features three buttons:
 * --the central "+" button leads us directly to adding a new entity -- either a new Direction or a new WorkSession
 * --LogOut -- signs the user out of the session
 * --Overview -- draws a bar-plot of the hours scored per activity
 */
public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addButton;
    private AppDatabase db;
    private ImageButton overviewButton, buttonLogOut, buttonSaveToFirebase;
    private FirebaseAuth auth;
    private BroadcastRecevierDataUploadFinished receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db= AppDatabase.getInstance(this);
        auth = FirebaseAuth.getInstance();

        receiver= new BroadcastRecevierDataUploadFinished();

        buttonSaveToFirebase= findViewById(R.id.buttonSaveToFirebase);
        buttonSaveToFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),ServiceSaveToFirebase.class);
                startService(intent);
            }
        });
        buttonLogOut= findViewById(R.id.buttonLogOut);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signMeOut();
            }
        });
        overviewButton= findViewById(R.id.overviewButton);

        overviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,FragmentDrawGraph.class);
                startActivity(intent);
            }
        });

        addButton= (FloatingActionButton)findViewById( R.id.addButton );
        addButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent= new Intent(MainActivity.this,AddNewEntryFragment.class);
                startActivity(intent);
            }
        });
    }

    private void signMeOut() {
        auth.signOut();

        // this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        authListener.onAuthStateChanged(auth);
    }

    public static class AddNewEntryFragment extends FragmentActivity {
        private AppDatabase db= null;
        private ListView directionsListView;
        private FloatingActionButton addNewDirectionFAB;
        private Button submitButton;
        private EditText editTextAdditionalHours;
        private List<Converter01.Model01> theList;
        private FloatingActionButton refreshButton;
        private ImageView imageView;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onCreate( @Nullable Bundle savedInstanceState ) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.add_new_entry_fragment);


            refreshButton= findViewById(R.id.refreshButton);

            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getApplicationContext(),"refreshButton",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(getApplicationContext(),ServiceUpdateDB.class);
                    //Toast.makeText(getApplicationContext(),"intent prepared",Toast.LENGTH_SHORT).show();
                    getApplicationContext().startService(intent);
                }
            });
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("the list"));

            db= AppDatabase.getInstance(this);

            directionsListView= (ListView)findViewById(R.id.directionsListView);
            directionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final int rowNum= i;
                    final TextView activityNameTextView= view.findViewById(R.id.textViewDirectionName);
                    DialogueAddNewSession dlg= new DialogueAddNewSession();
                    dlg.setDialogResult(new DialogueAddNewSession.OnMyDialogResult() {
                        @Override
                        public void finish( final String result ) {
                            new AsyncTask<Void,Void,Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    WorkSession ws= new WorkSession();
                                    ws.setAid(db.keyDirectionsDao().getIdByName(activityNameTextView.getText().toString()));
                                    ws.setDuration(Integer.parseInt(result));
                                    ws.setWhen(LocalDateTime.now());
                                    db.workSessionDao().insertAll(ws);
                                    return null;
                                }
                            }.execute();
                        }
                    });
                    dlg.show(getFragmentManager(),"DialogueAddNewSession");
                }
            });

            refreshButton.callOnClick();

            imageView = (ImageView)findViewById(R.id.imageView);
            String img;
            int drawableID= getApplicationContext().getResources().getIdentifier(img=String.format("forapp%02d",ThreadLocalRandom.current().nextInt(0,8)),"drawable",getPackageName());
            Log.d("CRITICAL: ",img);
            imageView.setImageResource(drawableID);


            addNewDirectionFAB= (FloatingActionButton)findViewById(R.id.addNewDirectionFAB);

            addNewDirectionFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAddNewDirectionClick(view);
                }
            });
            /*
            new AsyncTask<Void,Void,List<Converter01.Model01>>() {
                @Override
                protected List<Converter01.Model01> doInBackground(Void ... voids ) {
                    return db.workSessionDao().summarize(LocalDateTime.ofEpochSecond(0,0,ZoneOffset.UTC));
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
                    Toast t = Toast.makeText(getApplicationContext(),String.format("%d",hoursFlown.size()),Toast.LENGTH_LONG);
                    t.show();
                    if ( hoursFlown.isEmpty() ) {
                        directionsListView.setEmptyView(null);
                    }
                    else {
                        ListAdapter adapter= new DirectionsListViewAdapter(getApplicationContext(),db,hoursFlown,names);
                        directionsListView.setAdapter(adapter);
                        //directionsListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                        Toast tast= Toast.makeText(getApplicationContext(),String.format("Rsetting adapter"),Toast.LENGTH_LONG);
                        tast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
                        tast.show();
                    }
                }
            }.execute();
            */
        }
        private BroadcastReceiver mMessageReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                theList= intent.getParcelableArrayListExtra("the list");
                List<Double> hoursFlown= new ArrayList<>();
                List<String> names= new ArrayList<>();
                for ( Converter01.Model01 m: theList ) {
                     hoursFlown.add(m.getTimeInvested()/60.00);
                     names.add(m.getActivityName());
                }
                ListAdapter adapter= new DirectionsListViewAdapter(getApplicationContext(),db,hoursFlown,names);
                //Toast.makeText(getApplicationContext(),"inside BroadcastReceiver "+hoursFlown.size(),Toast.LENGTH_LONG).show();
                directionsListView.setAdapter(adapter);
            }
        };
        @Override
        public void onResume() {
            super.onResume();
            //Toast.makeText(getApplicationContext(),"onResume",Toast.LENGTH_LONG).show();
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("the list"));
        }
        @Override
        protected void onPause() {
            //Toast.makeText(getApplicationContext(),"onPause",Toast.LENGTH_LONG).show();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            super.onPause();
        }
        private void onAddNewDirectionClick( View view ) {
            DialogFragment dlg= new DialogueAddNewDirection();
            dlg.show(getFragmentManager(),"DialogueAddNewDirection");
        }
    }
}

