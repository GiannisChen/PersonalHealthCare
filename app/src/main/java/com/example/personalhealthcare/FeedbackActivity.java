package com.example.personalhealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.personalhealthcare.PO.Feedback;
import com.example.personalhealthcare.Service.FeedbackService;
import com.example.personalhealthcare.ServiceImpl.FeedbackServiceImpl;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.sql.Timestamp;
import java.util.Calendar;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    private Integer UserID = new Integer(0);
    private EditText editFeedbackTitle;
    private EditText editFeedbackContent;
    private QMUIRoundButton buttonConfirm;
    private QMUIRoundButton buttonBack;

    private FeedbackService feedbackService = new FeedbackServiceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }
        setTitle("问题反馈");

        initView();
    }

    private void initView() {
        editFeedbackTitle = findViewById(R.id.add_feedback_title);
        editFeedbackContent = findViewById(R.id.add_feedback_content);
        buttonConfirm = findViewById(R.id.button_feedback_confirm);
        buttonConfirm.setOnClickListener(this);
        buttonBack = findViewById(R.id.button_feedback_back);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_feedback_confirm:
                addFeedback();
                break;
            case R.id.button_feedback_back:
                finish();
                break;
        }
    }

    private void addFeedback() {
        final Feedback feedback = new Feedback();
        String title = editFeedbackTitle.getText().toString();
        String content = editFeedbackContent.getText().toString();
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && UserID != null) {

            feedback.setQuestionID(0);
            feedback.setIsResolved(0);
            feedback.setUserID(UserID);
            feedback.setQuestionTitle(title);
            feedback.setQuestionContent(content);
            feedback.setInquiryTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = feedbackService.addFeedback(feedback);
                    handler.sendMessage(msg);
                }
            }).start();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                //处理get
                case 0:
                    Boolean flag = (Boolean)msg.obj;
                    if(!flag) {
                        Toast.makeText(FeedbackActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(FeedbackActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };
}
