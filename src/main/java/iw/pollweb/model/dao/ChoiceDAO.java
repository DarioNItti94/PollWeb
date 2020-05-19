/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.model.dao;


import  iw.framework.data.DataException;
import  iw.pollweb.model.dto.Choice;
import  iw.pollweb.model.dto.Question;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public interface ChoiceDAO {

  // utility
  Choice createChoice ();

  Choice createChoiceFromRS (ResultSet rs) throws DataException;

  // CRUD
  void storeChoice (Choice choice) throws DataException;

  Choice getChoiceByID (int id) throws DataException;

  List<Choice> getChoices () throws DataException;

  List<Choice> getChoicesByQuestion (Question question) throws DataException;

  void deleteChoice (int id) throws DataException;
}
