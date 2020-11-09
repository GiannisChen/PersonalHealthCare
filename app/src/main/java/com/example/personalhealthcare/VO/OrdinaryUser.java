package com.example.personalhealthcare.VO;

import com.example.personalhealthcare.PO.OrdinaryUserData;
import com.example.personalhealthcare.PO.User;

import lombok.Data;

@Data
public class OrdinaryUser {
    private User baseInfo;
    private OrdinaryUserData userData;
    public OrdinaryUser() {}
    public OrdinaryUser(User baseInfo, OrdinaryUserData userData) {
        this.baseInfo = baseInfo;
        this.userData = userData;
    }
}
