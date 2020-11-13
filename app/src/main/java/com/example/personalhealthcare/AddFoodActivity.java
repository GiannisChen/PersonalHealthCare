package com.example.personalhealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import java.sql.Timestamp;
import java.util.Calendar;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editFoodName;
    private EditText editFoodCalories;
    private Spinner spinnerSpecies;
    private QMUIRoundButton buttonConfirm;
    private QMUIRoundButton buttonBack;
    private ImageButton buttonFoodImage;
    private Bitmap foodImage = null;

    private DietService dietService = new DietServiceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        initView();
    }

    private void initView() {

        setTitle("添加食品信息");

        editFoodName = findViewById(R.id.add_food_name);
        editFoodCalories = findViewById(R.id.add_food_calories);

        buttonConfirm = findViewById(R.id.button_add_food_confirm);
        buttonConfirm.setOnClickListener(this);
        buttonBack = findViewById(R.id.button_add_food_back);
        buttonBack.setOnClickListener(this);
        buttonFoodImage = findViewById(R.id.button_food_image);
        buttonFoodImage.setOnClickListener(this);

        spinnerSpecies = findViewById(R.id.add_food_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.species,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecies.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_food_confirm:
                addFoodData();
                break;
            case R.id.button_add_food_back:
                finish();
                break;
            case R.id.button_food_image:
                getPhoto();
                break;
        }
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

    private void addFoodData() {
        String foodName = editFoodName.getText().toString();
        Double foodCalories = Double.parseDouble(editFoodCalories.getText().toString());
        String foodSpecies = spinnerSpecies.getSelectedItem().toString();

        if(!TextUtils.isEmpty(foodName) && !TextUtils.isEmpty(foodSpecies)) {

            final FoodData foodData;
            if(foodImage == null) {
                foodData = new FoodData(
                        0,
                        foodName,
                        foodSpecies,
                        foodCalories,
                        new Timestamp(Calendar.getInstance().getTimeInMillis()),
                        "0".getBytes()
                );
            }
            else {
                foodData = new FoodData(
                        0,
                        foodName,
                        foodSpecies,
                        foodCalories,
                        new Timestamp(Calendar.getInstance().getTimeInMillis()),
                        ImageUtils.Bitmap2Bytes(foodImage)
                );
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = dietService.addFood(foodData);
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
                        Toast.makeText(AddFoodActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AddFoodActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };
}
