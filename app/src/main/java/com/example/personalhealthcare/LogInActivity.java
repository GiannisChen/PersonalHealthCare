package com.example.personalhealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalhealthcare.PO.User;
import com.example.personalhealthcare.Service.UserService;
import com.example.personalhealthcare.ServiceImpl.UserServiceImpl;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editUserName;
    private EditText editUserPassword;
    private TextView jumpToLogUp;
    private TextView jumpToForgotPassword;
    private Button logInButton;
    private UserService userService = new UserServiceImpl();

    private String userName;
    private String userPassword;

    private Integer UserID = new Integer(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_log_in);

        initView();
    }

    private void initView() {
        editUserName = findViewById(R.id.nameText);
        editUserPassword = findViewById(R.id.pwdText);
        jumpToLogUp = findViewById(R.id.jump_log_up);
        jumpToLogUp.setOnClickListener(this);
        jumpToForgotPassword = findViewById(R.id.jump_forget_password);
        jumpToForgotPassword.setOnClickListener(this);
        logInButton = findViewById(R.id.mBtnLogin);
        logInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtnLogin:
                //todo
                System.out.println("login");
                logIn();
                break;
            case R.id.jump_forget_password:
                //todo
                break;
            case R.id.jump_log_up:
                //todo
                Intent intent = new Intent(this, LogUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void logIn() {
        userName = editUserName.getText().toString();
        userPassword = editUserPassword.getText().toString();

        if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPassword)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = userService.getUser(userName);
                    handler.sendMessage(msg);
                }
            }).start();
        }
        else {
            Toast.makeText(LogInActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                //处理get
                case 0:
                    User user = (User)msg.obj;
                    if(user == null) {
                        Toast.makeText(LogInActivity.this, "用户名错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(user.getUserPassword().equals(userPassword)) {
                            SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                            sp.edit().putInt("UserID", user.getUserID()).commit();
                            Toast.makeText(LogInActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            if("User".equals(user.getUserType())) {
                                Intent intent = new Intent(LogInActivity.this, OrdinaryUserActivity.class);
                                startActivity(intent);
                            }
                            else if ("Admin".equals(user.getUserType())) {
                                Intent intent = new Intent(LogInActivity.this, AdminActivity.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            Toast.makeText(LogInActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };
}
