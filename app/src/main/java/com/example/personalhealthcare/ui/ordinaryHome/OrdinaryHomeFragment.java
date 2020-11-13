package com.example.personalhealthcare.ui.ordinaryHome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.personalhealthcare.CommonUtils.ImageUtils;
import com.example.personalhealthcare.FeedbackActivity;
import com.example.personalhealthcare.PO.FoodData;
import com.example.personalhealthcare.R;
import com.example.personalhealthcare.ROFoodListActivity;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.ServiceImpl.DietServiceImpl;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class OrdinaryHomeFragment extends Fragment {

    private Integer UserID;
    private List<FoodData> foodList;
    private DietService dietService = new DietServiceImpl();

    private OrdinaryHomeViewModel ordinaryHomeViewModel;
    private Handler mainHandler;

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

    private QMUIRoundButton buttonShowAll;
    private QMUIRoundButton buttonChangeRecommend;

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

        buttonShowAll = root.findViewById(R.id.button_show_me_money);
        buttonShowAll.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ROFoodListActivity.class);
                startActivity(intent);
            }
        });
        buttonChangeRecommend = root.findViewById(R.id.button_change_recommend);
        buttonChangeRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        mainHandler = new Handler(Looper.getMainLooper());
        foodList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                foodList = dietService.getFoodData();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                    }
                });
            }
        }).start();
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

    private void setData() {
        if(foodList != null && foodList.size() >= 4) {
            Random random = new Random();
            List<Integer> randNumber = new LinkedList<>();
            int tmpInt = 0;
            int length = foodList.size();
            while(randNumber.size() < 4) {
                tmpInt = random.nextInt(length);
                if(!randNumber.contains(tmpInt)) {
                    randNumber.add(tmpInt);
                }
            }

            System.out.println(randNumber);

            itemFoodName1.setText(foodList.get(randNumber.get(0)).getFoodName());
            itemFoodCalories1.setText(foodList.get(randNumber.get(0)).getCalorie().toString() + "cal");
            if(foodList.get(randNumber.get(0)).getImage() != null
                    && foodList.get(randNumber.get(0)).getImage().length > 0) {
                itemFoodImage1.setImageBitmap(ImageUtils.Bytes2Bitmap(foodList.get(randNumber.get(0)).getImage()));
            }
            else {
                itemFoodImage1.setImageResource(R.mipmap.ic_no_image2);
            }

            itemFoodName2.setText(foodList.get(randNumber.get(1)).getFoodName());
            itemFoodCalories2.setText(foodList.get(randNumber.get(1)).getCalorie().toString() + "cal");
            if(foodList.get(randNumber.get(1)).getImage() != null
                    && foodList.get(randNumber.get(1)).getImage().length > 0) {
                itemFoodImage2.setImageBitmap(ImageUtils.Bytes2Bitmap(foodList.get(randNumber.get(1)).getImage()));
            }
            else {
                itemFoodImage2.setImageResource(R.mipmap.ic_no_image2);
            }

            itemFoodName3.setText(foodList.get(randNumber.get(2)).getFoodName());
            itemFoodCalories3.setText(foodList.get(randNumber.get(2)).getCalorie().toString() + "cal");
            if(foodList.get(randNumber.get(2)).getImage() != null
                    && foodList.get(randNumber.get(2)).getImage().length > 0) {
                itemFoodImage3.setImageBitmap(ImageUtils.Bytes2Bitmap(foodList.get(randNumber.get(2)).getImage()));
            }
            else {
                itemFoodImage3.setImageResource(R.mipmap.ic_no_image2);
            }

            itemFoodName4.setText(foodList.get(randNumber.get(3)).getFoodName());
            itemFoodCalories4.setText(foodList.get(randNumber.get(3)).getCalorie().toString() + "cal");
            if(foodList.get(randNumber.get(3)).getImage() != null
                    && foodList.get(randNumber.get(3)).getImage().length > 0) {
                itemFoodImage4.setImageBitmap(ImageUtils.Bytes2Bitmap(foodList.get(randNumber.get(3)).getImage()));
            }
            else {
                itemFoodImage4.setImageResource(R.mipmap.ic_no_image2);
            }
        }
        else {
            Toast.makeText(getActivity(), "网络错误！", Toast.LENGTH_SHORT).show();
        }
    }
}