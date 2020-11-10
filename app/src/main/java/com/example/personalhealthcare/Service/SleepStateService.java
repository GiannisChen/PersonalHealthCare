package com.example.personalhealthcare.Service;

import com.example.personalhealthcare.PO.SleepState;

import java.util.List;

public interface SleepStateService {
    List<SleepState> getSleepStateByID(Integer UserID);
    Boolean deleteSleepState(Integer SleepStateID);
}
