package com.example.personalhealthcare.ui.adminHome;

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
import com.example.personalhealthcare.LogUpActivity;
import com.example.personalhealthcare.PO.User;
import com.example.personalhealthcare.R;
import com.example.personalhealthcare.Service.UserService;
import com.example.personalhealthcare.ServiceImpl.UserServiceImpl;
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
import java.util.LinkedList;
import java.util.List;

public class AdminHomeFragment extends Fragment {

    private AdminHomeViewModel adminHomeViewModel;
    private List<User> ordinaryUsers;
    private QMUIGroupListView mGroupListView;
    private UserService userService = new UserServiceImpl();
    private Handler mainHandler;
    private QMUITopBarLayout mTopBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminHomeViewModel =
                ViewModelProviders.of(this).get(AdminHomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_home, container, false);

        mGroupListView = root.findViewById(R.id.admin_users_groupListView);
        mTopBar = root.findViewById(R.id.topbar);

        initGroupListView(mGroupListView);
        initTopBar();

        return root;
    }

    private void initGroupListView(final QMUIGroupListView mGroupListView) {
        ordinaryUsers = new ArrayList<>();
        mainHandler  = new Handler((Looper.getMainLooper()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                ordinaryUsers = userService.findOrdinary();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showUsers(mGroupListView);
                    }
                });
            }
        }).start();
    }

    private void reInitGroupListView(final QMUIGroupListView mGroupListView) {
        ordinaryUsers = new ArrayList<>();
        mainHandler  = new Handler((Looper.getMainLooper()));
        mGroupListView.removeAllViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ordinaryUsers = userService.findOrdinary();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showUsers(mGroupListView);
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
                        Intent intent = new Intent(getActivity(), LogUpActivity.class);
                        startActivity(intent);
                    }
                });

        mTopBar.setTitle("普通用户管理");
    }

    //用PopUp处理操作
    private void showUsers(QMUIGroupListView mGroupListView) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(ordinaryUsers != null && ordinaryUsers.size() != 0) {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof QMUICommonListItemView) {
                        CharSequence text = ((QMUICommonListItemView) v).getText();
                        String tmp = text.toString();
                        final Integer userID = ordinaryUsers.get(Integer.parseInt(tmp.split(":")[0]) - 1).getUserID();
                        Toast.makeText(getContext(), userID.toString(), Toast.LENGTH_SHORT).show();
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
                                                deleteUser(userID);
                                            }
                                        }
                                ))
                                .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_change).text("设为管理").onClick(
                                        new QMUIQuickAction.OnClickListener() {
                                            @Override
                                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                                quickAction.dismiss();
                                                updateUser(userID);
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
            for(User u: ordinaryUsers) {
                QMUICommonListItemView item = mGroupListView.createItemView(count.toString() + ": " + u.getUserName());
                item.setOrientation(QMUICommonListItemView.VERTICAL);
                item.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);
                item.setDetailText("注册时间：" + time.format(u.getRegisterTime()));
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

    private void deleteUser(final Integer userID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = userService.deleteUser(userID);
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void updateUser(final Integer userID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = userService.setAdmin(userID);
                handler.sendMessage(msg);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            Boolean flag;
            switch(msg.what) {
                //处理get
                case 0:
                    flag = (Boolean)msg.obj;
                    if(flag) {
                        Toast.makeText(getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                        reInitGroupListView(mGroupListView);
                    }
                    else {
                        Toast.makeText(getContext(), "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 1:
                    flag = (Boolean)msg.obj;
                    if(flag) {
                        Toast.makeText(getContext(), "设置管理员成功！", Toast.LENGTH_SHORT).show();
                        reInitGroupListView(mGroupListView);
                    }
                    else {
                        Toast.makeText(getContext(), "设置管理员失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}