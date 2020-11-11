package com.example.personalhealthcare.ServiceImpl;

import com.example.personalhealthcare.Dao.SleepStateDao;
import com.example.personalhealthcare.DaoImpl.SleepStateDaoImpl;
import com.example.personalhealthcare.PO.SleepState;
import com.example.personalhealthcare.Service.SleepStateService;

import java.util.List;

public class SleepStateServiceImpl implements SleepStateService {
    private SleepStateDao sleepStateDao = new SleepStateDaoImpl();

    @Override
    public List<SleepState> getSleepStateByID(Integer UserID) {
        return sleepStateDao.findByUserID(UserID);
    }

    @Override
    public Boolean deleteSleepState(Integer SleepStateID) {
        return sleepStateDao.delete(SleepStateID);
    }

    @Override
    public Boolean addSleepState(SleepState sleepState) {
        return sleepStateDao.insert(sleepState);
    }
}
