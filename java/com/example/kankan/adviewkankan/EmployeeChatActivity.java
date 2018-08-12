package com.example.kankan.adviewkankan;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeChatActivity extends AppCompatActivity  implements View.OnClickListener{
    private DatabaseReference myRef;
    private FirebaseUser user;
    private ImageView imgBack,imgSentMessage;
    private EditText edtMessage;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private String depertment="";
    private List<Message> messageList;
    private Bundle bundle;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_chat);

        bundle=getIntent().getExtras();
        depertment=bundle.getString("DEPS");

        toolbar=findViewById(R.id.toolbarEmployeeChatActivity);
        setSupportActionBar(toolbar);

        imgBack=findViewById(R.id.imgEmployeeChatActivityBack);
        imgSentMessage=findViewById(R.id.imgSentMessage);
        edtMessage=findViewById(R.id.edtTypeMessage);
        recyclerView=findViewById(R.id.reViewEmployeeChatActivity);

        messageList=new ArrayList<>();
        profile=new Profile();

        myRef= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();

        getTheProfile();
        getTheMessage();

        imgSentMessage.setOnClickListener(this);
        imgBack.setOnClickListener(this);



    }

    private void getTheProfile() {

        myRef.child("Account List").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile=dataSnapshot.getValue(Profile.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getTheMessage() {

        myRef.child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Message message=snapshot.getValue(Message.class);
                    if(message.getDepertment().equalsIgnoreCase(depertment))
                    {
                        messageList.add(message);
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(EmployeeChatActivity.this));
                ChatAdapter adapter=new ChatAdapter(messageList,EmployeeChatActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgEmployeeChatActivityBack:
                finish();
                break;
            case R.id.imgSentMessage:
                //String message, String name, String photo, String depertment, String id
                if(edtMessage.getText().toString().equals("")||edtMessage.getText().toString().equals(" ")||edtMessage.getText().toString().equals("  ")||edtMessage.getText().toString().equals("   ")
                        ||edtMessage.getText().toString().equals("    ")||edtMessage.getText().toString().equals("     ")
                        ||edtMessage.getText().toString().equals("      "))
                {
                    Toast.makeText(EmployeeChatActivity.this,"Tou can't sent empty message",Toast.LENGTH_SHORT).show();
                    return;
                }
                String id=myRef.push().getKey();
                Message message=new Message(edtMessage.getText().toString(),profile.getName(),profile.getProfilePhoto(),depertment,id);
                myRef.child("Message").child(id).setValue(message);
                getTheMessage();
                edtMessage.setText("");
                break;
        }
    }
}
