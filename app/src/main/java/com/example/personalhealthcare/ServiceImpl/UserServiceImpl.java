package com.example.personalhealthcare.ServiceImpl;

import com.example.personalhealthcare.Dao.UserDao;
import com.example.personalhealthcare.DaoImpl.UserDaoImpl;
import com.example.personalhealthcare.PO.User;
import com.example.personalhealthcare.Service.UserService;
import com.example.personalhealthcare.VO.OrdinaryUser;

import java.util.List;

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
        else {
            User user1 = userDao.findAdminByID(UserID);
            if(user1 != null) {
                if (oldPassword.equals(user1.getUserPassword())) {
                    user1.setUserPassword(newPassword);
                    return userDao.updateAdmin(user1);
                }
            }
        }
        return false;
    }

    @Override
    public User getUser(String UserName) {
        return userDao.findByName(UserName);
    }

    @Override
    public Boolean addOrdinaryUser(OrdinaryUser user) {
        return userDao.insertOrdinaryUser(user);
    }

    @Override
    public List<User> findOrdinary() {
        return userDao.findOrdinaryOnlyBaseInfo();
    }

    @Override
    public Boolean deleteUser(Integer UserID) {
        return userDao.deleteOrdinary(UserID) || userDao.deleteAdmin(UserID);
    }

    @Override
    public User getUser(Integer UserID) {
        OrdinaryUser user = userDao.findOrdinaryByID(UserID);
        return (user == null) ? null : user.getBaseInfo();
    }

    @Override
    public Boolean setAdmin(Integer UserID) {
        OrdinaryUser user = userDao.findOrdinaryByID(UserID);
        if(user != null) {
            user.getBaseInfo().setUserType("Admin");
            return userDao.updateOrdinary(user);
        }
        return false;
    }
}
