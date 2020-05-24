package iw.pollweb.model.dao.mysql;

import iw.framework.data.DataAccessObject;
import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.SurveyDAO;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import iw.pollweb.model.dto.proxy.SurveyProxy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class SurveyDAO_MySQL extends DataAccessObject implements SurveyDAO {
  private PreparedStatement getIDs, selectSurveyByID, selectSurveyBySupervisor, selectSurveysByReservation, selectSurveysByVisibility, getSelectSurveysByVisibilityAndReservation, selectSurveysByStatus, insertSurvey, updateSurvey, deleteSurveyByID;

  public SurveyDAO_MySQL (DataLayer dataLayer) {
    super(dataLayer);
  }

  @Override
  public void init () throws DataException {
    try {
      super.init();
      // Precompilo le query
      getIDs = connection.prepareStatement("SELECT id FROM survey");
      selectSurveyByID = connection.prepareStatement("SELECT * FROM survey WHERE id=?");
      selectSurveyBySupervisor = connection.prepareStatement("SELECT * FROM survey WHERE supervisorID=?");
      selectSurveysByReservation = connection.prepareStatement("SELECT * FROM survey WHERE isReserved=?");
      selectSurveysByVisibility = connection.prepareStatement("SELECT * FROM survey WHERE isActive=?");
      getSelectSurveysByVisibilityAndReservation = connection.prepareStatement("SELECT * FROM survey WHERE isReserved=? AND isActive=?");
      selectSurveysByStatus = connection.prepareStatement("SELECT * FROM survey WHERE isClosed=?");
      insertSurvey = connection.prepareStatement("INSERT INTO survey (title, openingText, closingText, isReserved, isActive, isClosed, supervisorID) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      updateSurvey = connection.prepareStatement("UPDATE survey SET title=?, openingText=?, closingText=?, isReserved=?, isActive=?, isClosed=?, supervisorID=? WHERE id=?");
      deleteSurveyByID = connection.prepareStatement("DELETE FROM survey WHERE id=?");

    } catch (SQLException e) {
      throw new DataException("Errore inizializzazione Data Layer", e);
    }
  }

  @Override
  public void destroy () throws DataException {
    // Chiudo i PreparedStatement
    try {
      getIDs.close();
      selectSurveyByID.close();
      selectSurveyBySupervisor.close();
      selectSurveysByReservation.close();
      selectSurveysByVisibility.close();
      getSelectSurveysByVisibilityAndReservation.close();
      selectSurveysByStatus.close();
      insertSurvey.close();
      updateSurvey.close();
      deleteSurveyByID.close();

    } catch (SQLException e) {
      throw new DataException(e);
    }
    super.destroy();
  }

  @Override
  public SurveyProxy createSurvey () {
    return new SurveyProxy(getDataLayer());
  }

  @Override
  public Survey createSurveyFromRS (ResultSet rs) throws DataException {
    try {
      SurveyProxy survey = createSurvey();

      survey.setTitle(rs.getString("title"));
      survey.setOpeningText(rs.getString("openingText"));
      survey.setClosingText(rs.getString("closingText"));
      survey.setReserved(rs.getBoolean("isReserved"));
      survey.setActive(rs.getBoolean("isActive"));
      survey.setClosed(rs.getBoolean("isClosed"));
      survey.setSupervisorID(rs.getInt("supervisorID"));

      return survey;

    } catch (SQLException e) {
      throw new DataException("Errore creazione Survey", e);
    }
  }

  @Override
  public void storeSurvey (Survey survey) throws DataException {
    int id = survey.getID();

    try {
      if (survey.getID() > 0) { // UPDATE
        // Non eseguo operazioni se il proxy non presenta modifiche
        if (survey instanceof SurveyProxy && (((SurveyProxy) survey).isDirty())) {
          return;
        }
        // Eseguo l'update
        updateSurvey.setString(1, survey.getTitle());
        updateSurvey.setString(2, survey.getOpeningText());
        updateSurvey.setString(3, survey.getClosingText());
        updateSurvey.setBoolean(4, survey.isReserved());
        updateSurvey.setBoolean(5, survey.isActive());
        updateSurvey.setBoolean(6, survey.isClosed());
        if (survey.getSupervisor() != null) {
          updateSurvey.setInt(7, survey.getSupervisor().getID());

        } else {
          updateSurvey.setNull(7, Types.INTEGER);
        }
        updateSurvey.setInt(8, survey.getID());
        updateSurvey.executeUpdate();

      } else { // INSERT
        insertSurvey.setString(1, survey.getTitle());
        insertSurvey.setString(2, survey.getOpeningText());
        insertSurvey.setString(3, survey.getClosingText());
        insertSurvey.setBoolean(4, survey.isReserved());
        insertSurvey.setBoolean(5, survey.isActive());
        insertSurvey.setBoolean(6, survey.isClosed());
        if (survey.getSupervisor() != null) {
          insertSurvey.setInt(7, survey.getSupervisor().getID());

        } else {
          insertSurvey.setInt(7, Types.INTEGER);
        }
        if (insertSurvey.executeUpdate() == 1) {
          // Leggo la chiave generata dal DB per la precedente INSERT
          try (ResultSet rs = insertSurvey.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // Aggiorno la chiave
          survey.setID(id);
        }
      }
      // Resetto l'attributo dirty del proxy
      if (survey instanceof SurveyProxy) {
        ((SurveyProxy) survey).setDirty(false);
      }
    } catch (SQLException e) {
      throw new DataException("Errore salvataggio Survey", e);
    }
  }

  @Override
  public Survey getSurveyByID (int id) throws DataException {
    try {
      selectSurveyByID.setInt(1, id);
      try (ResultSet rs = selectSurveyByID.executeQuery()) {
        if (rs.next()) {
          return createSurveyFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Survey", e);
    }
    return null;
  }

  @Override
  public List<Survey> getSurveys () throws DataException {
    List<Survey> surveys = new ArrayList<>();

    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        surveys.add(getSurveyByID(rs.getInt("id")));
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Survey", e);
    }
    return surveys;
  }

  @Override
  public List<Survey> getSurveysBySupervisor (Supervisor supervisor) throws DataException {
    List<Survey> surveys = new ArrayList<>();

    try {
      selectSurveyBySupervisor.setInt(1, supervisor.getID());

      try (ResultSet rs = selectSurveyBySupervisor.executeQuery()) {
        while (rs.next()) {
          surveys.add(getSurveyByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Survey", e);
    }
    return surveys;
  }

  @Override
  public List<Survey> getSurveysByReservation (boolean isReserved) throws DataException {
    List<Survey> surveys = new ArrayList<>();

    try {
      selectSurveysByReservation.setBoolean(1, isReserved);

      try (ResultSet rs = selectSurveysByReservation.executeQuery()) {
        while (rs.next()) {
          surveys.add(getSurveyByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Survey", e);
    }
    return surveys;
  }

  @Override
  public List<Survey> getSurveysByVisibilityAndReservation (boolean isActive, boolean isReserved) throws DataException {
    List<Survey> surveys = new ArrayList<>();

    try {
      getSelectSurveysByVisibilityAndReservation.setBoolean(1, isActive);
      getSelectSurveysByVisibilityAndReservation.setBoolean(2, isReserved);
      try (ResultSet rs = getSelectSurveysByVisibilityAndReservation.executeQuery()) {
        while (rs.next()) {
          surveys.add(getSurveyByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Survey", e);
    }
    return surveys;
  }

  @Override
  public List<Survey> getSurveysByVisibility (boolean isActive) throws DataException {
    List<Survey> surveys = new ArrayList<>();

    try {
      selectSurveysByVisibility.setBoolean(1, isActive);

      try (ResultSet rs = selectSurveysByVisibility.executeQuery()) {
        while (rs.next()) {
          surveys.add(getSurveyByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Survey", e);
    }
    return surveys;
  }

  @Override
  public List<Survey> getSurveysByStatus (boolean isClosed) throws DataException {
    List<Survey> surveys = new ArrayList<>();

    try {
      selectSurveysByStatus.setBoolean(1, isClosed);

      try (ResultSet rs = selectSurveysByStatus.executeQuery()) {
        while (rs.next()) {
          surveys.add(getSurveyByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Survey", e);
    }
    return surveys;
  }

  @Override
  public void deleteSurvey (int id) throws DataException {
    try {
      deleteSurveyByID.setInt(1, id);
      deleteSurveyByID.executeUpdate();

    } catch (SQLException e) {
      throw new DataException("Errore eliminazione Survey", e);
    }
  }
}
