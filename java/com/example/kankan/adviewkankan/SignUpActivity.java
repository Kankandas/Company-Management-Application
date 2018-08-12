package com.example.kankan.adviewkankan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtGoLogin;
    private Button btnSignup12;
    private FirebaseUser user;
    private DatabaseReference myData;
    private FirebaseAuth auth;
    private EditText edtSignupEmail,edtPasswordSignup;
    private String email,password;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtGoLogin=findViewById(R.id.txtGoLogin);

        edtPasswordSignup=findViewById(R.id.edtPasswordSignup);
        edtSignupEmail=findViewById(R.id.edtSignupEmail);
        btnSignup12=findViewById(R.id.btnSignup);


        myData= FirebaseDatabase.getInstance().getReference();
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();


        dialog=new ProgressDialog(SignUpActivity.this);

        if(user !=null)
        {
            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            finish();
        }




        txtGoLogin.setOnClickListener(this);



        btnSignup12.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txtGoLogin:
                startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
                finish();
                break;
            case R.id.btnSignup:
                getSignup();

                break;
        }
    }

    private void getSignup()
    {
        email=edtSignupEmail.getText().toString();
        password=edtPasswordSignup.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(SignUpActivity.this,"Enter valid Email id",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(SignUpActivity.this,"Enter valid password",Toast.LENGTH_LONG).show();
            return;
        }
        dialog.setMessage("Please wait while loading....");
        dialog.show();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                startActivity(new Intent(SignUpActivity.this,UploadDataActivity.class));
                finish();

            }
        });

    }
}
