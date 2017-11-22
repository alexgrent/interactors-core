package org.reactome.server.interactors.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Guilherme S Viteri (gviteri@ebi.ac.uk)
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public class InteractorDatabaseGenerator {

    private static final Logger logger = LoggerFactory.getLogger(InteractorDatabaseGenerator.class);

    public static void create(Connection connection) {
        create(connection, true);
    }

    public static void create(Connection connection, boolean close) {
        logger.info("Creating interactors database.");

        try {
            Statement statement = connection.createStatement();

            // Create our tables
            logger.info("Creating table Interactor_Resource.");
            statement.executeUpdate(QueryStatement.CREATE_TABLE_INTERACTOR_RESOURCE);

            logger.info("Creating table Interaction Resource");
            statement.executeUpdate(QueryStatement.CREATE_TABLE_INTERACTION_RESOURCE);

            logger.info("Creating interactor");
            statement.executeUpdate(QueryStatement.CREATE_TABLE_INTERACTOR);

            logger.info("Creating interaction");
            statement.executeUpdate(QueryStatement.CREATE_TABLE_INTERACTION);

            logger.info("Creating participants");
            statement.executeUpdate(QueryStatement.CREATE_TABLE_INTERACTION_DETAILS);

            // Create indexes
            logger.info("Creating indexes");
            statement.executeUpdate(QueryStatement.CREATE_INTERACTOR_ACC_INDEX);
            statement.executeUpdate(QueryStatement.CREATE_INTERACTOR_A_INDEX);
            statement.executeUpdate(QueryStatement.CREATE_INTERACTOR_B_INDEX);
            statement.executeUpdate(QueryStatement.CREATE_INTERACTION_DETAILS_ID_INDEX);

            // Pre-populate tables
            logger.info("Populate table interaction resource");
            statement.executeUpdate(QueryStatement.INSERT_INTERACTION_RESOURCE_STATIC);

            logger.info("Populate table interactor resource");
            statement.executeUpdate(QueryStatement.INSERT_INTERACTOR_RESOURCE_UNDEFINED);
            statement.executeUpdate(QueryStatement.INSERT_INTERACTOR_RESOURCE_UNIPROT);
            statement.executeUpdate(QueryStatement.INSERT_INTERACTOR_RESOURCE_CHEBI);

            logger.info("Database has been created properly");

        } catch (SQLException e) {
            logger.error("Error creating interactor database", e);
        } catch (Exception e) {
            logger.error("Generic exception occurred. Please check stacktrace for further information", e);
        } finally {
            if (close) {
                try {
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing database connection", e);
                }
            }
        }
    }
}

