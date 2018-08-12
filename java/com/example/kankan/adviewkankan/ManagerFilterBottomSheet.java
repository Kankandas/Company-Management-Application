package com.example.kankan.adviewkankan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ManagerFilterBottomSheet extends BottomSheetDialogFragment {
    private View view;
    private RadioGroup radioGroup;
    private RadioButton radioButtonAllProject,radioButtonPendingProject,radioButtonCompleteProject;
    private static boolean allProject=true,completePRoject=false,pendingProject=false;

    private BottomSheetListner bottomSheetListner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.filter_layout,container,false);

        radioGroup=view.findViewById(R.id.radioGroupFilterLayout);
        radioButtonAllProject=view.findViewById(R.id.radioButtonAllProjectFilterLayout);
        radioButtonPendingProject=view.findViewById(R.id.radioButtonPendingProjectFilterLayout);
        radioButtonCompleteProject=view.findViewById(R.id.radioButtonCompleteProjectFilterLayout);

        if(allProject)
        {
            radioButtonAllProject.setChecked(true);
        }
        if(completePRoject)
        {
            radioButtonCompleteProject.setChecked(true);
        }
        if (pendingProject)
        {
            radioButtonPendingProject.setChecked(true);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radioButtonAllProjectFilterLayout:
                        bottomSheetListner.onBottomClicked("All Project");
                        allProject=true;
                        completePRoject=false;
                        pendingProject=false;
                        dismiss();
                        break;
                    case R.id.radioButtonCompleteProjectFilterLayout:
                        bottomSheetListner.onBottomClicked("Complete Project");
                        allProject=false;
                        completePRoject=true;
                        pendingProject=false;
                        dismiss();
                        break;
                    case R.id.radioButtonPendingProjectFilterLayout:
                        bottomSheetListner.onBottomClicked("Pending Project");
                        allProject=false;
                        completePRoject=false;
                        pendingProject=true;
                        dismiss();
                        break;
                }
            }
        });



        return view;
    }

    public interface  BottomSheetListner{
        void onBottomClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            bottomSheetListner=(BottomSheetListner)context;

        }catch (Exception e)
        {

        }
    }
}
