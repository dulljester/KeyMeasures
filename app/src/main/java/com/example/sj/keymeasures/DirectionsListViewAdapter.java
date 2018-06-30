package com.example.sj.keymeasures;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Adapter implemented following the official documentation, nothing special
 */
class DirectionsListViewAdapter implements ListAdapter {
    private final Context context;
    private final AppDatabase db;
    private final List<Double> hoursFlown;
    private final List<String> names;

    public DirectionsListViewAdapter( Context context, AppDatabase db, List<Double> hoursFlown, List<String> names  ) {
        this.context= context;
        this.db= db;
        this.hoursFlown= hoursFlown;
        this.names= names;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView( final int i, View view, ViewGroup parent ) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.list_view_item,parent,false);
        TextView textViewHours= (TextView)rowView.findViewById(R.id.textViewHours);
        textViewHours.setText(String.format("%.2f",hoursFlown.get(i)));
        TextView textViewDirectionsName= (TextView)rowView.findViewById(R.id.textViewDirectionName);
        textViewDirectionsName.setText(names.get(i));
        //final EditText editTextAdditionalHours= (EditText)rowView.findViewById(R.id.editTextAdditionalHours);
        /*
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextAdditionalHours.requestFocus();
            }
        });
        final Button submitButton= (Button)rowView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final WorkSession ws= new WorkSession();
                ws.setAid(i);
                ws.setDuration(Integer.parseInt(editTextAdditionalHours.getText().toString()));
                ws.setWhen(LocalDateTime.now());
                new AsyncTask<Void,Void,Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        db.workSessionDao().insertAll(ws);
                        return null;
                    }
                }.execute();
            }
        });
        */
        return rowView;
    }

    @Override
    public int getItemViewType(int i) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return names == null || names.isEmpty();
    }
}

