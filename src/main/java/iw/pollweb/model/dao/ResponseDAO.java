package iw.pollweb.model.dao;

import iw.framework.data.DataException;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Question;
import iw.pollweb.model.dto.Response;
import iw.pollweb.model.dto.Submission;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public interface ResponseDAO {

  // Utility
  Response createResponse ();
  Response createResponseFromRS (ResultSet rs) throws DataException;

  // CRUD
  void storeResponse (Response response) throws DataException;
  Response getResponseByID (int id) throws DataException;
  List<Response> getResponsesByParticipant (Participant participant) throws DataException;
  List<Response> getResponsesBySubmission (Submission submission) throws DataException;
  List<Response> getResponsesByQuestion (Question question) throws DataException;
  void deleteResponse (int id) throws DataException;
}
