package iw.pollweb.model.dao.mysql;

import iw.framework.data.DataAccessObject;
import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.ChoiceDAO;
import iw.pollweb.model.dto.Choice;
import iw.pollweb.model.dto.Question;
import iw.pollweb.model.dto.proxy.ChoiceProxy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class ChoiceDAO_MySQL extends DataAccessObject implements ChoiceDAO {
  private PreparedStatement getIDs, selectChoiceByID, selectChoicesByQuestion, insertChoice, updateChoice, deleteChoice;

  public ChoiceDAO_MySQL (DataLayer dataLayer) {
    super(dataLayer);
  }

  @Override
  public void init () throws DataException {
    try {
      super.init();
      // Precompilo le query
      getIDs = connection.prepareStatement("SELECT id FROM choice");
      selectChoiceByID = connection.prepareStatement("SELECT * FROM choice WHERE id=?");
      selectChoicesByQuestion = connection.prepareStatement("SELECT * FROM choice WHERE questionID=?");
      insertChoice = connection.prepareStatement("INSERT INTO choice (value, number, questionID) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      updateChoice = connection.prepareStatement("UPDATE choice SET value=?, number=?, questionID=? WHERE id=?");
      deleteChoice = connection.prepareStatement("DELETE FROM choice WHERE id=?");

    } catch (SQLException e) {
      throw new DataException("Errore inizializzazione Data Layer", e);
    }
  }

  @Override
  public void destroy () throws DataException {
    // Chiudo i PreparedStatement
    try {
      getIDs.close();
      selectChoiceByID.close();
      selectChoicesByQuestion.close();
      insertChoice.close();
      updateChoice.close();
      deleteChoice.close();

    } catch (SQLException e) {
      throw new DataException(e);
    }
    super.destroy();
  }

  @Override
  public ChoiceProxy createChoice () {
    return new ChoiceProxy(getDataLayer());
  }

  @Override
  public ChoiceProxy createChoiceFromRS (ResultSet rs) throws DataException {
    try {
      ChoiceProxy choice = createChoice();

      choice.setID(rs.getInt("id"));
      choice.setValue(rs.getString("value"));
      choice.setNumber(rs.getInt("number"));
      choice.setQuestionID(rs.getInt("questionID"));

      return choice;

    } catch (SQLException e) {
      throw new DataException("Errore creazione Choice", e);
    }
  }

  @Override
  public void storeChoice (Choice choice) throws DataException {
    int id = choice.getID();

    try {
      if (choice.getID() > 0) { // UPDATE
        // Non eseguo operazioni se il proxy non presenta modifiche
        if (choice instanceof ChoiceProxy && !((ChoiceProxy) choice).isDirty()) {
          return;
        }
        updateChoice.setString(1, choice.getValue());
        updateChoice.setInt(2, choice.getNumber());
        if (choice.getQuestion() != null) {
          updateChoice.setInt(3, choice.getQuestion().getID());
        } else {
          updateChoice.setNull(3, Types.INTEGER);
        }
        updateChoice.setInt(4, choice.getID());
        updateChoice.executeUpdate();

      } else { // INSERT
        insertChoice.setString(1, choice.getValue());
        insertChoice.setInt(2, choice.getNumber());
        if (choice.getQuestion() != null) {
          insertChoice.setInt(3, choice.getQuestion().getID());
        } else {
          insertChoice.setNull(3, Types.INTEGER);
        }
        if (insertChoice.executeUpdate() == 1) {
          // Leggo la chiave generata dal DB per la precedente INSERT
          try (ResultSet rs = insertChoice.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // Aggiorno la chiave
          choice.setID(id);
        }
      }
      // Resetto l'attributo dirty del proxy
      if (choice instanceof ChoiceProxy) {
        ((ChoiceProxy) choice).setDirty(false);
      }
    } catch (SQLException e) {
      throw new DataException("Errore salvataggio Choice", e);
    }
  }

  @Override
  public Choice getChoiceByID (int id) throws DataException {
    try {
      selectChoiceByID.setInt(1, id);
      try (ResultSet rs = selectChoiceByID.executeQuery()) {
        if (rs.next()) {
          return createChoiceFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Choice", e);
    }
    return null;
  }

  @Override
  public List<Choice> getChoices () throws DataException {
    List<Choice> choices = new ArrayList<>();

    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        choices.add(getChoiceByID(rs.getInt("id")));
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Choice", e);
    }
    return choices;
  }

  @Override
  public List<Choice> getChoicesByQuestion (Question question) throws DataException {
    List<Choice> choices = new ArrayList<>();

    try {
      selectChoicesByQuestion.setInt(1, question.getID());
      try (ResultSet rs = selectChoicesByQuestion.executeQuery()) {
        while (rs.next()) {
          choices.add(getChoiceByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Choice", e);
    }
    return choices;
  }

  @Override
  public void deleteChoice (int id) throws DataException {
    try {
      deleteChoice.setInt(1, id);
      deleteChoice.executeUpdate();

    } catch (SQLException e) {
      throw new DataException("Errore eliminazione Choice", e);
    }
  }
}
