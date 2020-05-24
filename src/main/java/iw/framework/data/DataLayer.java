package iw.framework.data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Primiano Medugno
 * @since 01/02/2020
 */

public class DataLayer implements AutoCloseable {
  private final DataSource dataSource;
  private final Map<Class, DataAccessObject> daos;
  private Connection connection;

  public DataLayer (DataSource dataSource) throws SQLException {
    super();
    this.dataSource = dataSource;
    this.connection = dataSource.getConnection();
    this.daos = new HashMap<>();
  }

  public void registerDAO (Class classEntity, DataAccessObject dao) throws DataException {
    daos.put(classEntity, dao);
    dao.init();
  }

  public DataAccessObject getDAO (Class entityClass) {
    return daos.get(entityClass);
  }

  public void init () throws DataException {
    //call registerDAO on your own DAOs
  }

  public void destroy () {
    try {
      if (connection != null) {
        connection.close();
        connection = null;
      }
    } catch (SQLException e) {
      //
    }
  }

  public DataSource getDataSource () {
    return dataSource;
  }

  public Connection getConnection () {
    return connection;
  }

  /*
    Metodo dell'interfaccia AutoCloseable che ci permette
    di usare il DataLayer nei try-with-resources
  */
  @Override
  public void close () throws Exception {
    destroy();
  }
}
