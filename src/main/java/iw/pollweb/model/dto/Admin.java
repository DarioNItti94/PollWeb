package iw.pollweb.model.dto;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */

public class Admin {
  private int id;
  private String email;
  private String hashedPassword;

  // Costruttori
  public Admin () {
  }

  public Admin (int id, String email, String hashedPassword) {
    this.id = id;
    this.email = email;
    this.hashedPassword = hashedPassword;
  }

  // Getter & Setter
  public int getID () {
    return id;
  }

  public void setID (int id) {
    this.id = id;
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
}
