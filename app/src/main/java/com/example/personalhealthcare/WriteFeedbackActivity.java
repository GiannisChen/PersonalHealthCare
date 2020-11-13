package com.example.personalhealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalhealthcare.PO.Feedback;
import com.example.personalhealthcare.Service.FeedbackService;
import com.example.personalhealthcare.ServiceImpl.FeedbackServiceImpl;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.sql.Timestamp;
import java.util.Calendar;

public class WriteFeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    private Integer questionID = new Integer(0);
    private Integer UserID = new Integer(0);

    private EditText editFeedback;
    private TextView textFeedbackTitle;
    private TextView textFeedbackContent;
    private QMUIRoundButton buttonConfirm;
    private QMUIRoundButton buttonBack;
    private Handler mainHandler;

    private Feedback feedback = null;

    private FeedbackService feedbackService = new FeedbackServiceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_feedback);

        questionID = getIntent().getIntExtra("QuestionID", 0);
        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }

        initView();
    }

    private void initView() {

        setTitle("写回复");

        editFeedback = findViewById(R.id.update_feedback_content);
        textFeedbackTitle = findViewById(R.id.feedback_title);
        textFeedbackContent = findViewById(R.id.feedback_content);
        buttonConfirm = findViewById(R.id.button_feedback_confirm);
        buttonConfirm.setOnClickListener(this);
        buttonBack = findViewById(R.id.button_feedback_back);
        buttonBack.setOnClickListener(this);

        mainHandler = new Handler(Looper.getMainLooper());

        getFeedback();
    }

    private void getFeedback() {
        if(questionID != null && questionID != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    feedback = feedbackService.getFeedbackByID(questionID);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(feedback != null) {
                                textFeedbackTitle.setText(feedback.getQuestionTitle());
                                textFeedbackContent.setText(feedback.getQuestionContent());
                            }
                            else {
                                Toast.makeText(WriteFeedbackActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).start();
        }
        else {
            Toast.makeText(this, "未知错误！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_feedback_confirm:
                updateFeedback();
                break;
            case R.id.button_feedback_back:
                finish();
                break;
        }
    }

    private void updateFeedback() {
        String feedbackContent = editFeedback.getText().toString();
        if(!TextUtils.isEmpty(feedbackContent) && UserID != 0 && feedback != null) {
            feedback.setFeedbackContent(feedbackContent);
            feedback.setIsResolved(1);
            feedback.setReplierID(UserID);
            feedback.setFeedbackTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = feedbackService.updateFeedback(feedback);
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
                        Toast.makeText(WriteFeedbackActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(WriteFeedbackActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };
}
