package com.example.personalhealthcare.ServiceImpl;

import com.example.personalhealthcare.Dao.DietDao;
import com.example.personalhealthcare.Dao.FoodDataDao;
import com.example.personalhealthcare.DaoImpl.DietDaoImpl;
import com.example.personalhealthcare.DaoImpl.FoodDataDaoImpl;
import com.example.personalhealthcare.PO.Diet;
import com.example.personalhealthcare.PO.FoodData;
import com.example.personalhealthcare.Service.DietService;
import com.example.personalhealthcare.VO.DietVO;

import java.util.List;

public class DietServiceImpl implements DietService {

    private DietDao dietDao = new DietDaoImpl();
    private FoodDataDao foodDataDao = new FoodDataDaoImpl();

    @Override
    public List<DietVO> getDietByID(Integer UserID) {
        return dietDao.findByUserID(UserID);
    }

    @Override
    public Boolean deleteDiet(Integer dietID) {
        return dietDao.delete(dietID);
    }

    @Override
    public List<FoodData> getFoodData() {
        return foodDataDao.findAll();
    }

    @Override
    public Boolean addDiet(Diet diet) {
        return dietDao.insert(diet);
    }

    @Override
    public Boolean deleteFood(Integer foodID) {
        return foodDataDao.delete(foodID);
    }

    @Override
    public Boolean addFood(FoodData foodData) {
        return foodDataDao.insert(foodData);
    }
}
