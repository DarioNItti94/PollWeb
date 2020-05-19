/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.model.dto;


/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class Choice {
  private int id;
  private String value;
  private int number;
  private Question question;

  // costruttori
  public Choice () {
  }

  public Choice (int id, String value, int number, Question question) {
    this.id = id;
    this.value = value;
    this.number = number;
    this.question = question;
  }

  // getter e setter
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

  public int getNumber () {
    return number;
  }

  public void setNumber (int number) {
    this.number = number;
  }

  public Question getQuestion () {
    return question;
  }

  public void setQuestion (Question question) {
    this.question = question;
  }
}
