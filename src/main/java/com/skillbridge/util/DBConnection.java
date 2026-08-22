package com.skillbridge.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBConnection provides centralized, thread-safe JDBC connection management 
 * for PostgreSQL database connectivity using db.properties configuration.
 */
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final Properties PROPERTIES = new Properties();

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static String dbDriver;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                LOGGER.log(Level.SEVERE, "db.properties file not found in classpath. Falling back to default configuration.");
                // Fallbacks
                dbDriver = "org.postgresql.Driver";
                dbUrl = "jdbc:postgresql://localhost:5432/skillbridge";
                dbUser = "postgres";
                dbPassword = "root";
            } else {
                PROPERTIES.load(is);
                dbDriver = PROPERTIES.getProperty("db.driver", "org.postgresql.Driver");
                dbUrl = PROPERTIES.getProperty("db.url", "jdbc:postgresql://localhost:5432/skillbridge");
                dbUser = PROPERTIES.getProperty("db.user", "postgres");
                dbPassword = PROPERTIES.getProperty("db.password", "root");
            }

            // Register PostgreSQL Driver
            Class.forName(dbDriver);
            LOGGER.info("PostgreSQL JDBC Driver successfully registered.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading db.properties", e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "PostgreSQL JDBC Driver class not found: " + dbDriver, e);
        }
    }

    /**
     * Private constructor to prevent direct instantiation (Singleton Utility pattern).
     */
    private DBConnection() {}

    /**
     * Returns a new active SQL Connection to the PostgreSQL database.
     * 
     * @return java.sql.Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to obtain PostgreSQL database connection: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Closes the given Connection safely.
     * 
     * @param connection the SQL Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }

    /**
     * Closes multiple AutoCloseable database resources (Statements, ResultSets, Connections).
     * 
     * @param resources varargs of AutoCloseable resources
     */
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error closing database resource: " + resource.getClass().getSimpleName(), e);
                }
            }
        }
    }

    /**
     * Quick test utility to verify connectivity from the command line or during deployment.
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isValid = conn != null && !conn.isClosed();
            if (isValid) {
                LOGGER.info("SUCCESS: Database connection to 'skillbridge' established successfully.");
            }
            return isValid;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "FAILURE: Could not connect to PostgreSQL database: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing SkillBridge Database Connection...");
        boolean success = testConnection();
        if (success) {
            System.out.println(">>> Database connected successfully! Ready for Phase 2.");
        } else {
            System.err.println(">>> Database connection failed. Please ensure PostgreSQL is running and 'skillbridge' database exists.");
        }
    }
}
