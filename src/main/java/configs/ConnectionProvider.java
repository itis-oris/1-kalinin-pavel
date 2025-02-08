package configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ConnectionProvider {
    private String url = "jdbc:postgresql://localhost:5432/quotes";
    private String user = "postgres";
    private String password = "q2f0i0f5";

    private static ConnectionProvider _instance;
    private Connection con;
    private static HikariDataSource dataSource;

    final static Logger logger = LogManager.getLogger(ConnectionProvider.class);

    public static ConnectionProvider getInstance() {
        if (_instance == null) {
            synchronized (ConnectionProvider.class) {
                if (_instance == null) {
                    _instance = new ConnectionProvider();
                }
            }
        }

        return _instance;
    }

    private ConnectionProvider(){
        try {
            Class.forName("org.postgresql.Driver");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            config.setConnectionTimeout(50000);
            config.setMaximumPoolSize(20);
            dataSource = new HikariDataSource(config);

            Flyway flyway = Flyway.configure().dataSource(dataSource).load();
            logger.info("start migration");
            flyway.migrate();
            logger.info("finish migration");
        } catch (ClassNotFoundException  e) {
            throw new IllegalArgumentException(e);
        }
    }

    public synchronized Connection getCon() throws SQLException {
        Connection connection = dataSource.getConnection();
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public void destroy() {
        dataSource.close();
    }
}
