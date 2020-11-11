package com.example.personalhealthcare.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.personalhealthcare.AddSleepStateActivity;
import com.example.personalhealthcare.FeedbackActivity;
import com.example.personalhealthcare.PO.Feedback;
import com.example.personalhealthcare.PO.User;
import com.example.personalhealthcare.R;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class HomeFragment extends Fragment {

    private Integer UserID;

    private HomeViewModel homeViewModel;
    private QMUITopBarLayout mTopBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }

        //懒得写了

        mTopBar = root.findViewById(R.id.topbar);
        mTopBar.setTitle("首页");

        initView();
        initTopBar();

        return root;
    }

    private void initView() {

    }

    private void initTopBar() {
        mTopBar.addRightImageButton(R.drawable.ic_feedback_black_24dp, R.id.qmui_topbar_item_left_back)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                        startActivity(intent);
                    }
                });
    }
}