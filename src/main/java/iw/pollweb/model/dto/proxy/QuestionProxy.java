/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.model.dto.proxy;


import  iw.framework.data.DataException;
import  iw.framework.data.DataLayer;
import  iw.pollweb.model.dao.ChoiceDAO;
import  iw.pollweb.model.dao.ResponseDAO;
import  iw.pollweb.model.dao.SurveyDAO;
import  iw.pollweb.model.dto.Choice;
import  iw.pollweb.model.dto.Question;
import  iw.pollweb.model.dto.Response;
import  iw.pollweb.model.dto.Survey;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class QuestionProxy extends Question {
  protected boolean dirty;
  protected int surveyID;
  protected DataLayer dataLayer;

  public QuestionProxy (DataLayer dataLayer) {
    super();
    //dependency injection
    this.dataLayer = dataLayer;
    this.dirty = false;
    this.surveyID = 0;
  }

  @Override
  public void setID (int id) {
    super.setID(id);
    this.dirty = true;
  }

  @Override
  public void setType (String type) {
    super.setType(type);
    this.dirty = true;
  }

  @Override
  public void setText (String text) {
    super.setText(text);
    this.dirty = true;
  }

  @Override
  public void setNote (String note) {
    super.setNote(note);
    this.dirty = true;
  }

  @Override
  public void setMandatory (boolean mandatory) {
    super.setMandatory(mandatory);
    this.dirty = true;
  }

  @Override
  public void setNumber (int number) {
    super.setNumber(number);
    this.dirty = true;
  }

  @Override
  public Survey getSurvey () {
    if (super.getSurvey() == null && surveyID > 0) {
      try {
        super.setSurvey(((SurveyDAO) dataLayer.getDAO(Survey.class)).getSurveyByID(surveyID));
      } catch (DataException e) {
        Logger.getLogger(QuestionProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getSurvey();
  }

  @Override
  public void setSurvey (Survey survey) {
    super.setSurvey(survey);
    if (survey != null) {
      this.surveyID = survey.getID();

    } else {
      this.surveyID = 0;
    }
    this.dirty = true;
  }

  @Override
  public List<Choice> getChoices () {
    if (super.getChoices() == null) {
      try {
        super.setChoices(((ChoiceDAO) dataLayer.getDAO(Choice.class)).getChoicesByQuestion(this));

      } catch (DataException e) {
        Logger.getLogger(QuestionProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getChoices();
  }

  @Override
  public void setChoices (List<Choice> choices) {
    super.setChoices(choices);
    this.dirty = true;
  }

  @Override
  public void addChoice (Choice choice) {
    super.addChoice(choice);
    this.dirty = true;
  }

  @Override
  public List<Response> getResponses () {
    if (super.getResponses() == null) {
      try {
        super.setResponses(((ResponseDAO) dataLayer.getDAO(Response.class)).getResponsesByQuestion(this));

      } catch (DataException e) {
        Logger.getLogger(QuestionProxy.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    return super.getResponses();
  }

  @Override
  public void setResponses (List<Response> responses) {
    super.setResponses(responses);
    this.dirty = true;
  }

  @Override
  public void addResponse (Response response) {
    super.addResponse(response);
    this.dirty = true;
  }

  // metodi del proxy
  public boolean isDirty () {
    return dirty;
  }

  public void setDirty (boolean dirty) {
    this.dirty = dirty;
  }

  public void setSurveyID (int surveyID) {
    this.surveyID = surveyID;
    //resetto la cache dell'oggetto Survey
    super.setSurvey(null);
  }
}
