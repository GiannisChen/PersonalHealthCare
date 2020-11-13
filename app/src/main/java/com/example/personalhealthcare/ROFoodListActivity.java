package com.example.personalhealthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.personalhealthcare.PO.FoodData;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.ServiceImpl.DietServiceImpl;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;

import java.util.ArrayList;
import java.util.List;

public class ROFoodListActivity extends AppCompatActivity {

    private QMUIGroupListView mGroupListView;
    private Handler mainHandler;
    private DietService dietService = new DietServiceImpl();
    private List<FoodData> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rofood_list);

        mGroupListView = findViewById(R.id.food_groupListView);
        setTitle("食物列表");

        initGroupListView(mGroupListView);
    }

    //初始化
    private void initGroupListView(final QMUIGroupListView mGroupListView) {
        foodList = new ArrayList<>();
        mainHandler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                foodList = dietService.getFoodData();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showDiet(mGroupListView);
                    }
                });
            }
        }).start();
    }

    //刷新
    private void reInitGroupListView(final QMUIGroupListView mGroupListView) {
        foodList = new ArrayList<>();
        mainHandler = new Handler(Looper.getMainLooper());
        mGroupListView.removeAllViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                foodList = dietService.getFoodData();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showDiet(mGroupListView);
                    }
                });
            }
        }).start();
    }

    //用PopUp处理操作
    private void showDiet(QMUIGroupListView mGroupListView) {
        if(foodList != null && foodList.size() != 0) {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof QMUICommonListItemView) {
                        CharSequence text = ((QMUICommonListItemView) v).getText();
                        String tmp = text.toString();
                        final Integer foodID = foodList.get(Integer.parseInt(tmp.split(":")[0]) - 1).getFoodID();
                        Toast.makeText(ROFoodListActivity.this, foodID.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            };

            QMUIGroupListView.newSection(ROFoodListActivity.this)
                    .setTitle("食物列表（只读）")
                    .setLeftIconSize(QMUIDisplayHelper.dp2px(ROFoodListActivity.this, 20), ViewGroup.LayoutParams.WRAP_CONTENT)
                    .addTo(mGroupListView);

            Integer count = 1;
            for(FoodData f : foodList) {
                QMUICommonListItemView item = mGroupListView.createItemView(count.toString() + ": " + f.getFoodName());
                item.setOrientation(QMUICommonListItemView.HORIZONTAL);
                item.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);
                item.setDetailText(f.getFoodSpecies() + "--" + f.getCalorie().toString() + "cal");
                QMUIGroupListView.newSection(ROFoodListActivity.this)
                        .addItemView(item, onClickListener)
                        .addTo(mGroupListView);
                count++;
            }

            QMUIGroupListView.newSection(ROFoodListActivity.this)
                    .setDescription("...")
                    .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(ROFoodListActivity.this, 16), 0)
                    .addTo(mGroupListView);
        }
    }
}
