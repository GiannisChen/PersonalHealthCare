package com.example.personalhealthcare.ui.SleepState;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SleepStateViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SleepStateViewModel() {

    }

    public LiveData<String> getText() {
        return mText;
    }
}