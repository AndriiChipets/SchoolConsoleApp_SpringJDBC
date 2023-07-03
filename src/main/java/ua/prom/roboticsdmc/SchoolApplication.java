package ua.prom.roboticsdmc;

import java.util.Scanner;

import ua.prom.roboticsdmc.controller.FrontController;
import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.dao.impl.CourseDaoImpl;
import ua.prom.roboticsdmc.dao.impl.GroupDaoImpl;
import ua.prom.roboticsdmc.dao.impl.StudentDaoImpl;
import ua.prom.roboticsdmc.view.ViewProvider;

public class SchoolApplication {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            ViewProvider viewProvider = new ViewProvider(scanner);
            ConnectorDB connectorDB = new ConnectorDB("database");
            StudentDao studentDao = new StudentDaoImpl(connectorDB);
            CourseDao courseDao = new CourseDaoImpl(connectorDB);
            GroupDao groupDao = new GroupDaoImpl(connectorDB);

            FrontController frontController = new FrontController(studentDao, courseDao, groupDao, viewProvider);
            frontController.run();
        }
    }
}
