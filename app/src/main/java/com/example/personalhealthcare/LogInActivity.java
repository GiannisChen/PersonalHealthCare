package com.example.personalhealthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editUserName;
    private EditText editUserPassword;
    private TextView jumpToLogUp;
    private TextView jumpToForgotPassword;
    private Button logInButton;

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
                break;
            case R.id.jump_forget_password:
                //todo
                break;
            case R.id.jump_log_up:
                //todo
                break;
        }
    }
}
