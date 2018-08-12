package com.example.kankan.adviewkankan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtGoSignup;
    private EditText edtEmail,edtPassword;
    private Button btnLogin;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private ProgressDialog dialog;
    private String []list={"Select Type","Employee","Manager","CareTeam"};
    private Profile profile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        dialog=new ProgressDialog(LogInActivity.this);
        dialog.setTitle("Loading Account...");
        dialog.show();

        myRef= FirebaseDatabase.getInstance().getReference();
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        profile=new Profile();
        edtEmail=findViewById(R.id.edtLoginEmail);
        edtPassword=findViewById(R.id.edtPasswordLogin);
        btnLogin=findViewById(R.id.btnLogin);

        if(user!=null)
        {


            myRef.child("Account List").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Toast.makeText(LogInActivity.this,"Welcome Back",Toast.LENGTH_SHORT).show();
                    profile=dataSnapshot.getValue(Profile.class);
                    if(profile.getPosition().equalsIgnoreCase("careTeam")) {
                        startActivity(new Intent(LogInActivity.this, CareTeamActivity.class));
                        dialog.dismiss();
                        finish();
                    }
                    if(profile.getPosition().equalsIgnoreCase("manager")) {
                        startActivity(new Intent(LogInActivity.this, ManagerActivity.class));
                        dialog.dismiss();
                        finish();
                    }
                    if(profile.getPosition().equalsIgnoreCase("Employee")) {
                        startActivity(new Intent(LogInActivity.this, EmployeeActivity.class));
                        dialog.dismiss();
                        finish();
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },2000);
        txtGoSignup=findViewById(R.id.txtGoSignUp);

        txtGoSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txtGoSignUp:
                startActivity(new Intent(LogInActivity.this,SignUpActivity.class));
                finish();
                break;
            case R.id.btnLogin:
                letsLogin();
                break;
        }

    }

    private void letsLogin()
    {

        final String email=edtEmail.getText().toString();
        String pass=edtPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(LogInActivity.this,"Please enter valid Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(LogInActivity.this,"Please enter valid Password",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Logging in...");
        dialog.show();
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                    startActivity(new Intent(LogInActivity.this,CheckActivity.class));
                    dialog.dismiss();
                    finish();

            }
        });
    }
}
