package com.example.personalhealthcare.ServiceImpl;

import com.example.personalhealthcare.Dao.UserDao;
import com.example.personalhealthcare.DaoImpl.UserDaoImpl;
import com.example.personalhealthcare.Service.UserService;
import com.example.personalhealthcare.VO.OrdinaryUser;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public OrdinaryUser getPersonalDetail(Integer UserID) {
        return userDao.findOrdinaryByID(UserID);
    }

    @Override
    public Boolean updateOrdinary(OrdinaryUser user) {
        return userDao.updateOrdinary(user);
    }

    @Override
    public Boolean updatePassword(Integer UserID, String oldPassword, String newPassword) {
        OrdinaryUser user = userDao.findOrdinaryByID(UserID);
        if(user != null) {
            if (oldPassword.equals(user.getBaseInfo().getUserPassword())) {
                user.getBaseInfo().setUserPassword(newPassword);
                return userDao.updateOrdinary(user);
            }
        }
        return false;
    }
}
