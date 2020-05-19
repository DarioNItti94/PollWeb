/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.model.dto.proxy;

import iw.framework.data.DataLayer;
import iw.pollweb.model.dto.Admin;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class AdminProxy extends Admin {
  protected boolean dirty;
  protected DataLayer dataLayer;

  public AdminProxy (DataLayer dataLayer) {
    super();
    //dependency injection
    this.dataLayer = dataLayer;
    this.dirty = false;
  }

  @Override
  public void setID (int id) {
    super.setID(id);
    this.dirty = true;
  }

  @Override
  public void setEmail (String email) {
    super.setEmail(email);
    this.dirty = true;
  }

  @Override
  public void setHashedPassword (String hashedPassword) {
    super.setHashedPassword(hashedPassword);
    this.dirty = true;
  }

  // metodi del proxy
  public boolean isDirty () {
    return dirty;
  }

  public void setDirty (boolean dirty) {
    this.dirty = dirty;
  }
}
