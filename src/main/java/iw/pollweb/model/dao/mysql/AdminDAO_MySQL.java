package iw.pollweb.model.dao.mysql;

import iw.framework.data.DataAccessObject;
import iw.framework.data.DataException;
import iw.framework.data.DataLayer;
import iw.pollweb.model.dao.AdminDAO;
import iw.pollweb.model.dto.Admin;
import iw.pollweb.model.dto.proxy.AdminProxy;

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

public class AdminDAO_MySQL extends DataAccessObject implements AdminDAO {
  private PreparedStatement getIDs, selectAdminByID, selectAdminByEmailPassword, insertAdmin, updateAdmin, deleteAdminByID;

  public AdminDAO_MySQL (DataLayer dataLayer) {
    super(dataLayer);
  }

  @Override
  public void init () throws DataException {

    try {
      super.init();
      // Precompilo le query
      getIDs = connection.prepareStatement("SELECT id FROM admin");
      selectAdminByID = connection.prepareStatement("SELECT * FROM admin WHERE id=?");
      selectAdminByEmailPassword = connection.prepareStatement("SELECT * FROM admin WHERE email=? AND hashedPassword=?");
      insertAdmin = connection.prepareStatement("INSERT INTO admin (email, hashedPassword) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
      updateAdmin = connection.prepareStatement("UPDATE admin SET email=?, hashedPassword=? WHERE id=?");
      deleteAdminByID = connection.prepareStatement("DELETE FROM admin WHERE id=?");

    } catch (SQLException e) {
      throw new DataException("Errore inizializzazione Data Layer", e);
    }
  }

  @Override
  public void destroy () throws DataException {

    // Chiudo i PreparedStatement
    try {
      getIDs.close();
      selectAdminByID.close();
      selectAdminByEmailPassword.close();
      insertAdmin.close();
      updateAdmin.close();
      deleteAdminByID.close();

    } catch (SQLException e) {
      throw new DataException(e);
    }
    super.destroy();
  }

  @Override
  public AdminProxy createAdmin () {
    return new AdminProxy(getDataLayer());
  }

  @Override
  public Admin createAdminFromRS (ResultSet rs) throws DataException {

    try {
      AdminProxy admin = createAdmin();

      admin.setID(rs.getInt("id"));
      admin.setEmail(rs.getString("email"));
      admin.setHashedPassword(rs.getString("hashedPassword"));

      return admin;

    } catch (SQLException e) {
      throw new DataException("Errore creazione Admin", e);
    }
  }

  @Override
  public int authenticateAdmin (Admin admin) throws DataException {

    // SELECT * FROM admin WHERE email=? AND hashedPassword=?
    try {
      selectAdminByEmailPassword.setString(1, admin.getEmail());
      selectAdminByEmailPassword.setString(2, admin.getHashedPassword());

      try (ResultSet rs = selectAdminByEmailPassword.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore autenticazione Admin", e);
    }
    return 0;
  }

  @Override
  public void storeAdmin (Admin admin) throws DataException {
    int id = admin.getID();

    try {
      if (admin.getID() > 0) {
        // Non eseguo operazioni di aggiornamento se il proxy non presenta modifiche
        if (admin instanceof AdminProxy && !((AdminProxy) admin).isDirty()) {
          return;
        }
        // UPDATE admin SET email=?, hashedPassword=? WHERE id=?
        updateAdmin.setString(1, admin.getEmail());
        updateAdmin.setString(2, admin.getHashedPassword());
        updateAdmin.setInt(3, admin.getID());
        updateAdmin.executeUpdate();

      } else {
        // INSERT INTO admin (email, hashedPassword) VALUES (?, ?)
        insertAdmin.setString(1, admin.getEmail());
        insertAdmin.setString(2, admin.getHashedPassword());
        if (insertAdmin.executeUpdate() == 1) {
          // Leggo la chiave generata dal DB per la precedente INSERT
          try (ResultSet rs = insertAdmin.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // Aggiorno la chiave
          admin.setID(id);
        }
      }
      // Resetto l'attributo dirty del proxy
      if (admin instanceof AdminProxy) {
        ((AdminProxy) admin).setDirty(false);
      }
    } catch (SQLException e) {
      throw new DataException("Errore salvataggio Admin", e);
    }
  }

  @Override
  public Admin getAdminByID (int id) throws DataException {

    // SELECT * FROM admin WHERE id=?
    try {
      selectAdminByID.setInt(1, id);
      try (ResultSet rs = selectAdminByID.executeQuery()) {
        if (rs.next()) {
          return createAdminFromRS(rs);
        }
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Admin", e);
    }
    return null;
  }

  @Override
  public List<Admin> getAdmins () throws DataException {
    List<Admin> admins = new ArrayList<>();

    // SELECT id FROM admin
    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        admins.add(getAdminByID(rs.getInt("id")));
      }
    } catch (SQLException e) {
      throw new DataException("Errore caricamento Admin", e);
    }
    return admins;
  }

  @Override
  public void deleteAdmin (int id) throws DataException {

    // DELETE FROM admin WHERE id=?
    try {
      deleteAdminByID.setInt(1, id);
      deleteAdminByID.executeUpdate();

    } catch (SQLException e) {
      throw new DataException("Errore eliminazione Admin", e);
    }
  }
}
