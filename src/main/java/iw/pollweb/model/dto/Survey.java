package iw.pollweb.model.dto;

import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class Survey {
  private int id;
  private String title;
  private String openingText;
  private String closingText;
  private boolean isReserved;
  private boolean isActive;
  private boolean isClosed;
  private Supervisor supervisor;
  private List<Question> questions;
  private List<Participant> participants;
  private List<Submission> submissions;

  // Costruttori
  public Survey () {}
  public Survey (int id, String title, String openingText, String closingText, boolean isReserved, boolean isActive, boolean isClosed, Supervisor supervisor, List<Question> questions, List<Participant> participants, List<Submission> submissions) {
    this.id = id;
    this.title = title;
    this.openingText = openingText;
    this.closingText = closingText;
    this.isReserved = isReserved;
    this.isActive = isActive;
    this.isClosed = isClosed;
    this.supervisor = supervisor;
    this.questions = questions;
    this.participants = participants;
    this.submissions = submissions;
  }

  // Getter & Setter
  public int getID () {
    return id;
  }

  public void setID (int id) {
    this.id = id;
  }

  public String getTitle () {
    return title;
  }

  public void setTitle (String title) {
    this.title = title;
  }

  public String getOpeningText () {
    return openingText;
  }

  public void setOpeningText (String openingText) {
    this.openingText = openingText;
  }

  public String getClosingText () {
    return closingText;
  }

  public void setClosingText (String closingText) {
    this.closingText = closingText;
  }

  public boolean isReserved () {
    return isReserved;
  }

  public void setReserved (boolean reserved) {
    isReserved = reserved;
  }

  public boolean isActive () {
    return isActive;
  }

  public void setActive (boolean active) {
    isActive = active;
  }

  public boolean isClosed () {
    return isClosed;
  }

  public void setClosed (boolean closed) {
    isClosed = closed;
  }

  public Supervisor getSupervisor () {
    return supervisor;
  }

  public void setSupervisor (Supervisor supervisor) {
    this.supervisor = supervisor;
  }

  public List<Question> getQuestions () {
    return questions;
  }

  public void setQuestions (List<Question> questions) {
    this.questions = questions;
  }

  public void addQuestion (Question question) {
    this.questions.add(question);
  }

  public List<Participant> getParticipants () {
    return participants;
  }

  public void setParticipants (List<Participant> participants) {
    this.participants = participants;
  }

  public void addParticipant (Participant participant) {
    this.participants.add(participant);
  }

  public List<Submission> getSubmissions () {
    return submissions;
  }

  public void setSubmissions (List<Submission> submissions) {
    this.submissions = submissions;
  }

  public void addSubmission (Submission submission) {
    this.submissions.add(submission);
  }
}
