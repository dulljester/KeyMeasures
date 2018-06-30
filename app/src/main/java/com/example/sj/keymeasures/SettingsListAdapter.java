package com.example.sj.keymeasures;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.sj.keymeasures.algopuzzles.helpers.EnvConstants;
import com.example.sj.keymeasures.algopuzzles.model.OjGoal;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.zip.Inflater;

public class SettingsListAdapter implements ListAdapter {
    private Context context;
    private List<OjGoal> list;

    public SettingsListAdapter(Context context, List<OjGoal> list ) {
        this.context= context;
        this.list= list;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private String emptyOrNot( String x, String y )  {
        return x.equals("") ? y : x;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //FIXME: ViewHolder pattern should be used!
        View rowView= inflater.inflate(R.layout.cardview_online_judge,parent,false);
        TextInputEditText ojUserId= rowView.findViewById(R.id.ojUserId);
        TextInputEditText rivalId= rowView.findViewById(R.id.rivalID);
        TextInputEditText goalNumber= rowView.findViewById(R.id.goalNumbers);
        ojUserId.setText(emptyOrNot(list.get(i).getMe(),"your handle"));
        rivalId.setText(emptyOrNot(list.get(i).getRival(),"your friend"));
        goalNumber.setText(list.get(i).getGoalNumber()+"");

        TextView tagTextView= rowView.findViewById(R.id.tagTextView);
        tagTextView.setText(list.get(i).getOjTag());
        TextView ojLinkTextView= rowView.findViewById(R.id.ojLinkTextView);
        ojLinkTextView.setText(list.get(i).getOjTag());

        return rowView;

    }

    @Override
    public int getItemViewType( int i ) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}

