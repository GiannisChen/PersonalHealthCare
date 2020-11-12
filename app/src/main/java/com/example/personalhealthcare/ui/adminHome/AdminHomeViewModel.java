package com.example.personalhealthcare.ui.adminHome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdminHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AdminHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}