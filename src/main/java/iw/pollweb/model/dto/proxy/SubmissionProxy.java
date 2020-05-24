package iw.pollweb.model.dto.proxy;

import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.ParticipantDAO;
import iw.pollweb.model.dao.ResponseDAO;
import iw.pollweb.model.dao.SurveyDAO;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Response;
import iw.pollweb.model.dto.Submission;
import iw.pollweb.model.dto.Survey;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class SubmissionProxy extends Submission {
  protected boolean dirty;
  protected int surveyID;
  protected int participantID;
  protected DataLayer dataLayer;

  public SubmissionProxy (DataLayer dataLayer) {
    super();
    // Dependency Injection
    this.dataLayer = dataLayer;
    this.dirty = false;
    this.surveyID = 0;
    this.participantID = 0;
  }

  @Override
  public void setID (int id) {
    super.setID(id);
    this.dirty = true;
  }

  @Override
  public void setTimestamp (Timestamp timestamp) {
    super.setTimestamp(timestamp);
    this.dirty = true;
  }

  @Override
  public Survey getSurvey () {
    if (super.getSurvey() == null && surveyID > 0) {
      try {
        super.setSurvey(((SurveyDAO) dataLayer.getDAO(Survey.class)).getSurveyByID(surveyID));

      } catch (DataException e) {
        Logger.getLogger(SubmissionProxy.class.getName()).log(Level.SEVERE, null, e);
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
  public Participant getParticipant () {
    if (super.getParticipant() != null && participantID > 0) {
      try {
        super.setParticipant(((ParticipantDAO) dataLayer.getDAO(Participant.class)).getParticipantByID(participantID));

      } catch (DataException e) {
        Logger.getLogger(ParticipantProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getParticipant();
  }

  @Override
  public void setParticipant (Participant participant) {
    super.setParticipant(participant);
    if (participant != null) {
      this.participantID = participant.getID();

    } else {
      this.participantID = 0;
    }
    this.dirty = true;
  }

  @Override
  public List<Response> getResponses () {
    if (super.getResponses() == null) {
      try {
        super.setResponses(((ResponseDAO) dataLayer.getDAO(Response.class)).getResponsesBySubmission(this));

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

  public void setParticipantID (int participantID) {
    this.participantID = participantID;
    // Resetto la cache dell'oggetto Participant
    super.setParticipant(null);
  }
}
