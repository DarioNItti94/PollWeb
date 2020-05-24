package iw.framework.data;

/**
 * @author Primiano Medugno
 * @since 01/02/2020
 */

public class DataException extends Exception {

  public DataException (String message) {
    super(message);
  }

  public DataException (String message, Throwable cause) {
    super(message, cause);
  }

  public DataException (Throwable cause) {
    super(cause);
  }

  @Override
  public String getMessage () {
    return super.getMessage() + (getCause() != null ? " (" + getCause().getMessage() + ")" : "");
  }
}
