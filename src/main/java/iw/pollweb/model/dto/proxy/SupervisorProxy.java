package iw.pollweb.model.dto.proxy;

import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.SurveyDAO;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class SupervisorProxy extends Supervisor {
  protected boolean dirty;
  protected DataLayer dataLayer;

  public SupervisorProxy (DataLayer dataLayer) {
    super();
    // Dependency Injection
    this.dataLayer = dataLayer;
    this.dirty = false;
  }

  @Override
  public void setID (int id) {
    super.setID(id);
    this.dirty = true;
  }

  @Override
  public void setFirstName (String firstName) {
    super.setFirstName(firstName);
    this.dirty = true;
  }

  @Override
  public void setLastName (String lastName) {
    super.setLastName(lastName);
    this.dirty = true;
  }

  @Override
  public void setEmail (String email) {
    super.setEmail(email);
    this.dirty = true;
  }

  @Override
  public void setHashedPassword (String hashedPassword) {
    super.setHashedPassword(hashedPassword);
    this.dirty = true;
  }

  @Override
  public List<Survey> getSurveys () {
    if (super.getSurveys() == null) {
      try {
        super.setSurveys(((SurveyDAO) dataLayer.getDAO(Survey.class)).getSurveysBySupervisor(this));

      } catch (DataException e) {
        Logger.getLogger(SupervisorProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getSurveys();
  }

  @Override
  public void setSurveys (List<Survey> surveys) {
    super.setSurveys(surveys);
    this.dirty = true;
  }

  @Override
  public void addSurvey (Survey survey) {
    super.addSurvey(survey);
    this.dirty = true;
  }

  // Metodi del proxy
  public boolean isDirty () {
    return dirty;
  }

  public void setDirty (boolean dirty) {
    this.dirty = dirty;
  }
}
