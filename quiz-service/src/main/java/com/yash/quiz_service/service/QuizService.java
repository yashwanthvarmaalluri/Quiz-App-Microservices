package com.yash.quiz_service.service;


import com.yash.quiz_service.dao.QuizDao;
import com.yash.quiz_service.feign.QuizInterface;
import com.yash.quiz_service.model.QuestionWrapper;
import com.yash.quiz_service.model.Quiz;
import com.yash.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;
//    @Autowired
//    QuestionDao questionDao;

    @Autowired
    QuizInterface quizInterface;


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<QuestionWrapper> questionsForUser = quizInterface.getQuestionsFromId(quiz.getQuestionIds()).getBody();

        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);

    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        Integer result = quizInterface.getScore(responses).getBody();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
