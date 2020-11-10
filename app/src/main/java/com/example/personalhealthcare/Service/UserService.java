package com.example.personalhealthcare.Service;

import com.example.personalhealthcare.VO.OrdinaryUser;

import java.util.List;

public interface UserService {
    OrdinaryUser getPersonalDetail(Integer UserID);
    Boolean updateOrdinary(OrdinaryUser user);
    Boolean updatePassword(Integer UserID, String oldPassword, String newPassword);
}
