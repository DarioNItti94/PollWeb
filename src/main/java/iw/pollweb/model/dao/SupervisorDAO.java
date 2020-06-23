package iw.pollweb.model.dao;

import iw.framework.data.DataException;
import iw.pollweb.model.dto.Supervisor;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */
public interface SupervisorDAO {

    // Utility
    Supervisor createSupervisor();

    Supervisor createSupervisorFromRS(ResultSet rs) throws DataException;

    public Supervisor getSupervisorByEmail(String email) throws DataException;

    int authenticateSupervisor(Supervisor supervisor) throws DataException;

    // CRUD
    void storeSupervisor(Supervisor supervisor) throws DataException;

    Supervisor getSupervisorByID(int id) throws DataException;

    List<Supervisor> getSupervisors() throws DataException;

    void deleteSupervisor(int id) throws DataException;
}
