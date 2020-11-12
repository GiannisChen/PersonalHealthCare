package com.example.personalhealthcare.ui.feedback;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedbackViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FeedbackViewModel() {

    }

    public LiveData<String> getText() {
        return mText;
    }
}