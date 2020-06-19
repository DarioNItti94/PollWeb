package iw.pollweb.model.dao;

import iw.framework.data.DataException;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public interface SurveyDAO {

  // Utility
  Survey createSurvey ();

  Survey createSurveyFromRS (ResultSet rs) throws DataException;

  // CRUD
  void storeSurvey (Survey survey) throws DataException;

  Survey getSurveyByID (int id) throws DataException;

  List<Survey> getSurveys () throws DataException;

  List<Survey> getSurveysBySupervisor (Supervisor supervisor) throws DataException;

  List<Survey> getSurveysByReservation (boolean isReserved) throws DataException;

  List<Survey> getSurveysByVisibilityAndReservation (boolean isActive, boolean isReserved) throws DataException;

  List<Survey> getSurveysByVisibility (boolean isActive) throws DataException;

  List<Survey> getSurveysByStatus (boolean isClosed) throws DataException;

  void deleteSurvey (int id) throws DataException;
}
