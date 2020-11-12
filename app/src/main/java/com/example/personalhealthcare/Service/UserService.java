package com.example.personalhealthcare.Service;

import com.example.personalhealthcare.PO.User;
import com.example.personalhealthcare.VO.OrdinaryUser;

import java.util.List;

public interface UserService {
    OrdinaryUser getPersonalDetail(Integer UserID);
    Boolean updateOrdinary(OrdinaryUser user);
    Boolean updatePassword(Integer UserID, String oldPassword, String newPassword);
    User getUser(String UserName);
    Boolean addOrdinaryUser(OrdinaryUser user);
    List<User> findOrdinary();
    Boolean deleteUser(Integer UserID);
    User getUser(Integer UserID);
    Boolean setAdmin(Integer UserID);

}
