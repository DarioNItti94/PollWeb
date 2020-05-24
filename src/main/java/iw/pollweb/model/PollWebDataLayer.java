package iw.pollweb.model;

import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.*;
import iw.pollweb.model.dao.mysql.*;
import iw.pollweb.model.dto.*;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Primiano Medugno
 * @since 01/02/2020
 */

public class PollWebDataLayer extends DataLayer {

  public PollWebDataLayer (DataSource dataSource) throws SQLException {
    super(dataSource);
  }

  @Override
  public void init () throws DataException {
    // Registro i DAO
    registerDAO(Admin.class, new AdminDAO_MySQL(this));
    registerDAO(Choice.class, new ChoiceDAO_MySQL(this));
    registerDAO(Participant.class, new ParticipantDAO_MySQL(this));
    registerDAO(Question.class, new QuestionDAO_MySQL(this));
    registerDAO(Response.class, new ResponseDAO_MySQL(this));
    registerDAO(Submission.class, new SubmissionDAO_MySQL(this));
    registerDAO(Supervisor.class, new SupervisorDAO_MySQL(this));
    registerDAO(Survey.class, new SurveyDAO_MySQL(this));
  }

  // Helpers
  public AdminDAO getAdminDAO () {
    return (AdminDAO) getDAO(Admin.class);
  }

  public ChoiceDAO getChoiceDAO () {
    return (ChoiceDAO) getDAO(Choice.class);
  }

  public ParticipantDAO getParticipantDAO () {
    return (ParticipantDAO) getDAO(Participant.class);
  }

  public QuestionDAO getQuestionDAO () {
    return (QuestionDAO) getDAO(Question.class);
  }

  public ResponseDAO getResponseDAO () {
    return (ResponseDAO) getDAO(Response.class);
  }

  public SubmissionDAO getSubmissionDAO () {
    return (SubmissionDAO) getDAO(Response.class);
  }

  public SupervisorDAO getSupervisorDAO () {
    return (SupervisorDAO) getDAO(Supervisor.class);
  }

  public SurveyDAO getSurveyDAO () {
    return (SurveyDAO) getDAO(Survey.class);
  }
}
