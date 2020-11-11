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

import com.example.personalhealthcare.Service.UserService;
import com.example.personalhealthcare.ServiceImpl.UserServiceImpl;
import com.example.personalhealthcare.VO.OrdinaryUser;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editConfirmedPassword;
    private QMUIRoundButton buttonConfirm;
    private QMUIRoundButton buttonBack;
    private UserService userService = new UserServiceImpl();
    private Integer UserID = new Integer(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_password_confirm:
                passwordConfirm();
                break;
            case R.id.button_password_back:
                finish();
                break;
        }
    }

    private void initView() {
        setTitle("修改密码");
        editOldPassword = findViewById(R.id.change_password_old);
        editNewPassword = findViewById(R.id.change_password_new);
        editConfirmedPassword = findViewById(R.id.change_password_confirm);
        buttonConfirm = findViewById(R.id.button_password_confirm);
        buttonConfirm.setOnClickListener(this);
        buttonBack = findViewById(R.id.button_password_back);
        buttonBack.setOnClickListener(this);
    }

    private void passwordConfirm() {
        final String oldPassword = editOldPassword.getText().toString();
        final String newPassword = editNewPassword.getText().toString();

        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }

        String confirmedPassword = editConfirmedPassword.getText().toString();
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmedPassword)) {
            Toast.makeText(ChangePasswordActivity.this, "请填写完整!", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmedPassword)) {
            Toast.makeText(ChangePasswordActivity.this, "新密码不一致！", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = userService.updatePassword(UserID, oldPassword, newPassword);
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
                        Toast.makeText(ChangePasswordActivity.this, "旧密码错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ChangePasswordActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };
}
