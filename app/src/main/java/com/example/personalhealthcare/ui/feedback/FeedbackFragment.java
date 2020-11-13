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
import com.example.personalhealthcare.PO.Feedback;
import com.example.personalhealthcare.R;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.Service.FeedbackService;
import com.example.personalhealthcare.ServiceImpl.DietServiceImpl;
import com.example.personalhealthcare.ServiceImpl.FeedbackServiceImpl;
import com.example.personalhealthcare.VO.DietVO;
import com.example.personalhealthcare.WriteFeedbackActivity;
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

    private QMUIGroupListView mGroupListView;
    private QMUITopBarLayout mTopBar;

    private Handler mainHandler;
    private DietViewModel dietViewModel;
    private FeedbackService feedbackService = new FeedbackServiceImpl();

    private List<Feedback> feedbackList;
    private Integer UserID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dietViewModel =
                ViewModelProviders.of(this).get(DietViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }

        mGroupListView = root.findViewById(R.id.admin_feedback_groupListView);
        mTopBar = root.findViewById(R.id.topbar);

        initGroupListView(mGroupListView);

        mTopBar.setTitle("反馈管理");

        return root;
    }

    private void initGroupListView(final QMUIGroupListView mGroupListView) {
        feedbackList = new ArrayList<>();
        mainHandler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                feedbackList = feedbackService.getAllUnresolvedFeedback();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showFeedback(mGroupListView);
                    }
                });
            }
        }).start();
    }

    private void reInitGroupListView(final QMUIGroupListView mGroupListView) {
        feedbackList = new ArrayList<>();
        mainHandler = new Handler(Looper.getMainLooper());
        mGroupListView.removeAllViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                feedbackList = feedbackService.getAllUnresolvedFeedback();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showFeedback(mGroupListView);
                    }
                });
            }
        }).start();
    }

    //用PopUp处理操作
    private void showFeedback(QMUIGroupListView mGroupListView) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(feedbackList != null && feedbackList.size() != 0) {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof QMUICommonListItemView) {
                        CharSequence text = ((QMUICommonListItemView) v).getText();
                        String tmp = text.toString();
                        final Integer questionID = feedbackList.get(Integer.parseInt(tmp.split(":")[0]) - 1).getQuestionID();
                        Toast.makeText(getContext(), questionID.toString(), Toast.LENGTH_SHORT).show();
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
                                                deleteFeedback(questionID);
                                            }
                                        }
                                ))
                                .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_feedback).text("回复").onClick(
                                        new QMUIQuickAction.OnClickListener() {
                                            @Override
                                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                                quickAction.dismiss();
                                                updateFeedback(questionID);
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
            for(Feedback f : feedbackList) {
                QMUICommonListItemView item = mGroupListView.createItemView(count.toString() + ": " + f.getQuestionTitle());
                item.setOrientation(QMUICommonListItemView.HORIZONTAL);
                item.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);
                item.setDetailText(time.format(f.getInquiryTime()));
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

    private void deleteFeedback(final Integer questionID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = feedbackService.deleteFeedback(questionID);
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void updateFeedback(final Integer questionID) {
        Intent intent = new Intent(getActivity(), WriteFeedbackActivity.class);
        intent.putExtra("QuestionID", questionID);
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