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

public class ProjectGridAdapter extends RecyclerView.Adapter<ProjectGridAdapter.MyInnerGrid>{

    private List<Project> projectList;
    private Context context;

    public ProjectGridAdapter(List<Project> projectList, Context context) {
        this.projectList = projectList;
        this.context = context;
    }

    @Override
    public MyInnerGrid onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.project_grid_adapter,parent,false);
        return new MyInnerGrid(view);
    }

    @Override
    public void onBindViewHolder(MyInnerGrid holder, int position) {

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

    class MyInnerGrid extends RecyclerView.ViewHolder
    {
        private ImageView img;
        private TextView txtProjectName,txtProjectDetails,txtProjectStatus,txtProjectUpload,txtProjectSubmission,txtProjectDepertment;
        public MyInnerGrid(View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.imgProjectPhotoGridAdapter);
            txtProjectStatus=itemView.findViewById(R.id.txtProjectStatusGridAdapter);
            txtProjectUpload=itemView.findViewById(R.id.txtProjectUploadGridAdapter);
            txtProjectSubmission=itemView.findViewById(R.id.txtProjectSubmissionGridAdapter);
            txtProjectDetails=itemView.findViewById(R.id.txtProjectDetailsGridAdapter);
            txtProjectName=itemView.findViewById(R.id.txtProjectNameGridAdapter);
            txtProjectDepertment=itemView.findViewById(R.id.txtProjectDepertmentGridAdapter);
        }
    }
}
