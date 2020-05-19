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

public class Supervisor {
  private int id;
  private String firstName;
  private String lastName;
  private String email;
  private String hashedPassword;
  private List<Survey> surveys;

  // costruttori
  public Supervisor () {
  }

  public Supervisor (int id, String firstName, String lastName, String email, String hashedPassword, List<Survey> surveys) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.hashedPassword = hashedPassword;
    this.surveys = surveys;
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

  public List<Survey> getSurveys () {
    return surveys;
  }

  public void setSurveys (List<Survey> surveys) {
    this.surveys = surveys;
  }

  public void addSurvey (Survey survey) {
    this.surveys.add(survey);
  }
}
