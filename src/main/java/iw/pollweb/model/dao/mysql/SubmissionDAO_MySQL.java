package iw.pollweb.model.dao.mysql;

import iw.framework.data.DataAccessObject;
import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.SubmissionDAO;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Submission;
import iw.pollweb.model.dto.Survey;
import iw.pollweb.model.dto.proxy.SubmissionProxy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class SubmissionDAO_MySQL extends DataAccessObject implements SubmissionDAO {
  private PreparedStatement getIDs, selectSubmissionByID, selectSubmissionByParticipant, selectSubmissionsBySurvey, insertSubmission, updateSubmission, deleteSubmission;

  public SubmissionDAO_MySQL (DataLayer dataLayer) {
    super(dataLayer);
  }

  @Override
  public void init () throws DataException {

    try {
      super.init();
      // Precompilo le query
      getIDs = connection.prepareStatement("SELECT id FROM submission");
      selectSubmissionByID = connection.prepareStatement("SELECT * FROM submission WHERE id=?");
      selectSubmissionByParticipant = connection.prepareStatement("SELECT * FROM submission WHERE participantID=?");
      selectSubmissionsBySurvey = connection.prepareStatement("SELECT * FROM submission WHERE surveyID=?");
      insertSubmission = connection.prepareStatement("INSERT INTO submission (surveyID, participantID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
      updateSubmission = connection.prepareStatement("UPDATE submission SET surveyID=?, participantID=? WHERE id=?");
      deleteSubmission = connection.prepareStatement("DELETE FROM submission WHERE id=?");

    } catch (SQLException e) {
      throw new DataException("Errore inizializzazione Data Layer", e);
    }
  }

  @Override
  public void destroy () throws DataException {

    // Chiudo i PreparedStatement
    try {
      getIDs.close();
      selectSubmissionByID.close();
      selectSubmissionByParticipant.close();
      selectSubmissionsBySurvey.close();
      insertSubmission.close();
      updateSubmission.close();
      deleteSubmission.close();

    } catch (SQLException e) {
      throw new DataException(e);
    }
    super.destroy();
  }

  @Override
  public SubmissionProxy createSubmission () {
    return new SubmissionProxy(getDataLayer());
  }

  @Override
  public SubmissionProxy createSubmissionFromRS (ResultSet rs) throws DataException {

    try {
      SubmissionProxy submission = createSubmission();

      submission.setID(rs.getInt("id"));
      submission.setTimestamp(rs.getTimestamp("timestamp"));
      submission.setSurveyID(rs.getInt("surveyID"));
      submission.setParticipantID(rs.getInt("participantID"));

      return submission;

    } catch (SQLException e) {
      throw new DataException("Errore creazione Submission", e);
    }
  }

  @Override
  public void storeSubmission (Submission submission) throws DataException {
    int id = submission.getID();

    try {
      if (submission.getID() > 0) { // UPDATE
        // Non eseguo operazioni se il proxy non presenta modifiche
        if (submission instanceof SubmissionProxy && !((SubmissionProxy) submission).isDirty()) {
          return;
        }
        if (submission.getSurvey() != null) {
          updateSubmission.setInt(1, submission.getSurvey().getID());
        } else {
          updateSubmission.setNull(1, Types.INTEGER);
        }
        if (submission.getParticipant() != null) {
          updateSubmission.setInt(2, submission.getParticipant().getID());
        } else {
          updateSubmission.setNull(2, Types.INTEGER);
        }
        updateSubmission.setInt(3, submission.getID());
        updateSubmission.executeUpdate();
      } else { // INSERT
        if (submission.getSurvey() != null) {
          insertSubmission.setInt(1, submission.getSurvey().getID());
        } else {
          insertSubmission.setNull(1, Types.INTEGER);
        }
        if (submission.getParticipant() != null) {
          insertSubmission.setInt(2, submission.getParticipant().getID());
        } else {
          insertSubmission.setNull(2, Types.INTEGER);
        }
        if (insertSubmission.executeUpdate() == 1) {
          // Leggo la chiave generata dal DB per la precedente INSERT
          try (ResultSet rs = insertSubmission.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // Aggiorno la chiave
          submission.setID(id);
        }
      }
      // Resetto l'attributo dirty del proxy
      if (submission instanceof SubmissionProxy) {
        ((SubmissionProxy) submission).setDirty(false);
      }
    } catch (SQLException e) {
      throw new DataException("Errore salvataggio Submission", e);
    }
  }

  @Override
  public Submission getSubmissionByID (int id) throws DataException {

    try {
      selectSubmissionByID.setInt(1, id);
      try (ResultSet rs = selectSubmissionByID.executeQuery()) {
        if (rs.next()) {
          return createSubmissionFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Submission", e);
    }
    return null;
  }

  @Override
  public Submission getSubmissionByParticipant (Participant participant) throws DataException {

    try {
      selectSubmissionByParticipant.setInt(1, participant.getID());
      try (ResultSet rs = selectSubmissionByParticipant.executeQuery()) {
        if (rs.next()) {
          return createSubmissionFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Submission", e);
    }
    return null;
  }

  @Override
  public List<Submission> getSubmissions () throws DataException {
    List<Submission> submissions = new ArrayList<>();

    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        submissions.add(getSubmissionByID(rs.getInt("id")));
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Submission", e);
    }
    return submissions;
  }

  @Override
  public List<Submission> getSubmissionsBySurvey (Survey survey) throws DataException {
    List<Submission> submissions = new ArrayList<>();

    try {
      selectSubmissionsBySurvey.setInt(1, survey.getID());
      try (ResultSet rs = selectSubmissionsBySurvey.executeQuery()) {
        while (rs.next()) {
          submissions.add(getSubmissionByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Submission", e);
    }
    return submissions;
  }

  @Override
  public void deleteSubmission (int id) throws DataException {

    try {
      deleteSubmission.setInt(1, id);
      deleteSubmission.executeUpdate();

    } catch (SQLException e) {
      throw new DataException("Errore eliminazione Submission", e);
    }
  }
}
