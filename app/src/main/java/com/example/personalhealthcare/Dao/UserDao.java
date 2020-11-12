package com.example.personalhealthcare.Dao;

import com.example.personalhealthcare.PO.User;
import com.example.personalhealthcare.VO.OrdinaryUser;

import java.util.List;

public interface UserDao {
    Boolean insertAdmin(User admin);
    Boolean insertOrdinaryUser(OrdinaryUser user);
    Boolean deleteAdmin(Integer userID);
    Boolean deleteOrdinary(Integer userID);
    Boolean updateAdmin(User admin);
    Boolean updateOrdinary(OrdinaryUser user);
    List<User> findAll();
    List<User> findAdmin();
    List<User> findOrdinaryOnlyBaseInfo();
    List<OrdinaryUser> findOrdinary();
    User findAdminByID(Integer UserID);
    OrdinaryUser findOrdinaryByID(Integer UserID);
    User findAdminByName(String UserName);
    OrdinaryUser findOrdinaryByName(String UserName);
    User findByName(String UserName);
}
