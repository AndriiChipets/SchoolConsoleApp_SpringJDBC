package ua.prom.roboticsdmc.tableutility;

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

public final class TableUtility {

    public static final ConnectorDB CONNECTOR_DB_TEST = new ConnectorDB("databaseH2");
    private static final String SCHEMA_FILE_PATH = "src/test/resources/sql/schemaH2.sql";
    private static final String DATA_GROUP_FILE_PATH = "src/test/resources/sql/dataGroup.sql";
    private static final String DATA_STUDENT_FILE_PATH = "src/test/resources/sql/dataStudent.sql";
    private static final String DATA_COURSE_FILE_PATH = "src/test/resources/sql/dataCourse.sql";
    private static final String DATA_STUDENT_COURSE_PATH = "src/test/resources/sql/dataStudentCourse.sql";
    
    private TableUtility() {
    }

    private static void createTable(String filePath) {

        try (Connection connection = CONNECTOR_DB_TEST.getConnection(); Statement statement = connection.createStatement()) {

            ScriptRunner scriptRunner = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader(filePath));
            scriptRunner.runScript(reader);

        } catch (SQLException | IOException e) {
            throw new DataBaseSqlRuntimeException("Schema and tables are not created or data are not filled..", e);
        }
    }
    
    public static void createTablesAndSchema () {
        createTable(SCHEMA_FILE_PATH);
    }
    
    public static void fillGroupTableDefaultData () {
        createTable(DATA_GROUP_FILE_PATH);
    }
    
    public static void fillStudentTableDefaultData () {
        createTable(DATA_STUDENT_FILE_PATH);
    }
    
    public static void fillCourseTableDefaultData () {
        createTable(DATA_COURSE_FILE_PATH);
    }
    
    public static void fillStudentCourseTableDefaultData () {
        createTable(DATA_STUDENT_COURSE_PATH);
    }
}
