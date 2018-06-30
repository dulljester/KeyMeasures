package com.example.sj.keymeasures.firebase;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public abstract class FirebaseRecyclerViewAdapter<T,S extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Query mRef;
    private Class<T> mModelClass;
    protected int mLayout;
    private LayoutInflater mInflater;
    private List<T> mModels;
    private List<String> mKeys;
    private ChildEventListener mListener;

    protected T getItem( int pos ) {
        return mModels.get(pos);
    }

    public FirebaseRecyclerViewAdapter(Query mRef, final Class<T> mModelClass, int mLayout, Activity activity ) {
        this.mRef       = mRef;
        this.mModelClass= mModelClass;
        this.mLayout    = mLayout;
        mInflater= activity.getLayoutInflater();
        mModels= new ArrayList<>();
        mKeys= new ArrayList<>();
        mListener= this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot, String previousChildName ) {
                T model= dataSnapshot.getValue(FirebaseRecyclerViewAdapter.this.mModelClass);
                Log.d("INSIDE: ",dataSnapshot.getValue().toString());
                String key= dataSnapshot.getKey();
                if ( previousChildName == null ) {
                    mModels.add(0,model);
                    mKeys.add(0,key);
                }
                else {
                    int prevIndex= mKeys.indexOf(key);
                    int nextIndex= prevIndex+1;
                    if ( nextIndex == mKeys.size() ) {
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
                T model= dataSnapshot.getValue(FirebaseRecyclerViewAdapter.this.mModelClass);
                String key= dataSnapshot.getKey();
                int idx= mKeys.indexOf(key);
                mModels.set(idx,model);
                notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key= dataSnapshot.getKey();
                int idx= mKeys.indexOf(key);
                mKeys.remove(idx);
                mModels.remove(idx);
                notifyDataSetChanged();
            }
            @Override
            public void onChildMoved( DataSnapshot dataSnapshot, String previousChildName ) {
                String key= dataSnapshot.getKey();
                T newModel= dataSnapshot.getValue(FirebaseRecyclerViewAdapter.this.mModelClass);
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

    public int getItemCount() {
        return mKeys.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}

