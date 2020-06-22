package iw.pollweb.model.dao.mysql;

import iw.framework.data.DataAccessObject;
import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.ResponseDAO;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Question;
import iw.pollweb.model.dto.Response;
import iw.pollweb.model.dto.Submission;
import iw.pollweb.model.dto.proxy.ResponseProxy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class ResponseDAO_MySQL extends DataAccessObject implements ResponseDAO {
  private PreparedStatement getIDs, selectResponseByID, selectResponsesByParticipant, selectResponsesBySubmission, selectResponsesByQuestion, insertResponse, updateResponse, deleteResponse;

  public ResponseDAO_MySQL (DataLayer dataLayer) {
    super(dataLayer);
  }

  @Override
  public void init () throws DataException {

    try {
      super.init();
      // Precompilo le query
      getIDs = connection.prepareStatement("SELECT id FROM response");
      selectResponseByID = connection.prepareStatement("SELECT * FROM response WHERE id=?");
      selectResponsesByParticipant = connection.prepareStatement("SELECT * FROM response WHERE participantID=?");
      selectResponsesBySubmission = connection.prepareStatement("SELECT * FROM response WHERE submissionID=?");
      selectResponsesByQuestion = connection.prepareStatement("SELECT * FROM response where questionID=?");
      insertResponse = connection.prepareStatement("INSERT INTO response (value, questionID, participantID, submissionID) VALUES (?, ?, ?, ?)");
      updateResponse = connection.prepareStatement("UPDATE response SET value=?, questionID=?, participantID=?, submissionID=? WHERE id=?");
      deleteResponse = connection.prepareStatement("DELETE FROM response WHERE id=?");

    } catch (SQLException e) {
      throw new DataException("Errore inizializzazione Data Layer", e);
    }
  }

  @Override
  public void destroy () throws DataException {

    // Chiudo i PreparedStatement
    try {
      getIDs.close();
      selectResponseByID.close();
      selectResponsesByParticipant.close();
      selectResponsesBySubmission.close();
      selectResponsesByQuestion.close();
      insertResponse.close();
      updateResponse.close();
      deleteResponse.close();

    } catch (SQLException e) {
      throw new DataException(e);
    }
    super.destroy();
  }

  @Override
  public ResponseProxy createResponse () {
    return new ResponseProxy(getDataLayer());
  }

  @Override
  public ResponseProxy createResponseFromRS (ResultSet rs) throws DataException {
    ResponseProxy response = createResponse();

    try {
      response.setID(rs.getInt("id"));
      response.setValue(rs.getString("value"));
      response.setQuestionID(rs.getInt("questionID"));
      response.setParticipantID(rs.getInt("participantID"));
      response.setSubmissionID(rs.getInt("submissionID"));

    } catch (SQLException e) {
      throw new DataException("Errore creazione Response", e);
    }
    return response;
  }

  @Override
  public void storeResponse (Response response) throws DataException {
    int id = response.getID();

    try {
      if (response.getID() > 0) {
        // Non eseguo operazioni di aggiornamento se il proxy non presenta modifiche
        if (response instanceof ResponseProxy && !((ResponseProxy) response).isDirty()) {
          return;
        }
        // UPDATE response SET value=?, questionID=?, participantID=?, submissionID=? WHERE id=?
        updateResponse.setString(1, response.getValue());
        if (response.getQuestion() != null) {
          updateResponse.setInt(2, response.getQuestion().getID());
        } else {
          updateResponse.setNull(2, Types.INTEGER);
        }
        if (response.getParticipant() != null) {
          updateResponse.setInt(3, response.getParticipant().getID());
        } else {
          updateResponse.setNull(3, Types.INTEGER);
        }
        if (response.getSubmission() != null) {
          updateResponse.setInt(4, response.getSubmission().getID());
        } else {
          updateResponse.setNull(4, Types.INTEGER);
        }
        updateResponse.setInt(5, response.getID());
        updateResponse.executeUpdate();

      } else {
        // INSERT INTO response (value, questionID, participantID, submissionID) VALUES (?, ?, ?, ?)
        insertResponse.setString(1, response.getValue());
        if (response.getQuestion() != null) {
          insertResponse.setInt(2, response.getQuestion().getID());
        } else {
          insertResponse.setNull(2, Types.INTEGER);
        }
        if (response.getParticipant() != null) {
          insertResponse.setInt(3, response.getParticipant().getID());
        } else {
          insertResponse.setNull(3, Types.INTEGER);
        }
        if (response.getSubmission() != null) {
          insertResponse.setInt(4, response.getSubmission().getID());
        } else {
          insertResponse.setNull(4, Types.INTEGER);
        }
        if (insertResponse.executeUpdate() == 1) {
          // Leggo la chiave generata dal DB per la precedente INSERT
          try (ResultSet rs = insertResponse.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // Aggiorno la chiave
          response.setID(id);
        }
      }
      // Resetto l'attributo dirty del proxy
      if (response instanceof ResponseProxy) {
        ((ResponseProxy) response).setDirty(false);
      }
    } catch (SQLException e) {
      throw new DataException("Errore salvataggio Response", e);
    }
  }

  @Override
  public Response getResponseByID (int id) throws DataException {

    // SELECT * FROM response WHERE id=?
    try {
      selectResponseByID.setInt(1, id);
      try (ResultSet rs = selectResponseByID.executeQuery()) {
        if (rs.next()) {
          return createResponseFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Response", e);
    }
    return null;
  }

  @Override
  public List<Response> getResponsesByParticipant (Participant participant) throws DataException {
    List<Response> responses = new ArrayList<>();

    // SELECT * FROM response WHERE participantID=?
    try {
      selectResponsesByParticipant.setInt(1, participant.getID());
      try (ResultSet rs = selectResponsesByParticipant.executeQuery()) {
        while (rs.next()) {
          responses.add(getResponseByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Response", e);
    }
    return responses;
  }

  @Override
  public List<Response> getResponsesBySubmission (Submission submission) throws DataException {
    List<Response> responses = new ArrayList<>();

    // SELECT * FROM response WHERE submissionID=?
    try {
      selectResponsesBySubmission.setInt(1, submission.getID());
      try (ResultSet rs = selectResponsesBySubmission.executeQuery()) {
        while (rs.next()) {
          responses.add(getResponseByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Response", e);
    }
    return responses;
  }

  @Override
  public List<Response> getResponsesByQuestion (Question question) throws DataException {
    List<Response> responses = new ArrayList<>();

    // SELECT * FROM response where questionID=?
    try {
      selectResponsesByQuestion.setInt(1, question.getID());
      try (ResultSet rs = selectResponsesByQuestion.executeQuery()) {
        while (rs.next()) {
          responses.add(getResponseByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Response", e);
    }
    return responses;
  }

  @Override
  public void deleteResponse (int id) throws DataException {

    // DELETE FROM response WHERE id=?
    try {
      deleteResponse.setInt(1, id);
      deleteResponse.executeUpdate();

    } catch (SQLException e) {
      throw new DataException("Errore eliminazione Response", e);
    }
  }
}
