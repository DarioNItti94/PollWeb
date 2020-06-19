package iw.pollweb.model.dao;

import iw.framework.data.DataException;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Submission;
import iw.pollweb.model.dto.Survey;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public interface SubmissionDAO {

  // Utility
  Submission createSubmission ();

  Submission createSubmissionFromRS (ResultSet rs) throws DataException;

  // CRUD
  void storeSubmission (Submission submission) throws DataException;

  Submission getSubmissionByID (int id) throws DataException;

  Submission getSubmissionByParticipant (Participant participant) throws DataException;

  List<Submission> getSubmissions () throws DataException;

  List<Submission> getSubmissionsBySurvey (Survey survey) throws DataException;

  void deleteSubmission (int id) throws DataException;
}
