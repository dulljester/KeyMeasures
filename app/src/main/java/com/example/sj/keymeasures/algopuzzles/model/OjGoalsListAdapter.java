package com.example.sj.keymeasures.algopuzzles.model;

import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sj.keymeasures.R;
import com.example.sj.keymeasures.algopuzzles.helpers.EnvConstants;
import com.example.sj.keymeasures.firebase.FirebasetListAdapter;
import com.google.firebase.database.Query;

public class OjGoalsListAdapter extends FirebasetListAdapter<OjGoal> {
    public OjGoalsListAdapter( Query ref, Activity activity, int layout ) {
        super(ref,OjGoal.class,layout,activity);
    }
    @Override
    protected void populateView(final View rowView, final OjGoal model) {
        //LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //FIXME: ViewHolder pattern should be used!
        //View rowView= inflater.inflate(R.layout.cardview_online_judge,parent,false);
        TextView ojUserId= rowView.findViewById(R.id.ojUserId);
        TextView rivalId= rowView.findViewById(R.id.rivalID);
        TextView goalNumber= rowView.findViewById(R.id.goalNumbers);
        ojUserId.setText(emptyOrNot(model.getMe(),"your handle"));
        ojUserId.setVisibility(View.VISIBLE);
        rivalId.setText(emptyOrNot(model.getRival(),"your friend"));
        goalNumber.setText(Integer.toString(model.getGoalNumber()));
        TextView tagTextView= rowView.findViewById(R.id.tagTextView);
        tagTextView.setText(model.getOjTag());
        tagTextView.setVisibility(View.INVISIBLE);
        TextView ojLinkTextView= rowView.findViewById(R.id.ojLinkTextView);
        ojLinkTextView.setText(EnvConstants.findUrlByTag(model.getOjTag()));
    }

    private String emptyOrNot(String me, String your_handle) {
        if ( me.equals("") )
            return your_handle;
        return me;
    }
}
