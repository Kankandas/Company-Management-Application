package com.example.kankan.adviewkankan;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgBack,imgProfilePhoto;
    private Toolbar toolbar;
    private TextView txtName,txtAge,txtDepertment,txtMemberOF,txtEmail;
    private DatabaseReference myData;
    private FirebaseUser curUser;
    private Profile profile;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        toolbar=findViewById(R.id.toolbarAccountActivity);
        setSupportActionBar(toolbar);

        dialog=new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();

        profile=new Profile();

        myData= FirebaseDatabase.getInstance().getReference();
        curUser= FirebaseAuth.getInstance().getCurrentUser();

        imgBack=findViewById(R.id.imgBackAccountLayout);
        imgProfilePhoto=findViewById(R.id.imgProfilePhotoAccountActivity);
        txtName=findViewById(R.id.txtNameAccountActivity);
        txtAge=findViewById(R.id.txtAgeAccountActivity);
        txtDepertment=findViewById(R.id.txtDepertmentAccountActivity);
        txtMemberOF=findViewById(R.id.txtMemberOFAccountActivity);
        txtEmail=findViewById(R.id.txtEmailAccountActivity);

        getTheData();

        imgBack.setOnClickListener(this);



    }

    private void getTheData() {
        myData.child("Account List").child(curUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile=dataSnapshot.getValue(Profile.class);

                Picasso.with(AccountActivity.this).load(profile.getProfilePhoto()).into(imgProfilePhoto);
                txtAge.setText(profile.getAge());
                txtDepertment.setText(profile.getPosition());
                txtEmail.setText(profile.getEmail());
                if(profile.getDepartment().equalsIgnoreCase("Null"))
                {
                    txtMemberOF.setText(profile.getPosition());
                }
                else {
                    txtMemberOF.setText(profile.getDepartment());
                }

                txtName.setText(profile.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },4000);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgBackAccountLayout:
                finish();
                break;
        }

    }
}
