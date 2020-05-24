package iw.pollweb.model.dto.proxy;

import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.ParticipantDAO;
import iw.pollweb.model.dao.QuestionDAO;
import iw.pollweb.model.dao.SubmissionDAO;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Question;
import iw.pollweb.model.dto.Response;
import iw.pollweb.model.dto.Submission;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class ResponseProxy extends Response {
  protected boolean dirty;
  protected int questionID;
  protected int participantID;
  protected int submissionID;
  protected DataLayer dataLayer;

  public ResponseProxy (DataLayer dataLayer) {
    super();
    // Dependency Injection
    this.dataLayer = dataLayer;
    this.dirty = false;
    this.questionID = 0;
    this.participantID = 0;
    this.submissionID = 0;
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
  public Question getQuestion () {
    if (super.getQuestion() == null && questionID > 0) {
      try {
        super.setQuestion(((QuestionDAO) dataLayer.getDAO(Question.class)).getQuestionByID(questionID));

      } catch (DataException e) {
        Logger.getLogger(ResponseProxy.class.getName()).log(Level.SEVERE, null, e);
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

  @Override
  public Participant getParticipant () {
    if (super.getParticipant() != null && participantID > 0) {
      try {
        super.setParticipant(((ParticipantDAO) dataLayer.getDAO(Participant.class)).getParticipantByID(participantID));

      } catch (DataException e) {
        Logger.getLogger(ResponseProxy.class.getName()).log(Level.SEVERE, null, e);
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
  public Submission getSubmission () {
    if (super.getSubmission() != null && submissionID > 0) {
      try {
        super.setSubmission(((SubmissionDAO) dataLayer.getDAO(Submission.class)).getSubmissionByID(submissionID));

      } catch (DataException e) {
        Logger.getLogger(ResponseProxy.class.getName()).log(Level.SEVERE, null, e);
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
      this.participantID = 0;
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

  public void setParticipantID (int participantID) {
    this.participantID = participantID;
    // Resetto la cache dell'oggetto Survey
    super.setParticipant(null);
  }

  public void setSubmissionID (int submissionID) {
    this.submissionID = submissionID;
    // Resetto la cache dell'oggetto Survey
    super.setSubmission(null);
  }
}
