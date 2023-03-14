package sk.tuke.gamestudio_backend.databaseJDBC;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.gamestudio_backend.databaseJDBC.properties.AppProperties;

public class DBConnection {

    private static final Logger log = LoggerFactory.getLogger(DBConnection.class);

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;

    static {

        String url = AppProperties.getProperty("DB_URL");
        String username = AppProperties.getProperty("DB_USERNAME");
        String password = AppProperties.getProperty("DB_PASSWORD");

        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
        log.info("Database connection established");
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}