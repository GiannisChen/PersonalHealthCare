package com.example.personalhealthcare.ui.adminSettings;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.personalhealthcare.ChangePasswordActivity;
import com.example.personalhealthcare.FigureDataActivity;
import com.example.personalhealthcare.R;
import com.example.personalhealthcare.ui.settings.SettingsViewModel;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

public class AdminSettingsFragment extends Fragment {

    private QMUIGroupListView mGroupListView;

    private AdminSettingsViewModel adminSettingsViewModel;

    private QMUITopBarLayout mTopBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminSettingsViewModel =
                ViewModelProviders.of(this).get(AdminSettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_settings, container, false);

        mGroupListView = root.findViewById(R.id.admin_settings_groupListView);
        initGroupListView(mGroupListView);

        mTopBar = root.findViewById(R.id.topbar);
        mTopBar.setTitle("个人信息和设置");

        return root;
    }

    private void initGroupListView(QMUIGroupListView mGroupListView) {

        //身体数据
        QMUICommonListItemView itemFigureData = mGroupListView.createItemView("添加新的管理员");
        itemFigureData.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        //修改密码
        QMUICommonListItemView itemChangePSWD = mGroupListView.createItemView("修改密码");
        itemChangePSWD.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        //注销用户
        QMUICommonListItemView itemDeleteUser = mGroupListView.createItemView("注销用户");
        itemDeleteUser.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemDeleteUser.getTextView().setTextColor(getResources().getColor(R.color.qmui_config_color_red));

        //按钮监听（3）
        //jump to add new admin
        View.OnClickListener toAddNewAdminListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = ((QMUICommonListItemView) v).getText();
                //Intent intent = new Intent(getActivity(), FigureDataActivity.class);
                //startActivity(intent);
            }
        };

        //jump to password_change
        View.OnClickListener toChangePasswordListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = ((QMUICommonListItemView) v).getText();
                Toast.makeText(getActivity(), text + " is Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        };

        //delete user
        View.OnClickListener deleteUserListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(getActivity(), text + " is Clicked", Toast.LENGTH_SHORT).show();
                    if (((QMUICommonListItemView) v).getAccessoryType() == QMUICommonListItemView.ACCESSORY_TYPE_SWITCH) {
                        ((QMUICommonListItemView) v).getSwitch().toggle();
                    }
                }
            }
        };

        QMUIGroupListView.newSection(getContext())
                .setUseDefaultTitleIfNone(false)
                .setUseTitleViewForSectionSpace(false)
                .setTitle("个人信息和设置")
                .setDescription("...")
                .setLeftIconSize(QMUIDisplayHelper.dp2px(getContext(), 20), ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemFigureData, toAddNewAdminListener)
                .addItemView(itemChangePSWD, toChangePasswordListener)
                .addItemView(itemDeleteUser, onClickListener)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                .addTo(mGroupListView);
    }
}