package com.example.kankan.adviewkankan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckActivity extends AppCompatActivity {
    private String []list={"Select Type","Employee","Manager","CareTeam"};
    private DatabaseReference reference;
    private FirebaseUser user;
    private Profile profile;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        profile=new Profile();

        reference= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();

        reference.child("Account List").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile=dataSnapshot.getValue(Profile.class);
                if(profile.getPosition().equalsIgnoreCase("careTeam")) {
                    startActivity(new Intent(CheckActivity.this, CareTeamActivity.class));
                    finish();
                    progressDialog.dismiss();
                }
                if(profile.getPosition().equalsIgnoreCase("manager")) {
                    startActivity(new Intent(CheckActivity.this, ManagerActivity.class));
                    finish();
                    progressDialog.dismiss();
                }
                if(profile.getPosition().equalsIgnoreCase("Employee")) {
                    startActivity(new Intent(CheckActivity.this, EmployeeActivity.class));
                    finish();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
