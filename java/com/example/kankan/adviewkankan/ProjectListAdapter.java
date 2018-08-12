package com.example.kankan.adviewkankan;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProjectListAdapter  extends RecyclerView.Adapter<ProjectListAdapter.MyInner>{
    private List<Project> projectList;
    private Context context;

    public ProjectListAdapter(List<Project> projectList, Context context) {
        this.projectList = projectList;
        this.context = context;
    }

    @Override
    public MyInner onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.project_list_adapter,parent,false);
        return new MyInner(view);
    }

    @Override
    public void onBindViewHolder(MyInner holder, int position) {
        Picasso.with(context).load(projectList.get(position).getProjectImage()).into(holder.img);
        holder.txtProjectName.setText(projectList.get(position).getProjectName());
        holder.txtProjectDepertment.setText(projectList.get(position).getDepertment());
        holder.txtProjectDetails.setText(projectList.get(position).getProjectDetails());
        holder.txtProjectSubmission.setText(projectList.get(position).getSubmissionDate());
        holder.txtProjectUpload.setText(projectList.get(position).getUploadDate());

        if(projectList.get(position).getProjectStatus().equalsIgnoreCase("Complete"))
        {
            holder.txtProjectStatus.setText("Complete");
            holder.txtProjectStatus.setTextColor(Color.BLUE);
        }
        else {
            holder.txtProjectStatus.setText(projectList.get(position).getProjectStatus());
            holder.txtProjectStatus.setTextColor(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    class MyInner extends RecyclerView.ViewHolder
    {
        private ImageView img;
        private TextView txtProjectName,txtProjectDetails,txtProjectStatus,txtProjectUpload,txtProjectSubmission,txtProjectDepertment;
        public MyInner(View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.imgProjectPhotoListAdapter);
            txtProjectStatus=itemView.findViewById(R.id.txtProjectStatusListAdapter);
            txtProjectUpload=itemView.findViewById(R.id.txtProjectUploadListAdapter);
            txtProjectSubmission=itemView.findViewById(R.id.txtProjectSubmissionListAdapter);
            txtProjectDetails=itemView.findViewById(R.id.txtProjectDetailsListAdapter);
            txtProjectName=itemView.findViewById(R.id.txtProjectNameListAdapter);
            txtProjectDepertment=itemView.findViewById(R.id.txtProjectDepertmentListAdapter);
        }
    }
}
