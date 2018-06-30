package com.example.sj.keymeasures.firebase;

import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.BaseAdapter;

import com.example.sj.keymeasures.algopuzzles.model.OjGoal;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * https://github.com/firebase/AndroidChat/blob/master/app/src/main/java/com/firebase/androidchat/FirebaseListAdapter.java
 */
public abstract class FirebasetListAdapter<T> extends BaseAdapter {
    private Query mRef;
    private Class<T> mModelClass;
    private int mLayout;
    private LayoutInflater mInflater;
    private List<T> mModels;
    private List<String> mKeys;
    private ChildEventListener mListener;

    public FirebasetListAdapter(Query mRef, final Class<T> mModelClass, int mLayout, Activity activity ) {
        this.mRef= mRef;
        this.mModelClass= mModelClass;
        this.mLayout= mLayout;
        mInflater= activity.getLayoutInflater();
        mModels= new ArrayList<>();
        mKeys= new ArrayList<>();
        mListener= this.mRef.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot, String previousChildName ) {
                T model= dataSnapshot.getValue(FirebasetListAdapter.this.mModelClass);
                String key= dataSnapshot.getKey();
                Log.d("ADDED: ",((OjGoal)model).getMe());

                if ( previousChildName == null ) {
                    mModels.add(0,model);
                    mKeys.add(0,key);
                }
                else {
                    int previousIndex= mKeys.indexOf(previousChildName);
                    int nextIndex= previousIndex+1;
                    if ( nextIndex == mModels.size() ) {
                        mModels.add(model);
                        mKeys.add(key);
                    }
                    else {
                        mModels.add(nextIndex,model);
                        mKeys.add(nextIndex,key);
                    }
                }
                notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key= dataSnapshot.getKey();
                T newModel= dataSnapshot.getValue(FirebasetListAdapter.this.mModelClass);
                int index= mKeys.indexOf(key);
                mModels.set(index,newModel);
                notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key= dataSnapshot.getKey();
                int index= mKeys.indexOf(key);
                mKeys.remove(index);
                mModels.remove(index);
                notifyDataSetChanged();
            }
            @Override
            public void onChildMoved( DataSnapshot dataSnapshot, String previousChildName ) {
                String key= dataSnapshot.getKey();
                T newModel= dataSnapshot.getValue(FirebasetListAdapter.this.mModelClass);
                int index= mKeys.indexOf(key);
                mModels.remove(index);
                mKeys.remove(index);
                if ( previousChildName == null ) {
                    mModels.add(0,newModel );
                    mKeys.add(0,key);
                }
                else {
                    int previousIndex= mKeys.indexOf(previousChildName);
                    int nextIndex= previousIndex+1;
                    if ( nextIndex == mKeys.size() ) {
                        mModels.add(newModel);
                        mKeys.add(key);
                    }
                    else {
                        mModels.add(nextIndex,newModel);
                        mKeys.add(nextIndex,key);
                    }
                }
                notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if ( view == null ) {
            view= mInflater.inflate(mLayout,viewGroup,false);
        }
        T model = mModels.get(i);
        populateView(view,model);
        return view;
    }

    protected abstract void populateView(View v, T model);

}
