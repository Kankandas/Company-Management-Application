package com.example.kankan.adviewkankan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CareTeamActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toogle;
    private NavigationView navigationView;
    private Uri uri;
    private DatabaseReference myRef;
    private StorageReference myStore;
    private FirebaseAuth auth;
    private FirebaseUser curUser;
    private Profile profile;
    private ImageView imgProfile,imgProjectImage,imgCalender;
    private EditText edtProjectName,edtProjectDetails;
    private Spinner spinner;
    private TextView txtDate,txtName,txtEmailAddress;
    private Button btnUpload;
    private static String type="",date="";
    private Dialog dialog;
    private String[] depertmentName={"Select Department","Sales","Graphics","Technical"};
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_team);



        setTheToolbaar();

        myRef=FirebaseDatabase.getInstance().getReference();
        myStore=FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        curUser=auth.getCurrentUser();
        profile=new Profile();
        progressDialog=new ProgressDialog(this);

        navigationView=findViewById(R.id.navView);
        View view=navigationView.getHeaderView(0);
        imgProfile=view.findViewById(R.id.imgProfileCareTeamPhoto);
        txtEmailAddress=view.findViewById(R.id.txtEmailAddressCareTEam);
        txtName=view.findViewById(R.id.txtNameCareTeam);

        getTheDataFromDataBase();

        imgCalender=findViewById(R.id.imgCalenderCareActivity);
        btnUpload=findViewById(R.id.btnUploadCareTEamActivity);
        spinner=findViewById(R.id.spinnerCareTeamActivityTeamType);
        txtDate=findViewById(R.id.txtSubmissionDate);
        edtProjectDetails=findViewById(R.id.edtProjectDetailsCareActivity);
        edtProjectName=findViewById(R.id.edtProjectNameCareActivity);
        imgProjectImage=findViewById(R.id.imgProjectPhotoCareTEamActivity);

        ArrayAdapter adapterDepertment=new ArrayAdapter(CareTeamActivity.this,android.R.layout.simple_spinner_item,depertmentName);
        adapterDepertment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterDepertment);







        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_careTeamLogOut:
                        auth.signOut();
                        startActivity(new Intent(CareTeamActivity.this,LogInActivity.class));
                        finish();
                        break;
                    case R.id.menu_CareTeam_Account:
                        startActivity(new Intent(CareTeamActivity.this,AccountActivity.class));
                        break;
                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=depertmentName[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        imgCalender.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        imgProjectImage.setOnClickListener(this);
    }

    private void getTheDataFromDataBase() {

        myRef.child("Account List").child(curUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile=dataSnapshot.getValue(Profile.class);
                Picasso.with(CareTeamActivity.this).load(profile.getProfilePhoto()).into(imgProfile);
                txtName.setText(profile.getName());
                txtEmailAddress.setText(profile.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setTheToolbaar() {

        toolbar=findViewById(R.id.toolbarCareTeamActivity);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawerCareTeamActivity);

        toogle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);

        drawerLayout.addDrawerListener(toogle);

        toogle.syncState();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgCalenderCareActivity:
                dialog=new Dialog(CareTeamActivity.this);
                dialog.setContentView(R.layout.calender_open_view);
                dialog.show();
                CalendarView calendarView=dialog.findViewById(R.id.calender_Open_layout_Date);

                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        date=dayOfMonth+"."+month+"."+year;
                        dialog.cancel();
                        txtDate.setText("Submission Date:   "+date);
                    }
                });
                break;
            case R.id.btnUploadCareTEamActivity:
                uploadData();
                break;
            case R.id.imgProjectPhotoCareTEamActivity:
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10045);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10045 && resultCode==RESULT_OK)
        {
            uri=data.getData();
            if(uri!=null)
            {
                try{
                    Bitmap bm= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    imgProjectImage.setImageBitmap(bm);

                }catch (Exception e)
                {

                }
            }
        }
    }

    private void uploadData() {
        if(TextUtils.isEmpty(edtProjectDetails.getText().toString()))
        {
            Toast.makeText(CareTeamActivity.this,"Please give the details of the project",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(edtProjectName.getText().toString()))
        {
            Toast.makeText(CareTeamActivity.this,"Please give some name of the project",Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtDate.getText().toString().equalsIgnoreCase("Submission Date:")){
            Toast.makeText(CareTeamActivity.this,"Please select the submission date",Toast.LENGTH_SHORT).show();
            return;
        }
        if (type.equalsIgnoreCase("Select Department")||type.equalsIgnoreCase("")){
            Toast.makeText(CareTeamActivity.this,"Please select Project Type",Toast.LENGTH_SHORT).show();
            return;
        }
        if (uri==null)
        {
            Toast.makeText(CareTeamActivity.this,"Please select some Project Image",Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setTitle("Uploading Project...");
        progressDialog.show();


        StorageReference reference=myStore.child("ProjectPhoto"+System.currentTimeMillis());

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String key=myRef.push().getKey();
                Project project=new Project(edtProjectDetails.getText().toString(),edtProjectName.getText().toString(),date,"",taskSnapshot.getDownloadUrl().toString(),
                                    type,key,"In Progress");
                myRef.child("Project").child(key).setValue(project);
                edtProjectDetails.setText("");
                edtProjectName.setText("");
                txtDate.setText("Submission Date:");
                imgProjectImage.setImageResource(R.drawable.galary);
                progressDialog.cancel();
            }
        });

    }
}
