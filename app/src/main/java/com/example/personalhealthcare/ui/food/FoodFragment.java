package com.example.personalhealthcare.ui.food;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.personalhealthcare.AddDietActivity;
import com.example.personalhealthcare.AddFoodActivity;
import com.example.personalhealthcare.PO.FoodData;
import com.example.personalhealthcare.R;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.ServiceImpl.DietServiceImpl;
import com.example.personalhealthcare.UpdateFoodActivity;
import com.example.personalhealthcare.VO.DietVO;
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

public class FoodFragment extends Fragment {

    private FoodViewModel foodViewModel;

    private QMUIGroupListView mGroupListView;
    private Handler mainHandler;
    private DietService dietService = new DietServiceImpl();
    private QMUITopBarLayout mTopBar;
    private List<FoodData> foodList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodViewModel =
                ViewModelProviders.of(this).get(FoodViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food, container, false);

        mGroupListView = root.findViewById(R.id.food_groupListView);
        mTopBar = root.findViewById(R.id.topbar);

        initGroupListView(mGroupListView);
        initTopBar();

        return root;
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

    private void initTopBar() {
        mTopBar.addRightImageButton(R.drawable.ic_add_black_24dp, R.id.qmui_topbar_item_left_back)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AddFoodActivity.class);
                        startActivity(intent);
                    }
                });

        mTopBar.setTitle("食物管理");
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
                        Toast.makeText(getContext(), foodID.toString(), Toast.LENGTH_SHORT).show();
                        QMUIPopups.quickAction(getContext(),
                                QMUIDisplayHelper.dp2px(getContext(), 56),
                                QMUIDisplayHelper.dp2px(getContext(), 56))
                                .shadow(true)
                                .skinManager(QMUISkinManager.defaultInstance(getContext()))
                                .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 20))
                                .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_delete).text("删除").onClick(
                                        new QMUIQuickAction.OnClickListener() {
                                            @Override
                                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                                quickAction.dismiss();
                                                deleteFood(foodID);
                                            }
                                        }
                                ))
                                .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_change).text("修改").onClick(
                                        new QMUIQuickAction.OnClickListener() {
                                            @Override
                                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                                quickAction.dismiss();
                                                updateFood(foodID);
                                            }
                                        }
                                ))
                                .show(v);
                    }
                }
            };

            QMUIGroupListView.newSection(getContext())
                    .setTitle("点击编辑")
                    .setLeftIconSize(QMUIDisplayHelper.dp2px(getContext(), 20), ViewGroup.LayoutParams.WRAP_CONTENT)
                    .addTo(mGroupListView);

            Integer count = 1;
            for(FoodData f : foodList) {
                QMUICommonListItemView item = mGroupListView.createItemView(count.toString() + ": " + f.getFoodName());
                item.setOrientation(QMUICommonListItemView.HORIZONTAL);
                item.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);
                item.setDetailText(f.getFoodSpecies() + "--" + f.getCalorie().toString() + "cal");
                QMUIGroupListView.newSection(getContext())
                        .addItemView(item, onClickListener)
                        .addTo(mGroupListView);
                count++;
            }

            QMUIGroupListView.newSection(getContext())
                    .setDescription("...")
                    .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                    .addTo(mGroupListView);
        }
    }

    private void deleteFood(final Integer foodID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = dietService.deleteFood(foodID);
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void updateFood(final Integer foodID) {
        Intent intent = new Intent(getActivity(), UpdateFoodActivity.class);
        intent.putExtra("FoodID", foodID);
        startActivity(intent);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what) {
                //处理get
                case 0:
                    Boolean flag = (Boolean)msg.obj;
                    if(flag) {
                        Toast.makeText(getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                        reInitGroupListView(mGroupListView);
                    }
                    else {
                        Toast.makeText(getContext(), "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}