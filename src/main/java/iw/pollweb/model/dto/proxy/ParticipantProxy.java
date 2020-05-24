package iw.pollweb.model.dto.proxy;

import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.ResponseDAO;
import iw.pollweb.model.dao.SubmissionDAO;
import iw.pollweb.model.dao.SurveyDAO;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Response;
import iw.pollweb.model.dto.Submission;
import iw.pollweb.model.dto.Survey;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class ParticipantProxy extends Participant {
  protected boolean dirty;
  protected int surveyID;
  protected int submissionID;
  protected DataLayer dataLayer;

  public ParticipantProxy (DataLayer dataLayer) {
    super();
    // Dependency Injection
    this.dataLayer = dataLayer;
    this.dirty = false;
    this.surveyID = 0;
    this.submissionID = 0;
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
  public void setDisabled (boolean disabled) {
    super.setDisabled(disabled);
    this.dirty = true;
  }

  @Override
  public Survey getSurvey () {
    if (super.getSurvey() == null && surveyID > 0) {
      try {
        super.setSurvey(((SurveyDAO) dataLayer.getDAO(Survey.class)).getSurveyByID(surveyID));

      } catch (DataException e) {
        Logger.getLogger(ParticipantProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getSurvey();
  }

  @Override
  public void setSurvey (Survey survey) {
    super.setSurvey(survey);
    if (survey != null) {
      this.surveyID = survey.getID();

    } else {
      this.surveyID = 0;
    }
    this.dirty = true;
  }

  @Override
  public Submission getSubmission () {
    if (super.getSubmission() != null && submissionID > 0) {
      try {
        super.setSubmission(((SubmissionDAO) dataLayer.getDAO(Submission.class)).getSubmissionByID(submissionID));

      } catch (DataException e) {
        Logger.getLogger(ParticipantProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getSubmission();
  }

  @Override
  public void setSubmission (Submission submission) {
    super.setSubmission(submission);
    if (submission != null) {
      this.submissionID = submission.getID();

    } else {
      this.submissionID = 0;
    }
    this.dirty = true;
  }

  @Override
  public List<Response> getResponses () {
    if (super.getResponses() == null) {
      try {
        super.setResponses(((ResponseDAO) dataLayer.getDAO(Response.class)).getResponsesByParticipant(this));

      } catch (DataException e) {
        Logger.getLogger(ParticipantProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getResponses();
  }

  @Override
  public void setResponses (List<Response> responses) {
    super.setResponses(responses);
    this.dirty = true;
  }

  @Override
  public void addResponse (Response response) {
    super.addResponse(response);
    this.dirty = true;
  }

  // Metodi del proxy
  public boolean isDirty () {
    return dirty;
  }

  public void setDirty (boolean dirty) {
    this.dirty = dirty;
  }

  public void setSurveyID (int surveyID) {
    this.surveyID = surveyID;
    // Resetto la cache dell'oggetto Survey
    super.setSurvey(null);
  }

  public void setSubmissionID (int submissionID) {
    this.submissionID = submissionID;
    // Resetto la cache dell'oggetto Submission
    super.setSubmission(null);
  }
}
