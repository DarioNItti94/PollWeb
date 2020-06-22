package iw.pollweb.model.dao.mysql;

import iw.framework.data.DataAccessObject;
import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.QuestionDAO;
import iw.pollweb.model.dto.Question;
import iw.pollweb.model.dto.Survey;
import iw.pollweb.model.dto.proxy.QuestionProxy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class QuestionDAO_MySQL extends DataAccessObject implements QuestionDAO {
  private PreparedStatement getIDs, selectQuestionByID, selectQuestionsBySurvey, insertQuestion, updateQuestion, deleteQuestion;

  public QuestionDAO_MySQL (DataLayer dataLayer) {
    super(dataLayer);
  }

  @Override
  public void init () throws DataException {

    try {
      super.init();
      // Precompilo le query
      getIDs = connection.prepareStatement("SELECT id FROM question");
      selectQuestionByID = connection.prepareStatement("SELECT * FROM question WHERE id=?");
      selectQuestionsBySurvey = connection.prepareStatement("SELECT * FROM question WHERE surveyID=?");
      insertQuestion = connection.prepareStatement("INSERT INTO question (type, text, note, isMandatory, number, surveyID) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      updateQuestion = connection.prepareStatement("UPDATE question SET type=?, text=?, note=?, isMandatory=?, number=?, surveyID=? WHERE id=?");
      deleteQuestion = connection.prepareStatement("DELETE FROM question WHERE id=?");

    } catch (SQLException e) {
      throw new DataException("Errore inizializzazione Data Layer", e);
    }
  }

  @Override
  public void destroy () throws DataException {

    // Chiudo i PreparedStatement
    try {
      getIDs.close();
      selectQuestionByID.close();
      selectQuestionsBySurvey.close();
      insertQuestion.close();
      updateQuestion.close();
      deleteQuestion.close();

    } catch (SQLException e) {
      throw new DataException(e);
    }
    super.destroy();
  }

  @Override
  public QuestionProxy createQuestion () {
    return new QuestionProxy(getDataLayer());
  }

  @Override
  public QuestionProxy createQuestionFromRS (ResultSet rs) throws DataException {
    QuestionProxy question = createQuestion();

    try {
      question.setID(rs.getInt("id"));
      question.setType(rs.getString("type"));
      question.setText(rs.getString("text"));
      question.setNote(rs.getString("note"));
      question.setMandatory(rs.getBoolean("isMandatory"));
      question.setNumber(rs.getInt("number"));
      question.setSurveyID(rs.getInt("surveyID"));

    } catch (SQLException e) {
      throw new DataException("Errore creazione Question", e);
    }
    return question;
  }

  @Override
  public void storeQuestion (Question question) throws DataException {
    int id = question.getID();

    try {
      if (question.getID() > 0) {
        // Non eseguo operazioni di aggiornamento se il proxy non presenta modifiche
        if (question instanceof QuestionProxy && !((QuestionProxy) question).isDirty()) {
          return;
        }
        // UPDATE question SET type=?, text=?, note=?, isMandatory=?, number=?, surveyID=? WHERE id=?
        updateQuestion.setString(1, question.getType());
        updateQuestion.setString(2, question.getText());
        updateQuestion.setString(3, question.getNote());
        updateQuestion.setBoolean(4, question.isMandatory());
        updateQuestion.setInt(5, question.getNumber());
        if (question.getSurvey() != null) {
          updateQuestion.setInt(6, question.getSurvey().getID());
        } else {
          updateQuestion.setNull(6, Types.INTEGER);
        }
        updateQuestion.setInt(7, question.getID());
        updateQuestion.executeUpdate();

      } else {
        // INSERT INTO question (type, text, note, isMandatory, number, surveyID) VALUES (?, ?, ?, ?, ?, ?)
        insertQuestion.setString(1, question.getType());
        insertQuestion.setString(2, question.getText());
        insertQuestion.setString(3, question.getNote());
        insertQuestion.setBoolean(4, question.isMandatory());
        insertQuestion.setInt(5, question.getNumber());
        if (question.getSurvey() != null) {
          insertQuestion.setInt(6, question.getSurvey().getID());
        } else {
          insertQuestion.setNull(6, Types.INTEGER);
        }
        if (insertQuestion.executeUpdate() == 1) {
          // Leggo la chiave generata dal DB per la precedente INSERT
          try (ResultSet rs = insertQuestion.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // Aggiorno la chiave
          question.setID(id);
        }
      }
      // Resetto l'attributo dirty del proxy
      if (question instanceof QuestionProxy) {
        ((QuestionProxy) question).setDirty(false);
      }
    } catch (SQLException e) {
      throw new DataException("Errore salvataggio Question", e);
    }
  }

  public void storeQuestions (List<Question> questions) throws DataException {
    for (Question question : questions) {
      storeQuestion(question);
    }
  }

  @Override
  public Question getQuestionByID (int id) throws DataException {

    // SELECT * FROM question WHERE id=?
    try {
      selectQuestionByID.setInt(1, id);
      try (ResultSet rs = selectQuestionByID.executeQuery()) {
        if (rs.next()) {
          return createQuestionFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Question", e);
    }
    return null;
  }

  @Override
  public List<Question> getQuestions () throws DataException {
    List<Question> questions = new ArrayList<>();

    // SELECT id FROM question
    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        questions.add(getQuestionByID(rs.getInt("id")));
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Question", e);
    }
    return questions;
  }

  @Override
  public List<Question> getQuestionsBySurvey (Survey survey) throws DataException {
    List<Question> questions = new ArrayList<>();

    // SELECT * FROM question WHERE surveyID=?
    try {
      selectQuestionsBySurvey.setInt(1, survey.getID());
      try (ResultSet rs = selectQuestionsBySurvey.executeQuery()) {
        while (rs.next()) {
          questions.add(getQuestionByID(rs.getInt("id")));
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Question", e);
    }
    return questions;
  }

  @Override
  public void deleteQuestion (int id) throws DataException {

    // DELETE FROM question WHERE id=?
    try {
      deleteQuestion.setInt(1, id);
      deleteQuestion.executeUpdate();

    } catch (SQLException e) {
      throw new DataException("Errore eliminazione Question", e);
    }
  }
}
