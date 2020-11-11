package com.example.personalhealthcare.ui.SleepState;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.personalhealthcare.AddSleepStateActivity;
import com.example.personalhealthcare.OrdinaryUserActivity;
import com.example.personalhealthcare.PO.SleepState;
import com.example.personalhealthcare.R;
import com.example.personalhealthcare.Service.SleepStateService;
import com.example.personalhealthcare.ServiceImpl.SleepStateServiceImpl;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SleepStateFragment extends Fragment {

    private QMUIGroupListView mGroupListView;
    private SleepStateViewModel sleepStateViewModel;
    private List<SleepState> stateList;
    private Handler mainHandler;
    private SleepStateService sleepStateService = new SleepStateServiceImpl();
    private QMUITopBarLayout mTopBar;
    private Integer UserID = new Integer(0);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sleepStateViewModel =
                ViewModelProviders.of(this).get(SleepStateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sleep_state, container, false);

        mGroupListView = root.findViewById(R.id.sleep_state_groupListView);
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
        stateList = new ArrayList<>();
        mainHandler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                stateList = sleepStateService.getSleepStateByID(UserID);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showSleepState(mGroupListView);
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
                        Toast.makeText(getContext(), "fuck", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), AddSleepStateActivity.class);
                        startActivity(intent);
                    }
                });

        mTopBar.setTitle("睡眠记录");
    }

    //刷新
    private void reInitGroupListView(final QMUIGroupListView mGroupListView) {
        stateList = new ArrayList<>();
        mainHandler = new Handler(Looper.getMainLooper());
        mGroupListView.removeAllViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                stateList = sleepStateService.getSleepStateByID(4);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showSleepState(mGroupListView);
                    }
                });
            }
        }).start();
    }

    //用PopUp处理操作
    private void showSleepState(QMUIGroupListView mGroupListView) {
        if(stateList != null && stateList.size() != 0) {
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat duraion = new SimpleDateFormat("时长 HH小时 mm分 ss秒");

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof QMUICommonListItemView) {
                        CharSequence text = ((QMUICommonListItemView) v).getText();
                        String tmp = text.toString();
                        final Integer sleepStateID = stateList.get(Integer.parseInt(tmp.split(":")[0]) - 1).getSleepStateID();
                        Toast.makeText(getContext(), sleepStateID.toString(), Toast.LENGTH_SHORT).show();
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
                                                deleteSleepState(sleepStateID);
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
            for(SleepState s : stateList) {
                QMUICommonListItemView item = mGroupListView.createItemView(count.toString() + ": " + time.format(s.getSleepDate()));
                item.setOrientation(QMUICommonListItemView.HORIZONTAL);
                item.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);
                item.setDetailText(duraion.format(s.getSleepDuration()));
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

    private void deleteSleepState(final Integer SleepStateID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = sleepStateService.deleteSleepState(SleepStateID);
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