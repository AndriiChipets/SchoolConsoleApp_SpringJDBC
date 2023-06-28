package ua.prom.roboticsdmc.tablecreator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.ibatis.jdbc.ScriptRunner;

import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;

public class TableCreatorImpl implements TableCreator {

    private final ConnectorDB connectorDB;

    public TableCreatorImpl(ConnectorDB connectorDB) {
        this.connectorDB = connectorDB;
    }

    @Override
    public void createTables(String schemaFilePath) {

        try (Connection connection = connectorDB.getConnection(); Statement statement = connection.createStatement()) {

            ScriptRunner scriptRunner = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader(schemaFilePath));
            scriptRunner.runScript(reader);

        } catch (SQLException | IOException e) {
            throw new DataBaseSqlRuntimeException("Schema and tables are not created..", e);
        }
    }
}
