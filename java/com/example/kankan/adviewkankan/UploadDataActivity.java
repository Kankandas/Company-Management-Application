package com.example.kankan.adviewkankan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadDataActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseUser curUser;
    private StorageReference myStore;
    private DatabaseReference myRef;
    private ImageView imgProfilePhoto;
    private EditText edtName,edtAge;
    private Spinner spinnerType,spinnerDepertment;
    private String[] str={"Select Type","Employee","Manager","CareTeam"};
    private String type="";
    private String belong="";
    private String[] depertmentName={"Select Department","Sales","Graphics","Technical"};
    private Button btnUpload;
    private Uri uri;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);

        curUser=FirebaseAuth.getInstance().getCurrentUser();
        myStore= FirebaseStorage.getInstance().getReference();
        myRef= FirebaseDatabase.getInstance().getReference();

        imgProfilePhoto=findViewById(R.id.imgProfilePicture);
        edtAge=findViewById(R.id.edtAge);
        edtName=findViewById(R.id.edtName);
        btnUpload=findViewById(R.id.btnUpload);
        spinnerDepertment=findViewById(R.id.spinnerDepertment);
        spinnerType=findViewById(R.id.spinnerPosition);

        dialog=new ProgressDialog(UploadDataActivity.this);

        spinnerDepertment.setVisibility(View.INVISIBLE);

        imgProfilePhoto.setOnClickListener(this);
        btnUpload.setOnClickListener(this);


        ArrayAdapter adapterStr=new ArrayAdapter(this,android.R.layout.simple_spinner_item,str);

        adapterStr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(adapterStr);

        ArrayAdapter adapterDepertment=new ArrayAdapter(this,android.R.layout.simple_spinner_item,depertmentName);

        adapterStr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDepertment.setAdapter(adapterDepertment);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=str[position];
                if(type.equalsIgnoreCase("Employee")) {
                    spinnerDepertment.setVisibility(View.VISIBLE);
                }
                else {
                    spinnerDepertment.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinnerDepertment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    belong=depertmentName[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnUpload:
                letsUploadTheData();
                break;
            case R.id.imgProfilePicture:
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1620);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1620 && resultCode==RESULT_OK)
        {
            uri=data.getData();
            if(uri!=null) {
                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgProfilePhoto.setImageBitmap(bm);
                } catch (Exception e) {

                }
            }
        }

    }

    private void letsUploadTheData() {
        if(TextUtils.isEmpty(edtName.getText().toString()))
        {
            Toast.makeText(this,"PLease Enter Valid Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtAge.getText().toString()))
        {
            Toast.makeText(this,"PLease Enter Valid Age",Toast.LENGTH_SHORT).show();
            return;
        }
        if(uri==null)
        {
            Toast.makeText(this,"PLease Enter Valid Image",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(type) || type.equalsIgnoreCase("Select Type"))
        {
            Toast.makeText(this,"PLease Enter Valid Type of Your Position",Toast.LENGTH_SHORT).show();
            return;
        }
        if (type.equalsIgnoreCase("Employee"))
        {
            if(TextUtils.isEmpty(belong)||belong.equalsIgnoreCase("Select Department"))
            {
                Toast.makeText(this,"Please Enter Valid Department type",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        dialog.setTitle("Uploading Details...");
        dialog.show();

        StorageReference reference=myStore.child("ProfilePhoto"+System.currentTimeMillis());

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Profile profile;

                if(type.equalsIgnoreCase("Employee"))
                {
                    profile=new Profile(edtName.getText().toString(),edtAge.getText().toString(),type,taskSnapshot.getDownloadUrl().toString(),belong,curUser.getUid()
                            ,curUser.getEmail());
                }
                else {
                    profile=new Profile(edtName.getText().toString(),edtAge.getText().toString(),type,taskSnapshot.getDownloadUrl().toString(),"Null",curUser.getUid()
                            ,curUser.getEmail());
                }

                //String name, String age, String position, String profilePhoto, String department, String user_Id, String email

                myRef.child("Account List").child(curUser.getUid()).setValue(profile);

                if(type.equalsIgnoreCase("CareTeam")) {
                    dialog.dismiss();
                    startActivity(new Intent(UploadDataActivity.this, CareTeamActivity.class));
                    finish();
                }
                if(type.equalsIgnoreCase("Employee"))
                {
                    dialog.dismiss();
                    startActivity(new Intent(UploadDataActivity.this,EmployeeActivity.class));
                    finish();
                }
                if (type.equalsIgnoreCase("Manager")){
                        dialog.dismiss();
                        startActivity(new Intent(UploadDataActivity.this,ManagerActivity.class));
                        finish();
                }
            }
        });








    }
}
