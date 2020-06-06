package iw.pollweb.model.dao.mysql;

import iw.framework.data.DataAccessObject;
import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.ParticipantDAO;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Survey;
import iw.pollweb.model.dto.proxy.ParticipantProxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class ParticipantDAO_MySQL extends DataAccessObject implements ParticipantDAO {
  private PreparedStatement getIDs, selectParticipantByID, selectParticipantByEmailPassword, selectParticipantBySurvey, insertParticipant, updateParticipant, deleteParticipant;

  public ParticipantDAO_MySQL (DataLayer dataLayer) {
    super(dataLayer);
  }

  @Override
  public void init () throws DataException {
    try {
      super.init();
      // Precompilo le query
      getIDs = connection.prepareStatement("SELECT id FROM participant");
      selectParticipantByID = connection.prepareStatement("SELECT * FROM participant WHERE id=?");
      selectParticipantByEmailPassword = connection.prepareStatement("SELECT * FROM participant WHERE email=? AND hashedPassword=? AND isDisabled=0 ");
      selectParticipantBySurvey = connection.prepareStatement("SELECT * FROM participant WHERE surveyID=?");
      insertParticipant = connection.prepareStatement("INSERT INTO participant (firstName, lastName, email, hashedPassword, isDisabled, surveyID) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      updateParticipant = connection.prepareStatement("UPDATE participant SET firstName=?, lastName=?, email=?, hashedPassword=?, isDisabled=?, surveyID=? WHERE id=?");
      deleteParticipant = connection.prepareStatement("DELETE FROM participant WHERE id=?");

    } catch (SQLException e) {
      throw new DataException("Errore inizializzazione Data Layer", e);
    }
  }

  @Override
  public void destroy () throws DataException {
    // Chiudo i PreparedStatement
    try {
      getIDs.close();
      selectParticipantByID.close();
      selectParticipantByEmailPassword.close();
      selectParticipantBySurvey.close();
      insertParticipant.close();
      updateParticipant.close();
      deleteParticipant.close();

    } catch (SQLException e) {
      throw new DataException(e);
    }
    super.destroy();
  }

  @Override
  public ParticipantProxy createParticipant () {
    return new ParticipantProxy(getDataLayer());
  }

  @Override
  public ParticipantProxy createParticipantFromRS (ResultSet rs) throws DataException {
    try {
      ParticipantProxy participant = createParticipant();

      participant.setID(rs.getInt("id"));
      participant.setFirstName(rs.getString("firstName"));
      participant.setLastName(rs.getString("lastName"));
      participant.setEmail(rs.getString("email"));
      participant.setHashedPassword(rs.getString("hashedPassword"));
      participant.setDisabled(rs.getBoolean("isDisabled"));
      participant.setSurveyID(rs.getInt("surveyID"));

      return participant;

    } catch (SQLException e) {
      throw new DataException("Errore creazione Participant", e);
    }
  }

  @Override
  public int authenticateParticipant (Participant participant) throws DataException {
    try {
      selectParticipantByEmailPassword.setString(1, participant.getEmail());
      selectParticipantByEmailPassword.setString(2, participant.getHashedPassword());

      try (ResultSet rs = selectParticipantByEmailPassword.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore autenticazione Participant", e);
    }
    return 0;
  }

  @Override
  public void storeParticipant (Participant participant) throws DataException {
    int id = participant.getID();

    try {
      if (participant.getID() > 0) { // UPDATE
        // Non eseguo operazioni se il proxy non presenta modifiche
        if (participant instanceof ParticipantProxy && ((ParticipantProxy) participant).isDirty()) {
          return;
        }
        updateParticipant.setString(1, participant.getFirstName());
        updateParticipant.setString(2, participant.getLastName());
        updateParticipant.setString(3, participant.getEmail());
        updateParticipant.setString(4, participant.getHashedPassword());
        updateParticipant.setBoolean(5, participant.isDisabled());
        if (participant.getSurvey() != null) {
          updateParticipant.setInt(6, participant.getSurvey().getID());
        } else {
          updateParticipant.setNull(6, Types.INTEGER);
        }
        updateParticipant.setInt(7, participant.getID());
        updateParticipant.executeUpdate();
      } else { // INSERT
        insertParticipant.setString(1, participant.getFirstName());
        insertParticipant.setString(2, participant.getLastName());
        insertParticipant.setString(3, participant.getEmail());
        insertParticipant.setString(4, participant.getHashedPassword());
        insertParticipant.setBoolean(5, participant.isDisabled());
        if (participant.getSurvey() != null) {
          insertParticipant.setInt(6, participant.getSurvey().getID());
        } else {
          insertParticipant.setNull(6, Types.INTEGER);
        }
        if (insertParticipant.executeUpdate() == 1) {
          // Leggo la chiave generata dal DB per la precedente INSERT
          try (ResultSet rs = insertParticipant.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // Aggiorno la chiave
          participant.setID(id);
        }
      }
      // Resetto l'attributo dirty del proxy
      if (participant instanceof ParticipantProxy) {
        ((ParticipantProxy) participant).setDirty(false);
      }
    } catch (SQLException e) {
      throw new DataException("Errore salvataggio Participant", e);
    }
  }

  @Override
  public Participant getParticipantByID (int id) throws DataException {
    try {
      selectParticipantByID.setInt(1, id);
      try (ResultSet rs = selectParticipantByID.executeQuery()) {
        if (rs.next()) {
          return createParticipantFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Participant", e);
    }
    return null;
  }

  @Override
  public List<Participant> getParticipants () throws DataException {
    List<Participant> participants = new ArrayList<>();

    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        participants.add(getParticipantByID(rs.getInt("id")));
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Participant", e);
    }
    return participants;
  }

  @Override
  public List<Participant> getParticipantsBySurvey (Survey survey) throws DataException {
    List<Participant> participants = new ArrayList<>();

    try {
      selectParticipantBySurvey.setInt(1, survey.getID());
      try (ResultSet rs = selectParticipantBySurvey.executeQuery()) {
        while (rs.next()) {
          participants.add(getParticipantByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Participant", e);
    }
    return participants;
  }

  @Override
  public void deleteParticipant (int id) throws DataException {
    try {
      deleteParticipant.setInt(1, id);
      deleteParticipant.executeUpdate();

    } catch (SQLException e) {
      throw new DataException("Errore eliminazione Participant", e);
    }
  }
}
