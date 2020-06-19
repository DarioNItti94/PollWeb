package iw.pollweb.model.dto;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class Response {
  private int id;
  private String value;
  private Question question;
  private Participant participant;
  private Submission submission;

  // Costruttori
  public Response () {
  }

  public Response (int id, String value, Question question, Participant participant, Submission submission) {
    this.id = id;
    this.value = value;
    this.question = question;
    this.participant = participant;
    this.submission = submission;
  }

  // Getter & Setter
  public int getID () {
    return id;
  }

  public void setID (int id) {
    this.id = id;
  }

  public String getValue () {
    return value;
  }

  public void setValue (String value) {
    this.value = value;
  }

  public Question getQuestion () {
    return question;
  }

  public void setQuestion (Question question) {
    this.question = question;
  }

  public Participant getParticipant () {
    return participant;
  }

  public void setParticipant (Participant participant) {
    this.participant = participant;
  }

  public Submission getSubmission () {
    return submission;
  }

  public void setSubmission (Submission submission) {
    this.submission = submission;
  }
}
