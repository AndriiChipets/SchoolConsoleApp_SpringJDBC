package ua.prom.roboticsdmc.config;

import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "ua.prom.roboticsdmc")
@PropertySource("database.properties")
public class SchoolApplicationConfig {

    @Bean
    DataSource dataSource(
            @Value("${db.url}")String url,
            @Value("${db.user}") String user,
            @Value("${db.password}") String password, 
            @Value("${db.cachePrepStmts.param}") String cachePrepStmtsParam,
            @Value("${db.cachePrepStmts.value}") String cachePrepStmtsValue,
            @Value("${db.prepStmtCacheSize.param}") String prepStmtCacheSizeParam, 
            @Value("${db.prepStmtCacheSize.value}") String prepStmtCacheSizeValue,
            @Value("${db.prepStmtCacheSqlLimit.param}") String prepStmtCacheSqlLimitParam,
            @Value("${db.prepStmtCacheSqlLimit.value}") String prepStmtCacheSqlLimitValue,
            @Value("${db.maximumPoolSize}") int maximumPoolSize) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty(cachePrepStmtsParam, cachePrepStmtsValue);
        config.addDataSourceProperty(prepStmtCacheSizeParam, prepStmtCacheSizeValue);
        config.addDataSourceProperty(prepStmtCacheSqlLimitParam, prepStmtCacheSqlLimitValue);
        config.setMaximumPoolSize(maximumPoolSize);
        
        return new HikariDataSource(config);
    }

    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
