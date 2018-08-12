package com.example.kankan.adviewkankan;

import android.app.Dialog;
import android.media.Image;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAllProjectAcitivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myData;
    private Toolbar toolbar;
    private ImageView imgBack;
    private RecyclerView recyclerView;
    private String depertment="";
    private Bundle bundle;
    private List<Project> projectList;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_all_project_acitivity);

        bundle=getIntent().getExtras();
        depertment=bundle.getString("DEPT");
        projectList=new ArrayList<>();

        setTheToolbar();


        imgBack=findViewById(R.id.imgEmployeeAllProjectActivityBack);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayoutEmployeeAllProjectActivity);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        myData= FirebaseDatabase.getInstance().getReference();

        swipeRefreshLayout.setRefreshing(true);
        getTheProjectList();

        imgBack.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getTheProjectList();
            }
        });



    }
    private void setTheRecyclerView(List<Project> list) {
        recyclerView=findViewById(R.id.reViewEmployeeAllProjectActivity);
            recyclerView.setLayoutManager(new LinearLayoutManager(EmployeeAllProjectAcitivity.this));
            ProjectListAdapter adapter = new ProjectListAdapter(list, EmployeeAllProjectAcitivity.this);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);


    }
    private void getTheProjectList() {

        myData.child("Project").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Project project=snapshot.getValue(Project.class);
                    if(project.getDepertment().equalsIgnoreCase(depertment))
                    {
                        projectList.add(project);
                    }
                    setTheRecyclerView(projectList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setTheToolbar() {
        toolbar=findViewById(R.id.toolbarEmployeeAllProjectActivity);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgEmployeeAllProjectActivityBack:
                finish();
                break;
        }
    }
}
