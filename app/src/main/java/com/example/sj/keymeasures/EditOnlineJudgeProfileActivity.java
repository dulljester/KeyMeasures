package com.example.sj.keymeasures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sj.keymeasures.algopuzzles.model.OjGoal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditOnlineJudgeProfileActivity extends AppCompatActivity {
    private EditText userIdEditText, friendIdEditText, goalNumberEditText;
    private Button saveButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_oj_info_layout);
        Intent intent= getIntent();
        final String ojTag= intent.getStringExtra("ojTag");

        userIdEditText= findViewById(R.id.userIdEditText);
        friendIdEditText= findViewById(R.id.friendIdEditText);
        goalNumberEditText= findViewById(R.id.goalSolvedProblemsEditText);
        saveButton= findViewById(R.id.saveUserProfileButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friend= friendIdEditText.getText().toString();
                String me= userIdEditText.getText().toString();
                String numbers= goalNumberEditText.getText().toString();
                if ( friend.equals("") ) friend= "N/A";
                if ( me.equals("") ) me= "N/A";
                if ( numbers.equals("") ) numbers= "1";
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
                ref.child("users").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid())
                        .child("oj_goals").child(ojTag)
                        .setValue(new OjGoal(me,friend,ojTag,Integer.parseInt(numbers)));
            }
        });
    }
}
