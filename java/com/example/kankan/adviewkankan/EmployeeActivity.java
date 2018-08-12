package com.example.kankan.adviewkankan;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myData;
    private ImageView imgProfilePhoto;
    private TextView txtEmail,txtName;
    private Profile profile;
    private String depertment="";
    private List<Project> projectList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Dialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        toolbar=findViewById(R.id.toolbarEmployeeActivity);
        drawerLayout=findViewById(R.id.drawerEmployeeActivity);
        navigationView=findViewById(R.id.navViewEmployeeActivity);

        profile=new Profile();
        projectList=new ArrayList<>();
        dialog=new Dialog(EmployeeActivity.this);

        setTheToolbar();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        myData= FirebaseDatabase.getInstance().getReference();

        View view=navigationView.getHeaderView(0);
        imgProfilePhoto=view.findViewById(R.id.imgProfileCareTeamPhoto);
        txtName=view.findViewById(R.id.txtNameCareTeam);
        txtEmail=view.findViewById(R.id.txtEmailAddressCareTEam);

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayoutEmployeeActivity);


        getTheDataFromDataBase();
        swipeRefreshLayout.setRefreshing(true);
        getTheProjectList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTheProjectList();
            }
        });





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_employeeLogOut:
                        auth.signOut();
                        startActivity(new Intent(EmployeeActivity.this,LogInActivity.class));
                        finish();
                        break;
                    case R.id.menu_emloyeeTeam_Account:
                        startActivity(new Intent(EmployeeActivity.this,AccountActivity.class));
                        break;
                    case R.id.menu_employeeUploadedData:
                        Intent intent=new Intent(EmployeeActivity.this,EmployeeAllProjectAcitivity.class);
                        intent.putExtra("DEPT",depertment);
                        startActivity(intent);
                        break;
                    case R.id.menu_emloyeeTeam_Chat:
                        Intent intent1=new Intent(EmployeeActivity.this,EmployeeChatActivity.class);
                        intent1.putExtra("DEPS",depertment);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });


    }

    private void setTheRecyclerView(List<Project> list) {
        if(list.size()==0) {
            projectList.clear();
            dialog.setContentView(R.layout.empty_list_layout);
            TextView txt = dialog.findViewById(R.id.txtEmptyListLayout);
            Button btn = dialog.findViewById(R.id.btnEmptyListLayout);
            txt.setText("No Pending Project");
            dialog.show();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            swipeRefreshLayout.setRefreshing(false);
        }else {
            recyclerView = findViewById(R.id.reViewEmployeeActivity);
            recyclerView.setLayoutManager(new LinearLayoutManager(EmployeeActivity.this));
            EmployeeAdapter adapter = new EmployeeAdapter(list, EmployeeActivity.this);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private void getTheProjectList() {

        myData.child("Project").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Project project=snapshot.getValue(Project.class);
                    if(project.getDepertment().equalsIgnoreCase(depertment)&&project.getProjectStatus().equalsIgnoreCase("In Progress"))
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

    private void getTheDataFromDataBase() {

        myData.child("Account List").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile=dataSnapshot.getValue(Profile.class);
                Picasso.with(EmployeeActivity.this).load(profile.getProfilePhoto()).into(imgProfilePhoto);
                txtName.setText(profile.getName());
                txtEmail.setText(profile.getEmail());
                depertment=profile.getDepartment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setTheToolbar() {
        toolbar=findViewById(R.id.toolbarEmployeeActivity);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawerEmployeeActivity);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }
}
