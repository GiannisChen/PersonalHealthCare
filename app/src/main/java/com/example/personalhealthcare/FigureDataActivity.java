package com.example.personalhealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.personalhealthcare.Dao.UserDao;
import com.example.personalhealthcare.DaoImpl.UserDaoImpl;
import com.example.personalhealthcare.Service.UserService;
import com.example.personalhealthcare.ServiceImpl.UserServiceImpl;
import com.example.personalhealthcare.VO.OrdinaryUser;
import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

public class FigureDataActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editUserName;
    private EditText editUserEmail;
    private EditText editUserID;
    private EditText editUserAge;
    private EditText editUserSature;
    private EditText editUserWeight;
    private EditText editUserBP;
    private QMUIRoundButton buttonConfirm;
    private QMUIRoundButton buttonBack;
    private UserService userService = new UserServiceImpl();
    private OrdinaryUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figure_data);
        initView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_figure_confirm:
                figureConfirm();
                break;
            case R.id.button_figure_back:
                finish();
                break;
        }
    }

    private void initView() {
        setTitle("个人数据");
        editUserName = findViewById(R.id.figure_user_name);
        editUserEmail = findViewById(R.id.figure_user_email);
        editUserID = findViewById(R.id.figure_user_id);
        editUserAge = findViewById(R.id.figure_user_age);
        editUserSature = findViewById(R.id.figure_user_sature);
        editUserWeight = findViewById(R.id.figure_user_weight);
        editUserBP = findViewById(R.id.figure_user_bp);
        buttonConfirm = findViewById(R.id.button_figure_confirm);
        buttonConfirm.setOnClickListener(this);
        buttonBack = findViewById(R.id.button_figure_back);
        buttonBack.setOnClickListener(this);

        getPersonalDetail();
    }

    //用户信息修改提交
    private void figureConfirm() {
        if(user == null) {
            Toast.makeText(FigureDataActivity.this, "更新失败，稍后重试！", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            user.getUserData().setUserAge(Integer.parseInt(editUserAge.getText().toString()));
            user.getUserData().setUserSature(Double.parseDouble(editUserSature.getText().toString()));
            user.getUserData().setUserWeight(Double.parseDouble(editUserWeight.getText().toString()));
            user.getUserData().setUserBP(Double.parseDouble(editUserBP.getText().toString()));
            System.out.println(user);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = userService.updateOrdinary(user);
                    handler.sendMessage(msg);
                }
            }).start();
        }
    }

    //查询该用户信息与消息处理
    private void getPersonalDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrdinaryUser user = userService.getPersonalDetail(4);
                System.out.println(user);
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = user;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what) {
                //处理get
                case 0:
                    user = (OrdinaryUser)msg.obj;
                    if(user != null) {
                        editUserName.setText(user.getBaseInfo().getUserName());
                        editUserEmail.setText(user.getBaseInfo().getUserEmail());
                        editUserID.setText(user.getBaseInfo().getUserID().toString());
                        editUserAge.setText(user.getUserData().getUserAge().toString());
                        editUserSature.setText(user.getUserData().getUserSature().toString());
                        editUserWeight.setText(user.getUserData().getUserWeight().toString());
                        editUserBP.setText(user.getUserData().getUserBP().toString());
                    }
                    break;
                //处理update
                case 1:
                    Boolean flag = (Boolean) msg.obj;
                    if(flag) {
                        Toast.makeText(FigureDataActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(FigureDataActivity.this, "更新失败，稍后重试！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };
}
