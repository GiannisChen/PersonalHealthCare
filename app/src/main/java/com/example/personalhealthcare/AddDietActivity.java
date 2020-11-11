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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.personalhealthcare.PO.Diet;
import com.example.personalhealthcare.PO.FoodData;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.ServiceImpl.DietServiceImpl;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AddDietActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editMealTime;
    private EditText editFoodNumer;
    private QMUIRoundButton buttonConfirm;
    private QMUIRoundButton buttonBack;

    private Spinner spinnerFood;
    private ArrayAdapter<String> adapter;
    private Map<String, Integer> foodNameKeyID = new LinkedHashMap<>();
    private List<FoodData> foodDataList;
    private List<String> foodList = new LinkedList<>();

    private Handler mainHandler;
    private DietService dietService = new DietServiceImpl();

    private Integer UserID = new Integer(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diet);

        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sp != null) {
            UserID = new Integer(sp.getInt("UserID", 0));
            if(UserID != null)
                System.out.println("UserID:" + UserID.toString());
        }
        initView();
        getFoodData();
    }

    private void getFoodData() {
        foodDataList = new LinkedList<>();
        mainHandler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                foodDataList = dietService.getFoodData();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initSpinner();
                    }
                });
            }
        }).start();
    }

    private void initView() {
        setTitle("添加饮食记录");
        editMealTime = findViewById(R.id.add_diet_meal_time);
        editFoodNumer = findViewById(R.id.add_diet_food_number);
        spinnerFood = findViewById(R.id.add_food_spinner);
        buttonConfirm = findViewById(R.id.button_diet_confirm);
        buttonConfirm.setOnClickListener(this);
        buttonBack = findViewById(R.id.button_diet_back);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_diet_confirm:
                addDiet();
                break;
            case R.id.button_diet_back:
                finish();
                break;
        }
    }

    private void addDiet() {
        final Diet diet = new Diet();

        if(UserID == null) {
            Toast.makeText(this, "未知错误！", Toast.LENGTH_SHORT);
            finish();
            return;
        }

        diet.setDietID(0);
        diet.setUserID(UserID);

        try {
            Double foodNumber = Double.valueOf(editFoodNumer.getText().toString());
            Integer foodID = foodNameKeyID.get(spinnerFood.getSelectedItem().toString());
            String mealTime = editMealTime.getText().toString().replace("/", "-");
            Timestamp foodMealTime = Timestamp.valueOf(mealTime);
            diet.setMealTime(foodMealTime);
            if(foodID != null && foodNumber != null) {
                diet.setFoodNumber(foodNumber);
                diet.setFoodID(foodID);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = dietService.addDiet(diet);
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        } catch (Exception e) {
            Toast.makeText(this, "输入错误！", Toast.LENGTH_SHORT);
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
                        Toast.makeText(AddDietActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AddDietActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };

    private void initSpinner() {
        for(FoodData f : foodDataList) {
            foodNameKeyID.put(f.getFoodName(), f.getFoodID());
            foodList.add(f.getFoodName());
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                foodList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFood.setAdapter(adapter);
        spinnerFood.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });

        spinnerFood.setOnTouchListener(new Spinner.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        spinnerFood.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                v.setVisibility(View.VISIBLE);
            }
        });
    }
}
