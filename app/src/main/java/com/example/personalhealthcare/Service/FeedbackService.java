package com.example.personalhealthcare.Service;

import com.example.personalhealthcare.PO.Feedback;

import java.util.List;

public interface FeedbackService {
    Boolean addFeedback(Feedback feedback);
    List<Feedback> getAllUnresolvedFeedback();
    Boolean deleteFeedback(Integer questionID);
}
