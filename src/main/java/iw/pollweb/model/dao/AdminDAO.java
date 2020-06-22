package iw.pollweb.model.dao;

import iw.framework.data.DataException;
import iw.pollweb.model.dto.Admin;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public interface AdminDAO {

  // Utility
  Admin createAdmin ();
  Admin createAdminFromRS (ResultSet rs) throws DataException;
  int authenticateAdmin (Admin admin) throws DataException;

  // CRUD
  void storeAdmin (Admin admin) throws DataException;
  Admin getAdminByID (int id) throws DataException;
  List<Admin> getAdmins () throws DataException;
  void deleteAdmin (int id) throws DataException;
}
