package com.example.personalhealthcare.Dao;

import com.example.personalhealthcare.PO.FoodData;

import java.util.List;

public interface FoodDataDao {
    Boolean insert(FoodData foodData);
    Boolean delete(Integer FoodID);
    Boolean update(FoodData foodData);
    List<FoodData> findAll();
    FoodData findByFoodID(Integer FoodID);
    List<FoodData> findBySpecies(String Species);
    List<FoodData> findByNameLike(String keyword);
}
