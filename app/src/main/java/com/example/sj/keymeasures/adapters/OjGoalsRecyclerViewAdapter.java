package com.example.sj.keymeasures.adapters;

import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sj.keymeasures.R;
import com.example.sj.keymeasures.SettingsActivity;
import com.example.sj.keymeasures.algopuzzles.helpers.EnvConstants;
import com.example.sj.keymeasures.algopuzzles.model.OjGoal;
import com.example.sj.keymeasures.firebase.FirebaseRecyclerViewAdapter;
import com.google.firebase.database.Query;

import java.util.List;

public class OjGoalsRecyclerViewAdapter extends FirebaseRecyclerViewAdapter<OjGoal,OjGoalsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG= "CustomAdapter";

    public OjGoalsRecyclerViewAdapter( Query mRef, int mLayout, Activity activity ) {
        super(mRef,OjGoal.class,mLayout,activity);
    }

    // BEGIN_INCLUDE(recyclerViewCustomViewHolder)
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ojTagTextView, ojLinkTextView;
        public EditText userId, friendId, goalNumber;
        public ViewHolder( View rowView ) {
            super(rowView);
            this.ojTagTextView  = rowView.findViewById(R.id.tagTextView);
            this.ojLinkTextView = rowView.findViewById(R.id.ojLinkTextView);
            this.userId         = rowView.findViewById(R.id.ojUserId);
            this.friendId       = rowView.findViewById(R.id.rivalID);
            this.goalNumber     = rowView.findViewById(R.id.goalNumbers);
            Log.d("HERE","added URL");

            userId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
        public TextView getOjTagTextView() {
            return ojTagTextView;
        }
        public TextView getOjLinkTextView() {
            return ojLinkTextView;
        }
        public EditText getUserId() {
            return userId;
        }
        public EditText getFriendId() {
            return friendId;
        }
        public EditText getGoalNumber() {
            return goalNumber;
        }
    }
    // END_INCLUDE(recyclerViewCustomViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    /**
     *
     * @param parent
     * @param viewType needed to select different layouts depending on the type
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View v= LayoutInflater.from(parent.getContext()).inflate(mLayout,parent,false);
        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    //BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, final int position ) {
        OjGoal x= getItem(position);
        Log.d(TAG,x.toString());
        OjGoalsRecyclerViewAdapter.ViewHolder holder= (OjGoalsRecyclerViewAdapter.ViewHolder)viewHolder;
        holder.getFriendId().setText(x.getRival());
        holder.getGoalNumber().setText(Integer.toString(x.getGoalNumber()));
        holder.getOjTagTextView().setText(x.getOjTag());
        holder.getUserId().setText(x.getMe());
        holder.getOjLinkTextView().setText(x.getOjTag());
        //holder.ojWebView.loadUrl("www.spoj.com");
    }
    //END_INCLUDE(recyclerViewOnBindViewHolder)
}
