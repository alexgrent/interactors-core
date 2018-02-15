package org.reactome.server.interactors.dao.intact;

import org.reactome.server.interactors.dao.InteractorResourceDAO;
import org.reactome.server.interactors.database.InteractorsDatabase;
import org.reactome.server.interactors.model.InteractorResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guilherme S Viteri <gviteri@ebi.ac.uk>
 */

public class StaticInteractorResource implements InteractorResourceDAO {

    private final Logger logger = LoggerFactory.getLogger(StaticInteractorResource.class);

    private Connection connection;

    private String ALL_COLUMNS = "NAME, URL";
    private String ALL_COLUMNS_SEL = "ID, ".concat(ALL_COLUMNS);

    public StaticInteractorResource(InteractorsDatabase database) {
        this.connection = database.getConnection();
    }

    public List<InteractorResource> getAll() throws SQLException {
        logger.debug("Retrieving all InteractorResources");
        final String TABLE = "INTERACTOR_RESOURCE";
        List<InteractorResource> ret = new ArrayList<>();
        String query = "SELECT " + ALL_COLUMNS_SEL +
                        " FROM " + TABLE;

        PreparedStatement pstm = connection.prepareStatement(query);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            InteractorResource interactorResource = buildInteractorResource(rs);
            ret.add(interactorResource);
        }
        return ret;
    }

    private InteractorResource buildInteractorResource(ResultSet rs) throws SQLException {
        InteractorResource ret = new InteractorResource();
        ret.setId(rs.getLong("ID"));
        ret.setName(rs.getString("NAME"));
        ret.setUrl(rs.getString("URL"));
        return ret;
    }
}
