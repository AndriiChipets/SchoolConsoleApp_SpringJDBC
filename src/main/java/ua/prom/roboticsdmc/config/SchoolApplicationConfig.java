package ua.prom.roboticsdmc.config;

import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "ua.prom.roboticsdmc")
@PropertySource("database.properties")
public class SchoolApplicationConfig {

    @Autowired
    private Environment enviroment;

    @Bean
    DataSource dataSource() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(enviroment.getProperty("db.url"));
        config.setUsername(enviroment.getProperty("db.user"));
        config.setPassword(enviroment.getProperty("db.password"));
        config.addDataSourceProperty(enviroment.getProperty("db.cachePrepStmts.param"),
                enviroment.getProperty("db.prepStmtCacheSize.value"));
        config.addDataSourceProperty(enviroment.getProperty("db.prepStmtCacheSize.param"),
                enviroment.getProperty("db.prepStmtCacheSize.value"));
        config.addDataSourceProperty(enviroment.getProperty("db.prepStmtCacheSqlLimit.param"),
                enviroment.getProperty("db.prepStmtCacheSqlLimit.value"));
        config.setMaximumPoolSize(Integer.valueOf(enviroment.getProperty("db.maximumPoolSize")));
        return new HikariDataSource(config);
    }

    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
