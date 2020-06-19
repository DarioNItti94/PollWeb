package iw.pollweb.model.dto;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class Submission {
  private int id;
  private Timestamp timestamp;
  private Survey survey;
  private Participant participant;
  private List<Response> responses;

  // Costruttori
  public Submission () {
  }

  public Submission (int id, Timestamp timestamp, Survey survey, Participant participant, List<Response> responses) {
    this.id = id;
    this.timestamp = timestamp;
    this.survey = survey;
    this.participant = participant;
    this.responses = responses;
  }

  // Getter & Setter
  public int getID () {
    return id;
  }

  public void setID (int id) {
    this.id = id;
  }

  public Timestamp getTimestamp () {
    return timestamp;
  }

  public void setTimestamp (Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public Survey getSurvey () {
    return survey;
  }

  public void setSurvey (Survey survey) {
    this.survey = survey;
  }

  public Participant getParticipant () {
    return participant;
  }

  public void setParticipant (Participant participant) {
    this.participant = participant;
  }

  public List<Response> getResponses () {
    return responses;
  }

  public void setResponses (List<Response> responses) {
    this.responses = responses;
  }

  public void addResponse (Response response) {
    this.responses.add(response);
  }
}
