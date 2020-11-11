package com.example.personalhealthcare.ui.Diet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.personalhealthcare.R;
import com.example.personalhealthcare.ui.SleepState.SleepStateViewModel;

public class DietFragment extends Fragment {

    private DietViewModel dietViewModel;
    private Integer UserID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dietViewModel =
                ViewModelProviders.of(this).get(DietViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diet, container, false);

        if(getArguments() != null) {
            UserID = getArguments().getInt("UserID");
            System.out.println(UserID);
        }

        return root;
    }
}