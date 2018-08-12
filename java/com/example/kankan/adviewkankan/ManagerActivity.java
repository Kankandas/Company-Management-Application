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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity implements View.OnClickListener,Switch.OnCheckedChangeListener,ManagerFilterBottomSheet.BottomSheetListner {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private DatabaseReference myRef;
    private StorageReference myStore;
    private FirebaseAuth auth;
    private Dialog dialog;
    private FirebaseUser curUser;
    private Profile profile;
    private ImageView imgProfile;
    private TextView txtName,txtEmailAddress;
    private Switch switchCheck;
    private static String type="List";
    private static String sort="All Project";
    private RecyclerView recyclerView;
    private List<Project> projectList,sortProjectList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayoutFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        dialog=new Dialog(this);

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayoutManagerActivity);


        linearLayoutFilter=findViewById(R.id.linerarLayoutFilterManagerActivity);
        linearLayoutFilter.setOnClickListener(this);

        projectList=new ArrayList<>();
        sortProjectList=new ArrayList<>();

        setTheToolbar();

        myRef= FirebaseDatabase.getInstance().getReference();
        myStore= FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        curUser=auth.getCurrentUser();
        profile=new Profile();

        recyclerView=findViewById(R.id.reViewManagerActivity);

        switchCheck=findViewById(R.id.switchManagerActivity);
        switchCheck.setChecked(false);
        switchCheck.setText("List");




        getTheDataFromDataBase();
        if(sort.equalsIgnoreCase("All Project"))
        {
            sortByAllProject();
        }
        if (sort.equalsIgnoreCase("Pending Project"))
        {
            sortByPendingProject();
        }
        if (sort.equalsIgnoreCase("Complete"))
        {
            sortByCompleteProject();
        }

        swipeRefreshLayout.setRefreshing(true);
        setLinerLayoutToRecyclerView(projectList);

        navigationView=findViewById(R.id.navViewManagerActivity);
        View view=navigationView.getHeaderView(0);
        imgProfile=view.findViewById(R.id.imgProfileCareTeamPhoto);
        txtEmailAddress=view.findViewById(R.id.txtEmailAddressCareTEam);
        txtName=view.findViewById(R.id.txtNameCareTeam);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_careTeamLogOut:
                        auth.signOut();
                        startActivity(new Intent(ManagerActivity.this,LogInActivity.class));
                        finish();
                        break;
                    case R.id.menu_CareTeam_Account:
                        startActivity(new Intent(ManagerActivity.this,AccountActivity.class));
                        break;
                }
                return false;
            }
        });

        switchCheck.setOnCheckedChangeListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if(type.equalsIgnoreCase("List")) {
                    if(sort.equalsIgnoreCase("All Project"))
                    {
                        sortByAllProject();
                    }
                    if (sort.equalsIgnoreCase("Pending Project"))
                    {
                        sortByPendingProject();
                    }
                    if (sort.equalsIgnoreCase("Complete"))
                    {
                        sortByCompleteProject();
                    }
                }
                if (type.equalsIgnoreCase("Grid"))
                {
                    if(sort.equalsIgnoreCase("All Project"))
                    {
                        sortByAllProject();
                    }
                    if (sort.equalsIgnoreCase("Pending Project"))
                    {
                        sortByPendingProject();
                    }
                    if (sort.equalsIgnoreCase("Complete"))
                    {
                        sortByCompleteProject();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sort.equalsIgnoreCase("All Project"))
        {
            sortByAllProject();
        }
        if (sort.equalsIgnoreCase("Pending Project"))
        {
            sortByPendingProject();
        }
        if (sort.equalsIgnoreCase("Complete"))
        {
            sortByCompleteProject();
        }
    }

    private void getTheProjectData() {
        myRef.child("Project").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Project project1=snapshot.getValue(Project.class);
                    projectList.add(project1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getTheDataFromDataBase() {

        myRef.child("Account List").child(curUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile=dataSnapshot.getValue(Profile.class);
                Picasso.with(ManagerActivity.this).load(profile.getProfilePhoto()).into(imgProfile);
                txtName.setText(profile.getName());
                txtEmailAddress.setText(profile.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setTheToolbar() {
        toolbar=findViewById(R.id.toolbarManagerActivity);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawerManagerActivity);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            type="Grid";
            switchCheck.setChecked(true);
            switchCheck.setText("Grid");
            if(sort.equalsIgnoreCase("All Project"))
            {
                sortByAllProject();
            }
            if (sort.equalsIgnoreCase("Pending Project"))
            {
                sortByPendingProject();
            }
            if (sort.equalsIgnoreCase("Complete"))
            {
                sortByCompleteProject();
            }
        }
        else {
            type="List";
            switchCheck.setChecked(false);
            switchCheck.setText("List");
            if(sort.equalsIgnoreCase("All Project"))
            {
                sortByAllProject();
            }
            if (sort.equalsIgnoreCase("Pending Project"))
            {
                sortByPendingProject();
            }
            if (sort.equalsIgnoreCase("Complete"))
            {
                sortByCompleteProject();
            }
        }
    }

    private void setGridViewToRecyclerView(List<Project> list) {
        if(list.size()==0)
        {
            projectList.clear();
            dialog.setContentView(R.layout.empty_list_layout);
            TextView txt=dialog.findViewById(R.id.txtEmptyListLayout);
            Button btn=dialog.findViewById(R.id.btnEmptyListLayout);
            if(sort.equalsIgnoreCase("All Project"))
            {
                txt.setText("No Project Yet");
            }
            if (sort.equalsIgnoreCase("Pending Project"))
            {
                txt.setText("No Pending Project");
            }
            if (sort.equalsIgnoreCase("Complete"))
            {
                txt.setText("No Complete Project");
            }
            dialog.show();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

        }
        else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(ManagerActivity.this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            ProjectGridAdapter gridAdapter = new ProjectGridAdapter(list, ManagerActivity.this);
            recyclerView.setAdapter(gridAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setLinerLayoutToRecyclerView(List<Project> list) {
        if(list.size()==0)
        {
            projectList.clear();
            dialog.setContentView(R.layout.empty_list_layout);
            TextView txt=dialog.findViewById(R.id.txtEmptyListLayout);
            Button btn=dialog.findViewById(R.id.btnEmptyListLayout);
            if(sort.equalsIgnoreCase("All Project"))
            {
                txt.setText("No Project Yet");
            }
            if (sort.equalsIgnoreCase("Pending Project"))
            {
                txt.setText("No Pending Project");
            }
            if (sort.equalsIgnoreCase("Complete"))
            {
                txt.setText("No Complete Project");
            }
            dialog.show();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
        else {
            recyclerView.setLayoutManager(new LinearLayoutManager(ManagerActivity.this));
            ProjectListAdapter listAdapter = new ProjectListAdapter(list, ManagerActivity.this);
            recyclerView.setAdapter(listAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onBottomClicked(String text) {
        if (text.equalsIgnoreCase("All Project"))
        {
            sort="All Project";
            sortByAllProject();
        }
        if (text.equalsIgnoreCase("Complete Project"))
        {
            sort="Complete";
            sortByCompleteProject();
        }
        if (text.equalsIgnoreCase("Pending Project"))
        {
            sort="Pending Project";
            sortByPendingProject();
        }
    }

    private void sortByPendingProject() {
        getTheProjectData();
        sortProjectList.clear();
        for(int i=0;i<projectList.size();i++)
        {
            if(projectList.get(i).getProjectStatus().equalsIgnoreCase("In Progress"))
            {
                sortProjectList.add(projectList.get(i));
            }
        }
        if(type.equalsIgnoreCase("List"))
        {
            setLinerLayoutToRecyclerView(sortProjectList);
        }
        if (type.equalsIgnoreCase("Grid"))
        {
            setGridViewToRecyclerView(sortProjectList);
        }

    }

    private void sortByCompleteProject() {
        getTheProjectData();
        sortProjectList.clear();
        for(int i=0;i<projectList.size();i++)
        {
            if(projectList.get(i).getProjectStatus().equalsIgnoreCase("Complete"))
            {
                sortProjectList.add(projectList.get(i));
            }
        }
        if(type.equalsIgnoreCase("List"))
        {
            setLinerLayoutToRecyclerView(sortProjectList);
        }
        if (type.equalsIgnoreCase("Grid"))
        {
            setGridViewToRecyclerView(sortProjectList);
        }

    }

    private void sortByAllProject() {
        getTheProjectData();
        if(type.equalsIgnoreCase("List"))
        {
            setLinerLayoutToRecyclerView(projectList);
        }
        if (type.equalsIgnoreCase("Grid"))
        {
            setGridViewToRecyclerView(projectList);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.linerarLayoutFilterManagerActivity:
                    ManagerFilterBottomSheet managerFilterBottomSheet=new ManagerFilterBottomSheet();
                    managerFilterBottomSheet.show(getSupportFragmentManager(),"");

                break;
        }
    }
}
