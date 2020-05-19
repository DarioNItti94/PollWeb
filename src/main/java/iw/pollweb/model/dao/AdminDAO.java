/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.model.dao;


import  iw.framework.data.DataException;
import  iw.pollweb.model.dto.Admin;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public interface AdminDAO {

  // utility
  Admin createAdmin ();

  Admin createAdminFromRS (ResultSet rs) throws DataException;

  int authenticateAdmin (Admin admin) throws DataException;

  // CRUD
  void storeAdmin (Admin admin) throws DataException;

  Admin getAdminByID (int id) throws DataException;

  List<Admin> getAdmins () throws DataException;

  void deleteAdmin (int id) throws DataException;
}
