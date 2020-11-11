package com.example.personalhealthcare.Service;

import com.example.personalhealthcare.PO.Diet;
import com.example.personalhealthcare.PO.FoodData;
import com.example.personalhealthcare.VO.DietVO;

import java.util.List;

public interface DietService {
    List<DietVO> getDietByID(Integer UserID);
    Boolean deleteDiet(Integer dietID);
    List<FoodData> getFoodData();
    Boolean addDiet(Diet diet);
}
