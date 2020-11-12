package com.example.personalhealthcare.ui.adminSettings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdminSettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AdminSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}