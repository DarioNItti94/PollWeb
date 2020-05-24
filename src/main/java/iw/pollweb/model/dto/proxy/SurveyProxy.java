package iw.pollweb.model.dto.proxy;

import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.ParticipantDAO;
import iw.pollweb.model.dao.QuestionDAO;
import iw.pollweb.model.dao.SubmissionDAO;
import iw.pollweb.model.dao.SupervisorDAO;
import iw.pollweb.model.dto.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class SurveyProxy extends Survey {
  protected boolean dirty;
  protected int supervisorID;
  protected DataLayer dataLayer;

  public SurveyProxy (DataLayer dataLayer) {
    super();
    // Dependency Injection
    this.dataLayer = dataLayer;
    this.dirty = false;
    this.supervisorID = 0;
  }

  @Override
  public void setID (int id) {
    super.setID(id);
    this.dirty = true;
  }

  @Override
  public void setTitle (String title) {
    super.setTitle(title);
    this.dirty = true;
  }

  @Override
  public void setOpeningText (String openingText) {
    super.setOpeningText(openingText);
    this.dirty = true;
  }

  @Override
  public void setClosingText (String closingText) {
    super.setClosingText(closingText);
    this.dirty = true;
  }

  @Override
  public void setReserved (boolean reserved) {
    super.setReserved(reserved);
    this.dirty = true;
  }

  @Override
  public void setActive (boolean active) {
    super.setActive(active);
    this.dirty = true;
  }

  @Override
  public void setClosed (boolean closed) {
    super.setClosed(closed);
    this.dirty = true;
  }

  @Override
  public Supervisor getSupervisor () {
    if (super.getSupervisor() == null && supervisorID > 0) {
      try {
        super.setSupervisor(((SupervisorDAO) dataLayer.getDAO(Supervisor.class)).getSupervisorByID(supervisorID));

      } catch (DataException e) {
        Logger.getLogger(SurveyProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getSupervisor();
  }

  @Override
  public void setSupervisor (Supervisor supervisor) {
    super.setSupervisor(supervisor);
    if (supervisor != null) {
      this.supervisorID = supervisor.getID();
    } else {
      this.supervisorID = 0;
    }
    this.dirty = true;
  }

  @Override
  public List<Question> getQuestions () {
    if (super.getQuestions() == null) {
      try {
        super.setQuestions(((QuestionDAO) dataLayer.getDAO(Question.class)).getQuestionsBySurvey(this));

      } catch (DataException e) {
        Logger.getLogger(SurveyProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getQuestions();
  }

  @Override
  public void setQuestions (List<Question> questions) {
    super.setQuestions(questions);
    this.dirty = true;
  }

  @Override
  public void addQuestion (Question question) {
    super.addQuestion(question);
    this.dirty = true;
  }

  @Override
  public List<Participant> getParticipants () {
    if (super.getParticipants() == null) {
      try {
        super.setParticipants(((ParticipantDAO) dataLayer.getDAO(Participant.class)).getParticipantsBySurvey(this));

      } catch (DataException e) {
        Logger.getLogger(SurveyProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getParticipants();
  }

  @Override
  public void setParticipants (List<Participant> participants) {
    super.setParticipants(participants);
    this.dirty = true;
  }

  @Override
  public void addParticipant (Participant participant) {
    super.addParticipant(participant);
    this.dirty = true;
  }

  @Override
  public List<Submission> getSubmissions () {
    if (super.getSubmissions() == null) {
      try {
        super.setSubmissions(((SubmissionDAO) dataLayer.getDAO(Submission.class)).getSubmissionsBySurvey(this));

      } catch (DataException e) {
        Logger.getLogger(SurveyProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getSubmissions();
  }

  @Override
  public void setSubmissions (List<Submission> submissions) {
    super.setSubmissions(submissions);
    this.dirty = true;
  }

  @Override
  public void addSubmission (Submission submission) {
    super.addSubmission(submission);
    this.dirty = true;
  }

  // Metodi del proxy
  public boolean isDirty () {
    return dirty;
  }

  public void setDirty (boolean dirty) {
    this.dirty = dirty;
  }

  public void setSupervisorID (int supervisorID) {
    this.supervisorID = supervisorID;
    // Resetto la cache dell'oggetto Supervisor
    super.setSupervisor(null);
  }
}
