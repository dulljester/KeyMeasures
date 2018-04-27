package com.example.sj.keymeasures;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Now, this is a bit involved, but I found it illuminating.
 * What happens is the user clicks on one of the rows containing his/her Directions.
 * In our running example of GCJ, French, and Research, GCJ is clicked.
 * That means some work has been done in the direction of GCJ, and the user enter
 * the number of minutes dedicated to this direction. The problem is, how to deliver
 * this number back from the dialogue to the activity?
 * For this specific reason, a certain interface "onMyDialogueResult" is introduced.
 * When the user enters the number and clicks "Submit", the OnClickListener associated
 * with the clicked button calls the "finish" method of the implementation of that
 * interface, myDialogResult. The latter is implemented so that its finish() method
 * updates the database.
 * Found this solution somewhere on StackOverflow. It is weird though that we
 * have to go to such length for such a simple operation, but then again
 * dialogues were meant to be simple Yes/No functions...
 */
public class DialogueAddNewSession extends DialogFragment {
    private OnMyDialogResult myDialogResult;
    private AppDatabase db;
    private EditText editText;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        final View inf= inflater.inflate(R.layout.add_new_session_dialogue,null);
        db= AppDatabase.getInstance(getContext());
        editText= inf.findViewById(R.id.additionalHoursEditText);

        builder.setView(inf);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if ( myDialogResult != null )
                    myDialogResult.finish(editText.getText().toString());
            }
        });
        //return super.onCreateDialog(savedInstanceState);
        return builder.create();
    }

    public void setDialogResult( OnMyDialogResult dialogResult ) {
        myDialogResult= dialogResult;
    }

    public interface OnMyDialogResult {
        void finish( String result ) ;
    }

}
