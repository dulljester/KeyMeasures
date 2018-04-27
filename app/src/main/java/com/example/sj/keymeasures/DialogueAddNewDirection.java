package com.example.sj.keymeasures;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDateTime;

/**
 * When user clicks a floating action button to add a new Direction,
 * a dialogue appears to enable the user to enter the name of the new Direction
 * NOTE: no checking mechanism is implemented to guard against entering an
 * existing direction name; Ideally, the Direction names should be primary keys,
 * but somehow I chose to store the Directions in a separate table with integers
 * as primary keys
 */
public class DialogueAddNewDirection extends DialogFragment {

    private AppDatabase db;
    private EditText editText;

    public interface DialogueListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        final View inf= inflater.inflate(R.layout.add_new_direction_dialogue,null);
        editText= inf.findViewById(R.id.newDirectionEditText);
        db= AppDatabase.getInstance(getContext());

        builder.setView(inf);

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogueListener.onDialogPositiveClick(DialogueAddNewDirection.this);
                        String newName= editText.getText().toString();
                        /**
                         * take the string from the EditText and enter it into the Directions table
                         * Also, we make sure to add a new "session" with duration 0 and timestamp "Now",
                         * so that when later the user clicks "Refresh", the new direction appears on the list,
                         * albeit with a 0.00 in front of it
                         */
                        new AsyncTask<String,Void,Void>() {
                            @Override
                            protected Void doInBackground(String... strings) {
                                final KeyDirections kd= new KeyDirections();
                                kd.setActivityName(strings[0]);
                                db.keyDirectionsDao().insertAll(kd);
                                return null;
                            }
                        }.execute(newName);
                        new AsyncTask<String,Void,Integer>() {
                            @Override
                            protected Integer doInBackground(String... strings) {
                                return db.keyDirectionsDao().getIdByName(strings[0]);
                            }
                            @Override
                            protected void onPostExecute(final Integer integer) {
                                super.onPostExecute(integer);
                                new AsyncTask<Void,Void,Void>() {
                                   @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    protected Void doInBackground(Void ... voids) {
                                        final WorkSession ws= new WorkSession();
                                        ws.setAid(integer);
                                        ws.setWhen(LocalDateTime.now());
                                        ws.setDuration(0);
                                        db.workSessionDao().insertAll(ws);
                                        return null;
                                    }
                                }.execute();
                            }
                        }.execute(newName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        return builder.create();
    }
}
