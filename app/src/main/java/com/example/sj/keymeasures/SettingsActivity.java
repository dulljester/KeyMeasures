package com.example.sj.keymeasures;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sj.keymeasures.adapters.OjGoalsRecyclerViewAdapter;
import com.example.sj.keymeasures.algopuzzles.helpers.Pair;
import com.example.sj.keymeasures.algopuzzles.model.OJFactory;
import com.example.sj.keymeasures.algopuzzles.model.OjGoal;
import com.example.sj.keymeasures.algopuzzles.model.OjGoalsListAdapter;
import com.example.sj.keymeasures.algopuzzles.model.OjUser;
import com.example.sj.keymeasures.algopuzzles.model.OnlineJudge;
import com.example.sj.keymeasures.algopuzzles.model.SguRuOlimpOnlineJudge;
import com.example.sj.keymeasures.algopuzzles.model.SguRuOnlineJudge;
import com.example.sj.keymeasures.algopuzzles.model.Spoj;
import com.example.sj.keymeasures.algopuzzles.model.TimusOnlineJudge;
import com.example.sj.keymeasures.algopuzzles.model.UvaOnlineJudge;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SettingsActivity extends AppCompatActivity {

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType= LayoutManagerType.LINEAR_LAYOUT_MANAGER;

    //private RecyclerView userInfoRecyclerView;
    private ListView userInfoListView;
    private ValueEventListener mConnectedListener;
    //private FloatingActionButton commitChangesFab;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private OjGoalsListAdapter mAdapter;
    //private OjGoalsRecyclerViewAdapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //ojUserId= findViewById(R.id.ojUserId);
        //goalNumbers= findViewById(R.id.goalNumbers);
        //rivalId= findViewById(R.id.rivalID);
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        //userInfoRecyclerView= findViewById(R.id.userInfoRecyclerView);
        userInfoListView= findViewById(R.id.userInfoListView);
        mAdapter= new OjGoalsListAdapter(databaseReference.child("users").child(currentUser.getUid()).child("oj_goals").orderByChild("ojTag"),this,R.layout.recycler_view_item);
        userInfoListView.setAdapter(mAdapter);
        /*mAdapter= new OjGoalsRecyclerViewAdapter(databaseReference.child("users").child(currentUser.getUid()).child("oj_goals").orderByChild("ojTag"),R.layout.cardview_online_judge,this);
        userInfoRecyclerView.setAdapter(mAdapter);
        userInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this))//;
        */

        /*
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                userInfoListView.setSelection(mAdapter.getCount()-1);
            }
        });
        */
        userInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"Clicked on "+((OjGoal)(adapterView.getItemAtPosition(i))).getOjTag(),Toast.LENGTH_SHORT).show();
                OjGoal goal= (OjGoal) adapterView.getItemAtPosition(i);
                Intent intent= new Intent(SettingsActivity.this,EditOnlineJudgeProfileActivity.class);
                Toast.makeText(getApplicationContext(),"Clicked on "+goal.getOjTag(),Toast.LENGTH_SHORT).show();
                intent.putExtra("ojTag",goal.getOjTag());
                startActivity(intent);
            }
        });

        /*
        commitChangesFab= findViewById(R.id.commitChangesFab);
        commitChangesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for ( int i= 0; i < 5; ++i ) {
                    View v= userInfoListView.getChildAt(i);
                    TextView ojUserIdTextView= v.findViewById(R.id.ojUserId);
                    TextView rivalIdTextView= v.findViewById(R.id.rivalID);
                    TextView goalNumbers= v.findViewById(R.id.goalNumbers);
                    TextView tagTextView= v.findViewById(R.id.tagTextView);
                    String ojUserId= ojUserIdTextView.getText().toString();
                    String rivalId= rivalIdTextView.getText().toString();
                    String num= goalNumbers.getText().toString();
                    if ( num.equals("") || ojUserId.equals("") || rivalId.equals("") ) {
                        Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        OjUser me= new OjUser(ojUserId), rival= new OjUser(rivalId);
                        Integer goalNumber= Integer.parseInt(num);
                        String ojString= tagTextView.getText().toString();
                        OjGoal ojGoal= new OjGoal(me.getUserId(),rival.getUserId(),ojString,goalNumber);
                        databaseReference.child("users").child(currentUser.getUid()).child("oj_goals").child(ojString).setValue(ojGoal);
                    }
                }
            }
        });
        */
    }
}

