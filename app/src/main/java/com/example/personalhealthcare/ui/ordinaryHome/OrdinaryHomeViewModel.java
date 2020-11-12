package com.example.personalhealthcare.ui.ordinaryHome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrdinaryHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OrdinaryHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}