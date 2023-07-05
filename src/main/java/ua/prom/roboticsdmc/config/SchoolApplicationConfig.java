package ua.prom.roboticsdmc.config;

import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua.prom.roboticsdmc.controller.FrontController;
import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.dao.impl.CourseDaoImpl;
import ua.prom.roboticsdmc.dao.impl.GroupDaoImpl;
import ua.prom.roboticsdmc.dao.impl.StudentDaoImpl;
import ua.prom.roboticsdmc.view.ViewProvider;

@Configuration
public class SchoolApplicationConfig {

    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }

    @Bean
    public ViewProvider getViewProvider() {
        return new ViewProvider(getScanner());
    }

    @Bean
    public ConnectorDB getConnectorDB() {
        return new ConnectorDB("database");
    }

    @Bean
    public StudentDao getStudentDao() {
        return new StudentDaoImpl(getConnectorDB());
    }

    @Bean
    public CourseDao getCourseDao() {
        return new CourseDaoImpl(getConnectorDB());
    }

    @Bean
    public GroupDao getGroupDao() {
        return new GroupDaoImpl(getConnectorDB());
    }

    @Bean
    public FrontController getFrontController() {
        return new FrontController(getStudentDao(), getCourseDao(), getGroupDao(), getViewProvider());
    }
}
