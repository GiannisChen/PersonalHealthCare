package com.example.personalhealthcare.ui.feedback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.personalhealthcare.AddDietActivity;
import com.example.personalhealthcare.R;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.ServiceImpl.DietServiceImpl;
import com.example.personalhealthcare.VO.DietVO;
import com.example.personalhealthcare.ui.Diet.DietViewModel;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FeedbackFragment extends Fragment {


    private DietViewModel dietViewModel;

    private Integer UserID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dietViewModel =
                ViewModelProviders.of(this).get(DietViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diet, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }
        return root;
    }
}