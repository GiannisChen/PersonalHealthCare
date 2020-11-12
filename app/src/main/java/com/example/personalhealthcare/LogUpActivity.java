package com.example.personalhealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.personalhealthcare.PO.OrdinaryUserData;
import com.example.personalhealthcare.PO.User;
import com.example.personalhealthcare.Service.UserService;
import com.example.personalhealthcare.ServiceImpl.UserServiceImpl;
import com.example.personalhealthcare.VO.OrdinaryUser;

import java.sql.Timestamp;
import java.util.Calendar;

public class LogUpActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editUserName;
    private EditText editUserPassword;
    private EditText editPasswordConfirm;
    private EditText editUserEmail;
    private EditText editUserSature;
    private EditText editUserWeight;
    private EditText editUserAge;
    private EditText editUserBP;
    private Button buttonLogUp;

    private UserService userService = new UserServiceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_log_up);

        initView();
    }

    private void initView() {
        editUserName = findViewById(R.id.log_up_user_name);
        editUserEmail = findViewById(R.id.log_up_user_email);
        editUserPassword = findViewById(R.id.log_up_password);
        editPasswordConfirm = findViewById(R.id.log_up_password_confirm);

        editUserAge = findViewById(R.id.log_up_age);
        editUserSature = findViewById(R.id.log_up_sature);
        editUserWeight = findViewById(R.id.log_up_weight);
        editUserBP = findViewById(R.id.log_up_BP);
        buttonLogUp = findViewById(R.id.log_up_button);
        buttonLogUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_up_button:
                logUp();
                break;
        }
    }

    private void logUp() {
        String userName = editUserName.getText().toString();
        String userEmail = editUserEmail.getText().toString();
        String userPassword = editUserPassword.getText().toString();
        String passwordConfirm = editPasswordConfirm.getText().toString();

        try {
            Integer userAge = Integer.parseInt(editUserAge.getText().toString());
            final Double userSature = Double.parseDouble(editUserSature.getText().toString());
            Double userWeight = Double.parseDouble(editUserWeight.getText().toString());
            Double userBP = Double.parseDouble(editUserBP.getText().toString());
            if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)
            || TextUtils.isEmpty(passwordConfirm)) {
                Toast.makeText(this, "请完整填写数据！", Toast.LENGTH_SHORT).show();
            }
            else if(!userPassword.equals(passwordConfirm)){
                Toast.makeText(this, "确认密码不正确！", Toast.LENGTH_SHORT).show();
            }
            else {
                final OrdinaryUser user = new OrdinaryUser();
                user.setBaseInfo(new User(0,
                        userEmail,
                        userName,
                        userPassword,
                        new Timestamp(Calendar.getInstance().getTimeInMillis()),
                        "User"));
                user.setUserData(new OrdinaryUserData(
                        0,
                        userSature,
                        userWeight,
                        userBP,
                        userAge
                ));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = userService.addOrdinaryUser(user);
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        }catch (Exception e) {
            Toast.makeText(this, "格式错误！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LogUpActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LogUpActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };
}
