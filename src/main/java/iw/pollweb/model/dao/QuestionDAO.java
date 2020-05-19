/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.model.dao;

import  iw.framework.data.DataException;
import  iw.pollweb.model.dto.Question;
import  iw.pollweb.model.dto.Survey;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public interface QuestionDAO {

  // utility
  Question createQuestion ();

  Question createQuestionFromRS (ResultSet rs) throws DataException;

  // CRUD
  void storeQuestion (Question question) throws DataException;

  Question getQuestionByID (int id) throws DataException;

  List<Question> getQuestions () throws DataException;

  List<Question> getQuestionsBySurvey (Survey survey) throws DataException;

  void deleteQuestion (int id) throws DataException;
}
