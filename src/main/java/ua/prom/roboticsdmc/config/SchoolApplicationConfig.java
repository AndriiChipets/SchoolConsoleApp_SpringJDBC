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
    
    @Value("${db.user}")
    private String user;
    @Value("${db.password}")
    private String password;
    @Value("${db.url}")
    private String url;
    @Value("${db.cachePrepStmts.param}")
    private String cachePrepStmtsParam;
    @Value("${db.cachePrepStmts.value}")
    private String cachePrepStmtsValue;
    @Value("${db.prepStmtCacheSize.param}")
    private String prepStmtCacheSizeParam;
    @Value("${db.prepStmtCacheSize.value}")
    private String prepStmtCacheSizeValue;
    @Value("${db.prepStmtCacheSqlLimit.param}")
    private String prepStmtCacheSqlLimitParam;
    @Value("${db.prepStmtCacheSqlLimit.value}")
    private String prepStmtCacheSqlLimitValue;
    @Value("${db.maximumPoolSize}")
    private int maximumPoolSize;

    @Bean
    DataSource dataSource() {
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
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
