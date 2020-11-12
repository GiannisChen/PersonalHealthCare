package com.example.personalhealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.personalhealthcare.PO.FoodData;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.ServiceImpl.DietServiceImpl;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UpdateFoodActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editFoodName;
    private EditText editFoodCalories;
    private Spinner spinnerSpecies;
    private QMUIRoundButton buttonConfirm;
    private QMUIRoundButton buttonBack;
    private Handler mainHandler;

    private DietService dietService = new DietServiceImpl();

    private Integer FoodID = new Integer(0);

    private FoodData foodData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        FoodID = getIntent().getIntExtra("FoodID", 0);

        initView();
    }

    private void initView() {

        setTitle("修改食品信息");

        editFoodName = findViewById(R.id.update_food_name);
        editFoodCalories = findViewById(R.id.update_food_calories);
        spinnerSpecies = findViewById(R.id.update_food_spinner);

        buttonConfirm = findViewById(R.id.button_update_food_confirm);
        buttonConfirm.setOnClickListener(this);
        buttonBack = findViewById(R.id.button_update_food_back);
        buttonBack.setOnClickListener(this);

        mainHandler = new Handler(Looper.getMainLooper());

        spinnerSpecies = findViewById(R.id.add_food_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.species,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecies.setAdapter(adapter);

        getFoodData();
    }

    private void getFoodData() {
        if(FoodID != null && FoodID != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    foodData = dietService.getFoodDataByID(FoodID);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            fillSpinner(foodData);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_update_food_confirm:
                updateFoodData();
                break;
            case R.id.button_update_food_back:
                finish();
                break;
        }
    }

    private void updateFoodData() {

    }

    private void fillSpinner(FoodData foodData) {
        List<String> species = new ArrayList<>(Arrays.asList("肉类", "蔬菜", "水果", "主食", "干果", "水产", "饮品"));
        editFoodName.setHint(foodData.getFoodName());
        editFoodCalories.setText(foodData.getFoodSpecies());
        //todo
        //spinnerSpecies.setSelection(species.);
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
                        Toast.makeText(UpdateFoodActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(UpdateFoodActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };
}
