package iw.pollweb.model.dto.proxy;

import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.QuestionDAO;
import iw.pollweb.model.dto.Choice;
import iw.pollweb.model.dto.Question;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class ChoiceProxy extends Choice {
  protected boolean dirty;
  protected int questionID;
  protected DataLayer dataLayer;

  public ChoiceProxy (DataLayer dataLayer) {
    super();
    // Dependency Injection
    this.dataLayer = dataLayer;
    this.dirty = false;
    this.questionID = 0;
  }

  @Override
  public void setID (int id) {
    super.setID(id);
    this.dirty = true;
  }

  @Override
  public void setValue (String value) {
    super.setValue(value);
    this.dirty = true;
  }

  @Override
  public void setNumber (int number) {
    super.setNumber(number);
    this.dirty = true;
  }

  @Override
  public Question getQuestion () {
    if (super.getQuestion() == null && questionID > 0) {
      try {
        super.setQuestion(((QuestionDAO) dataLayer.getDAO(Question.class)).getQuestionByID(questionID));

      } catch (DataException e) {
        Logger.getLogger(ChoiceProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getQuestion();
  }

  @Override
  public void setQuestion (Question question) {
    super.setQuestion(question);
    if (question != null) {
      this.questionID = question.getID();

    } else {
      this.questionID = 0;
    }
    this.dirty = true;
  }

  // Metodi del proxy
  public boolean isDirty () {
    return dirty;
  }

  public void setDirty (boolean dirty) {
    this.dirty = dirty;
  }

  public void setQuestionID (int questionID) {
    this.questionID = questionID;
    // Resetto la cache dell'oggetto Survey
    super.setQuestion(null);
  }
}
