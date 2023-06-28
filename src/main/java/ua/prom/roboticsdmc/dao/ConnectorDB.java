package ua.prom.roboticsdmc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectorDB {

    private final String user;
    private final String password;
    private final String url;
    private final String cachePrepStmtsParam;
    private final String cachePrepStmtsValue;
    private final String prepStmtCacheSizeParam;
    private final String prepStmtCacheSizeValue;
    private final String prepStmtCacheSqlLimitParam;
    private final String prepStmtCacheSqlLimitValue;
    private final int MaximumPoolSize;
    private HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    public ConnectorDB(String propertyFileName) {
        ResourceBundle resource = ResourceBundle.getBundle(propertyFileName);
        this.user = resource.getString("db.user");
        this.password = resource.getString("db.password");
        this.url = resource.getString("db.url");
        this.cachePrepStmtsParam = resource.getString("db.cachePrepStmts.param");
        this.cachePrepStmtsValue = resource.getString("db.cachePrepStmts.value");
        this.prepStmtCacheSizeParam = resource.getString("db.prepStmtCacheSize.param");
        this.prepStmtCacheSizeValue = resource.getString("db.prepStmtCacheSize.value");
        this.prepStmtCacheSqlLimitParam = resource.getString("db.prepStmtCacheSqlLimit.param");
        this.prepStmtCacheSqlLimitValue = resource.getString("db.prepStmtCacheSqlLimit.value");
        this.MaximumPoolSize = Integer.parseInt(resource.getString("db.MaximumPoolSize"));
        initializeDataSourse();
    }

    private void initializeDataSourse() {
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty(cachePrepStmtsParam, cachePrepStmtsValue);
        config.addDataSourceProperty(prepStmtCacheSizeParam, prepStmtCacheSizeValue);
        config.addDataSourceProperty(prepStmtCacheSqlLimitParam, prepStmtCacheSqlLimitValue);
        config.setMaximumPoolSize(MaximumPoolSize);
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
