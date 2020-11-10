package com.example.personalhealthcare.ui.Diet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DietViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DietViewModel() {

    }

    public LiveData<String> getText() {
        return mText;
    }
}