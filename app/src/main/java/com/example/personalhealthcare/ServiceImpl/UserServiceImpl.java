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
}
