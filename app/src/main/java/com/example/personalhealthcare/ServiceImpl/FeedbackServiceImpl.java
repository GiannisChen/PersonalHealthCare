package com.example.personalhealthcare.ServiceImpl;

import com.example.personalhealthcare.Dao.FeedbackDao;
import com.example.personalhealthcare.DaoImpl.FeedbackDaoImpl;
import com.example.personalhealthcare.PO.Feedback;
import com.example.personalhealthcare.Service.FeedbackService;

public class FeedbackServiceImpl implements FeedbackService {

    private FeedbackDao feedbackDao = new FeedbackDaoImpl();

    @Override
    public Boolean addFeedback(Feedback feedback) {
        return feedbackDao.insert(feedback);
    }
}
