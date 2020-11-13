package com.example.personalhealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.personalhealthcare.CommonUtils.ImageUtils;
import com.example.personalhealthcare.PO.FoodData;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.ServiceImpl.DietServiceImpl;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.IOException;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class UpdateFoodActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editFoodName;
    private EditText editFoodCalories;
    private Spinner spinnerSpecies;
    private QMUIRoundButton buttonConfirm;
    private QMUIRoundButton buttonBack;
    private ImageButton buttonFoodImage;

    private Handler mainHandler;

    private DietService dietService = new DietServiceImpl();

    private Integer FoodID = new Integer(0);
    private Bitmap foodImage = null;

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
        buttonFoodImage = findViewById(R.id.button_food_image);
        buttonFoodImage.setOnClickListener(this);

        mainHandler = new Handler(Looper.getMainLooper());

        spinnerSpecies = findViewById(R.id.update_food_spinner);
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
        else {
            Toast.makeText(this, "未知错误！", Toast.LENGTH_SHORT).show();
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
            case R.id.button_food_image:
                getPhoto();
                break;
        }
    }

    private void updateFoodData() {
        String foodName = editFoodName.getText().toString();
        String foodSpecies = spinnerSpecies.getSelectedItem().toString();
        Double foodCalories = Double.parseDouble(editFoodCalories.getText().toString());

        if(!TextUtils.isEmpty(foodName) && !TextUtils.isEmpty(foodSpecies) && foodCalories != null && foodCalories >= 0) {
            foodData.setFoodName(foodName);
            foodData.setFoodSpecies(foodSpecies);
            foodData.setCalorie(foodCalories);
            foodData.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));

            if(foodImage != null) {
                foodData.setImage(ImageUtils.Bitmap2Bytes(foodImage));
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = dietService.updateFoodData(foodData);
                    handler.sendMessage(msg);
                }
            }).start();
        }
        else {
            Toast.makeText(this, "请填写完整！", Toast.LENGTH_SHORT).show();
        }
    }

    private void fillSpinner(FoodData foodData) {
        List<String> species = new ArrayList<>(Arrays.asList("肉类", "蔬菜", "水果", "主食", "干果", "水产", "饮品"));
        editFoodName.setText(foodData.getFoodName());
        editFoodCalories.setText(foodData.getCalorie().toString());

        buttonFoodImage.setImageBitmap(ImageUtils.Bytes2Bitmap(foodData.getImage()));

        int index = species.indexOf(foodData.getFoodSpecies());
        spinnerSpecies.setSelection(index >= 0 ? index : 0);
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    buttonFoodImage.setImageBitmap(bitmap);
                    foodImage = bitmap;
                } catch (IOException e) {
                    Toast.makeText(this, "图片未找到！", Toast.LENGTH_SHORT).show();
                }
            }
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
