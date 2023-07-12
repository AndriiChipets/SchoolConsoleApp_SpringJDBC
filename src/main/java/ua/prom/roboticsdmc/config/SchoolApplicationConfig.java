package ua.prom.roboticsdmc.config;

import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ua.prom.roboticsdmc.dao.ConnectorDB;

@Configuration
@ComponentScan(basePackages = "ua.prom.roboticsdmc")
public class SchoolApplicationConfig {

    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }

    @Bean
    public ConnectorDB getConnectorDB() {
        return new ConnectorDB("database");
    }
}
