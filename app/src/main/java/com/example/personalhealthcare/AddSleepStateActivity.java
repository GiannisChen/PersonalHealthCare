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

import com.example.personalhealthcare.Dao.FeedbackDao;
import com.example.personalhealthcare.Dao.SleepStateDao;
import com.example.personalhealthcare.DaoImpl.SleepStateDaoImpl;
import com.example.personalhealthcare.PO.SleepState;
import com.example.personalhealthcare.Service.SleepStateService;
import com.example.personalhealthcare.Service.UserService;
import com.example.personalhealthcare.ServiceImpl.SleepStateServiceImpl;
import com.example.personalhealthcare.ServiceImpl.UserServiceImpl;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.regex.Pattern;

public class AddSleepStateActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText sleepDuration;
    private EditText sleepDate;
    private QMUIRoundButton buttonConfirm;
    private QMUIRoundButton buttonBack;
    private SleepStateService sleepStateService = new SleepStateServiceImpl();
    private Integer UserID = new Integer(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep_state);

        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }

        initView();
    }

    private void initView() {
        setTitle("添加睡眠记录");
        sleepDuration = findViewById(R.id.add_sleep_state_duration);
        sleepDate = findViewById(R.id.add_sleep_state_datetime);
        buttonConfirm = findViewById(R.id.button_sleep_state_confirm);
        buttonConfirm.setOnClickListener(this);
        buttonBack = findViewById(R.id.button_sleep_state_back);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sleep_state_confirm:
                addSleepState();
                break;
            case R.id.button_sleep_state_back:
                finish();
                break;
        }
    }

    private void addSleepState() {
        final SleepState sleepState = new SleepState();

        if(UserID == null) {
            Toast.makeText(this, "未知错误！", Toast.LENGTH_SHORT);
            finish();
            return;
        }

        sleepState.setSleepStateID(0);
        sleepState.setUserID(UserID);

        String duration = sleepDuration.getText().toString();
        String date = sleepDate.getText().toString();

        if(TextUtils.isEmpty(duration) ||  TextUtils.isEmpty(date) || !isDate(date) || !isTime(duration)) {
            Toast.makeText(this, "格式错误！", Toast.LENGTH_SHORT);
        }
        else {
            System.out.println("1970/01/01 " + duration);
            System.out.println(date.replace("/","-") + " 00:00:00");

            Timestamp durationTIme = Timestamp.valueOf("1970-01-01 " + duration);
            Timestamp dateTime = Timestamp.valueOf(date.replace("/","-") + " 00:00:00");
            sleepState.setSleepDate(dateTime);
            sleepState.setSleepDuration(durationTIme.getTime());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = sleepStateService.addSleepState(sleepState);
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
                        Toast.makeText(AddSleepStateActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AddSleepStateActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };

    //时间匹配
    public boolean isDate(String date){
        Pattern p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
        return p.matcher(date).matches();
    }

    public boolean isTime(String time){
        Pattern p = Pattern.compile("((((0?[0-9])|([1][0-9])|([2][0-4]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        return p.matcher(time).matches();
    }
}
