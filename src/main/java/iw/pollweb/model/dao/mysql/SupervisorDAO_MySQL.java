package iw.pollweb.model.dao.mysql;

import iw.framework.data.DataAccessObject;
import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.SupervisorDAO;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.proxy.SupervisorProxy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class SupervisorDAO_MySQL extends DataAccessObject implements SupervisorDAO {
  private PreparedStatement getIDs, selectSupervisorByID, selectSupervisorByEmailPassword, insertSupervisor, updateSupervisor, deleteSupervisorByID;

  public SupervisorDAO_MySQL (DataLayer dataLayer) {
    super(dataLayer);
  }

  @Override
  public void init () throws DataException {
    try {
      super.init();
      // Precompilo le query
      getIDs = connection.prepareStatement("SELECT id FROM supervisor");
      selectSupervisorByID = connection.prepareStatement("SELECT * FROM supervisor WHERE id=?");
      selectSupervisorByEmailPassword = connection.prepareStatement("SELECT * FROM supervisor WHERE email=? AND hashedPassword=?");
      insertSupervisor = connection.prepareStatement("INSERT INTO supervisor (firstName, lastName, email, hashedPassword) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      updateSupervisor = connection.prepareStatement("UPDATE supervisor set firstName=?, lastName=?, email=?, hashedPassword=? WHERE id=?");
      deleteSupervisorByID = connection.prepareStatement("DELETE FROM supervisor WHERE id=?");

    } catch (SQLException e) {
      throw new DataException("Errore inizializzazione Data Layer", e);
    }
  }

  @Override
  public void destroy () throws DataException {
    // Chiudo i PreparedStatement
    try {
      getIDs.close();
      selectSupervisorByID.close();
      selectSupervisorByEmailPassword.close();
      insertSupervisor.close();
      updateSupervisor.close();
      deleteSupervisorByID.close();

    } catch (SQLException e) {
      throw new DataException(e);
    }
    super.destroy();
  }

  @Override
  public SupervisorProxy createSupervisor () {
    return new SupervisorProxy(getDataLayer());
  }

  @Override
  public SupervisorProxy createSupervisorFromRS (ResultSet rs) throws DataException {
    try {
      SupervisorProxy supervisor = createSupervisor();

      supervisor.setFirstName(rs.getString("firstName"));
      supervisor.setLastName(rs.getString("lastName"));
      supervisor.setEmail(rs.getString("email"));
      supervisor.setHashedPassword(rs.getString("hashedPassword"));

      return supervisor;

    } catch (SQLException e) {
      throw new DataException("Errore creazione Supervisor", e);
    }
  }

  @Override
  public int authenticateSupervisor (Supervisor supervisor) throws DataException {
    try {
      selectSupervisorByEmailPassword.setString(1, supervisor.getEmail());
      selectSupervisorByEmailPassword.setString(2, supervisor.getHashedPassword());

      try (ResultSet rs = selectSupervisorByEmailPassword.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore autenticazione Supervisor", e);
    }
    return 0;
  }

  @Override
  public void storeSupervisor (Supervisor supervisor) throws DataException {
    int id = supervisor.getID();

    try {
      if (supervisor.getID() > 0) { //UPDATE
        // Non eseguo operazioni se il proxy non presenta modifiche
        if (supervisor instanceof SupervisorProxy && ((SupervisorProxy) supervisor).isDirty()) {
          return;
        }
        updateSupervisor.setString(1, supervisor.getFirstName());
        updateSupervisor.setString(2, supervisor.getLastName());
        updateSupervisor.setString(3, supervisor.getEmail());
        updateSupervisor.setString(4, supervisor.getHashedPassword());
        updateSupervisor.executeUpdate();
      } else { //INSERT
        insertSupervisor.setString(1, supervisor.getFirstName());
        insertSupervisor.setString(2, supervisor.getLastName());
        insertSupervisor.setString(3, supervisor.getEmail());
        insertSupervisor.setString(4, supervisor.getHashedPassword());

        if (insertSupervisor.executeUpdate() == 1) {
          // Leggo la chiave generata dal DB per la precedente INSERT
          try (ResultSet rs = insertSupervisor.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // Aggiorno la chiave
          supervisor.setID(id);
        }
      }
      // Resetto l'attributo dirty del proxy
      if (supervisor instanceof SupervisorProxy) {
        ((SupervisorProxy) supervisor).setDirty(false);
      }
    } catch (SQLException e) {
      throw new DataException("Errore salvataggio Supervisor", e);
    }

  }

  @Override
  public Supervisor getSupervisorByID (int id) throws DataException {
    try {
      selectSupervisorByID.setInt(1, id);
      try (ResultSet rs = selectSupervisorByID.executeQuery()) {
        if (rs.next()) {
          return createSupervisorFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Supervisor", e);
    }
    return null;
  }

  @Override
  public List<Supervisor> getSupervisors () throws DataException {
    List<Supervisor> supervisors = new ArrayList<>();

    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        supervisors.add(getSupervisorByID(rs.getInt("id")));
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Supervisor", e);
    }
    return supervisors;
  }

  @Override
  public void deleteSupervisor (int id) throws DataException {
    try {
      deleteSupervisorByID.setInt(1, id);
      deleteSupervisorByID.executeUpdate();

    } catch (SQLException e) {
      throw new DataException("Errore eliminazione Supervisor", e);
    }
  }
}
