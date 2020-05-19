/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.model.dto;

import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class Participant {
  private int id;
  private String firstName;
  private String lastName;
  private String email;
  private String hashedPassword;
  private boolean isDisabled;
  private Survey survey;
  private Submission submission;
  private List<Response> responses;

  // costruttori
  public Participant () {
  }

  public Participant (int id, String firstName, String lastName, String email, String hashedPassword, boolean isDisabled, Survey survey, Submission submission, List<Response> responses) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.hashedPassword = hashedPassword;
    this.isDisabled = isDisabled;
    this.survey = survey;
    this.submission = submission;
    this.responses = responses;
  }

  // getter e setter
  public int getID () {
    return id;
  }

  public void setID (int id) {
    this.id = id;
  }

  public String getFirstName () {
    return firstName;
  }

  public void setFirstName (String firstName) {
    this.firstName = firstName;
  }

  public String getLastName () {
    return lastName;
  }

  public void setLastName (String lastName) {
    this.lastName = lastName;
  }

  public String getEmail () {
    return email;
  }

  public void setEmail (String email) {
    this.email = email;
  }

  public String getHashedPassword () {
    return hashedPassword;
  }

  public void setHashedPassword (String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public boolean isDisabled () {
    return isDisabled;
  }

  public void setDisabled (boolean disabled) {
    isDisabled = disabled;
  }

  public Survey getSurvey () {
    return survey;
  }

  public void setSurvey (Survey survey) {
    this.survey = survey;
  }

  public Submission getSubmission () {
    return submission;
  }

  public void setSubmission (Submission submission) {
    this.submission = submission;
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
