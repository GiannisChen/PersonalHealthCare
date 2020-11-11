package com.example.personalhealthcare.ui.Diet;

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
import com.example.personalhealthcare.AddSleepStateActivity;
import com.example.personalhealthcare.PO.SleepState;
import com.example.personalhealthcare.R;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.ServiceImpl.DietServiceImpl;
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

public class DietFragment extends Fragment {

    private QMUIGroupListView mGroupListView;
    private DietViewModel dietViewModel;
    private List<DietVO> dietList;
    private Handler mainHandler;
    private DietService dietService = new DietServiceImpl();
    private QMUITopBarLayout mTopBar;
    private Integer UserID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dietViewModel =
                ViewModelProviders.of(this).get(DietViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diet, container, false);

        mGroupListView = root.findViewById(R.id.diet_groupListView);
        mTopBar = root.findViewById(R.id.topbar);

        SharedPreferences sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }

        initGroupListView(mGroupListView);
        initTopBar();

        return root;
    }

    //初始化
    private void initGroupListView(final QMUIGroupListView mGroupListView) {
        dietList = new ArrayList<>();
        mainHandler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                dietList = dietService.getDietByID(UserID);
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
                        Intent intent = new Intent(getActivity(), AddDietActivity.class);
                        startActivity(intent);
                    }
                });

        mTopBar.setTitle("饮食记录");
    }

    //刷新
    private void reInitGroupListView(final QMUIGroupListView mGroupListView) {
        dietList = new ArrayList<>();
        mainHandler = new Handler(Looper.getMainLooper());
        mGroupListView.removeAllViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dietList = dietService.getDietByID(UserID);
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
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(dietList != null && dietList.size() != 0) {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof QMUICommonListItemView) {
                        CharSequence text = ((QMUICommonListItemView) v).getText();
                        String tmp = text.toString();
                        final Integer dietID = dietList.get(Integer.parseInt(tmp.split(":")[0]) - 1).getDietID();
                        Toast.makeText(getContext(), dietID.toString(), Toast.LENGTH_SHORT).show();
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
                                                deleteDiet(dietID);
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
            for(DietVO d : dietList) {
                QMUICommonListItemView item = mGroupListView.createItemView(count.toString() + ": " + time.format(d.getMealTime()));
                item.setOrientation(QMUICommonListItemView.HORIZONTAL);
                item.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);
                item.setDetailText(d.getFoodName() + d.getFoodNumber().toString() + "g");
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

    private void deleteDiet(final Integer dietID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = dietService.deleteDiet(dietID);
                handler.sendMessage(msg);
            }
        }).start();
    };

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