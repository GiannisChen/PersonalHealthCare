package com.example.personalhealthcare.ui.ordinaryHome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.personalhealthcare.FeedbackActivity;
import com.example.personalhealthcare.R;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class OrdinaryHomeFragment extends Fragment {

    private Integer UserID;

    private OrdinaryHomeViewModel ordinaryHomeViewModel;
    private QMUITopBarLayout mTopBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ordinaryHomeViewModel =
                ViewModelProviders.of(this).get(OrdinaryHomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ordinary_home, container, false);

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