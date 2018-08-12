package com.example.kankan.adviewkankan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmployeeAdapter  extends RecyclerView.Adapter<EmployeeAdapter.MyInnerEmployee>{
    private List<Project> projectList;
    private Context context;
    private DatabaseReference myRef;

    public EmployeeAdapter(List<Project> projectList, Context context) {
        this.projectList = projectList;
        this.context = context;
        myRef= FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public MyInnerEmployee onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.employee_adapter,parent,false);
        return new MyInnerEmployee(view);
    }
    @Override
    public void onBindViewHolder(MyInnerEmployee holder, int position) {
        Picasso.with(context).load(projectList.get(position).getProjectImage()).into(holder.img);
        holder.txtProjectName.setText(projectList.get(position).getProjectName());
        holder.txtProjectDepertment.setText(projectList.get(position).getDepertment());
        holder.txtProjectDetails.setText(projectList.get(position).getProjectDetails());
        holder.txtProjectSubmission.setText(projectList.get(position).getSubmissionDate());
        holder.txtProjectUpload.setText(projectList.get(position).getUploadDate());
        holder.txtProjectStatus.setText(projectList.get(position).getProjectStatus());
        if(projectList.get(position).getProjectStatus().equalsIgnoreCase("Complete Project"))
        {
            holder.btnDone.setVisibility(View.INVISIBLE);
        }
        else {
            holder.btnDone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }


    class MyInnerEmployee extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView img;
        private TextView txtProjectName,txtProjectDetails,txtProjectStatus,txtProjectUpload,txtProjectSubmission,txtProjectDepertment;
        private Button btnDone;
        public MyInnerEmployee(View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.imgProjectPhotoEmployeeAdapter);
            txtProjectStatus=itemView.findViewById(R.id.txtProjectStatusEmployeeAdapter);
            txtProjectUpload=itemView.findViewById(R.id.txtProjectUploadEmployeeAdapter);
            txtProjectSubmission=itemView.findViewById(R.id.txtProjectSubmissionEmployeeAdapter);
            txtProjectDetails=itemView.findViewById(R.id.txtProjectDetailsEmployeeAdapter);
            txtProjectName=itemView.findViewById(R.id.txtProjectNameEmployeeAdapter);
            txtProjectDepertment=itemView.findViewById(R.id.txtProjectDepertmentEmployeeAdapter);
            btnDone=itemView.findViewById(R.id.btnCompleteEmployeeAdapter);

            btnDone.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            switch (v.getId())
            {
                case R.id.btnCompleteEmployeeAdapter:
                    myRef.child("Project").child(projectList.get(pos).getId()).child("projectStatus").setValue("Complete");
                    btnDone.setVisibility(View.INVISIBLE);
                    notifyItemChanged(pos);
                    break;
            }
        }
    }
}
