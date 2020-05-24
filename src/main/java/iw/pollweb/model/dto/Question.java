package iw.pollweb.model.dto;

import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class Question {
  private int id;
  private String type;
  private String text;
  private String note;
  private boolean isMandatory;
  private int number;
  private Survey survey;
  private List<Choice> choices;
  private List<Response> responses;

  // Costruttori
  public Question () {}
  public Question (int id, String type, String text, String note, boolean isMandatory, int number, Survey survey, List<Choice> choices, List<Response> responses) {
    this.id = id;
    this.type = type;
    this.text = text;
    this.note = note;
    this.isMandatory = isMandatory;
    this.number = number;
    this.survey = survey;
    this.choices = choices;
    this.responses = responses;
  }

  // Getter & Setter
  public int getID () {
    return id;
  }

  public void setID (int id) {
    this.id = id;
  }

  public String getType () {
    return type;
  }

  public void setType (String type) {
    this.type = type;
  }

  public String getText () {
    return text;
  }

  public void setText (String text) {
    this.text = text;
  }

  public String getNote () {
    return note;
  }

  public void setNote (String note) {
    this.note = note;
  }

  public boolean isMandatory () {
    return isMandatory;
  }

  public void setMandatory (boolean mandatory) {
    isMandatory = mandatory;
  }

  public int getNumber () {
    return number;
  }

  public void setNumber (int number) {
    this.number = number;
  }

  public Survey getSurvey () {
    return survey;
  }

  public void setSurvey (Survey survey) {
    this.survey = survey;
  }

  public List<Choice> getChoices () {
    return choices;
  }

  public void setChoices (List<Choice> choices) {
    this.choices = choices;
  }

  public void addChoice (Choice choice) {
    this.choices.add(choice);
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
