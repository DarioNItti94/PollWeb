package iw.pollweb.model.dao;

import iw.framework.data.DataException;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Survey;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public interface ParticipantDAO {

  // Utility
  Participant createParticipant ();

  Participant createParticipantFromRS (ResultSet rs) throws DataException;

  int authenticateParticipant (Participant participant) throws DataException;

  // CRUD
  void storeParticipant (Participant participant) throws DataException;

  Participant getParticipantByID (int id) throws DataException;

  List<Participant> getParticipants () throws DataException;

  List<Participant> getParticipantsBySurvey (Survey survey) throws DataException;

  void deleteParticipant (int id) throws DataException;
}
