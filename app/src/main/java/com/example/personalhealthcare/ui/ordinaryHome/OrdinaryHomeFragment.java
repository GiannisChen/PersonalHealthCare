package com.example.personalhealthcare.ui.ordinaryHome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private ImageView itemFoodImage1;
    private ImageView itemFoodImage2;
    private ImageView itemFoodImage3;
    private ImageView itemFoodImage4;

    private TextView itemFoodName1;
    private TextView itemFoodName2;
    private TextView itemFoodName3;
    private TextView itemFoodName4;

    private TextView itemFoodCalories1;
    private TextView itemFoodCalories2;
    private TextView itemFoodCalories3;
    private TextView itemFoodCalories4;

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

        initView(root);
        initTopBar();

        return root;
    }

    private void initView(View root) {
        itemFoodImage1 = root.findViewById(R.id.item_img_1);
        itemFoodImage2 = root.findViewById(R.id.item_img_2);
        itemFoodImage3 = root.findViewById(R.id.item_img_3);
        itemFoodImage4 = root.findViewById(R.id.item_img_4);

        itemFoodName1 = root.findViewById(R.id.item_food_name_1);
        itemFoodName2 = root.findViewById(R.id.item_food_name_2);
        itemFoodName3 = root.findViewById(R.id.item_food_name_3);
        itemFoodName4 = root.findViewById(R.id.item_food_name_4);

        itemFoodCalories1 = root.findViewById(R.id.item_food_calories_1);
        itemFoodCalories2 = root.findViewById(R.id.item_food_calories_2);
        itemFoodCalories3 = root.findViewById(R.id.item_food_calories_3);
        itemFoodCalories4 = root.findViewById(R.id.item_food_calories_4);


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