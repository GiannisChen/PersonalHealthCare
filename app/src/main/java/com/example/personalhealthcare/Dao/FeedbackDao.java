package com.example.personalhealthcare.Dao;

import com.example.personalhealthcare.PO.Feedback;

import java.util.List;

public interface FeedbackDao {
    Boolean insert(Feedback feedback);
    Boolean delete(Integer QuestionID);
    Boolean update(Feedback feedback);
    List<Feedback> findAll();
    List<Feedback> findBySubmitterID(Integer UserID);
    List<Feedback> findByReplierID(Integer UserID);
    List<Feedback> findResolved();
    List<Feedback> findUnresolved();
    Feedback findByID(Integer QuestionID);
}
