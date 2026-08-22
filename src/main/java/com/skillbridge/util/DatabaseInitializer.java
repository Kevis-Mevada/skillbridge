package com.skillbridge.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseInitializer provides a standalone helper to execute the schema.sql script 
 * and set up the PostgreSQL tables and seed data for SkillBridge.
 */
public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    public static boolean initializeDatabase() {
        LOGGER.info("Starting SkillBridge Database Initialization...");

        // Look for schema.sql in classpath or file system
        InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream("schema.sql");
        if (is == null) {
            // Also try to read from the project database/schema.sql path
            try {
                java.io.File file = new java.io.File("database/schema.sql");
                if (file.exists()) {
                    is = new java.io.FileInputStream(file);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Could not open database/schema.sql file", e);
            }
        }

        if (is == null) {
            LOGGER.log(Level.SEVERE, "schema.sql file could not be found.");
            return false;
        }

        StringBuilder sqlBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignore SQL comments
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }
                sqlBuilder.append(line).append("\n");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed reading schema.sql", e);
            return false;
        }

        String fullSql = sqlBuilder.toString();
        String[] statements = fullSql.split(";");

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            for (String singleSql : statements) {
                String trimmed = singleSql.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }

            LOGGER.info("Database schema initialized and seed data inserted successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing SQL schema script: " + e.getMessage(), e);
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" SkillBridge Database Setup & Connection Tester");
        System.out.println("==================================================");

        boolean connected = DBConnection.testConnection();
        if (!connected) {
            System.err.println("\n[ERROR] Could not connect to PostgreSQL. Please check:");
            System.err.println(" 1. Is PostgreSQL service running on localhost:5432?");
            System.err.println(" 2. Does database 'skillbridge' exist?");
            System.err.println(" 3. Are credentials in 'src/main/resources/db.properties' accurate (postgres/root)?");
            System.exit(1);
        }

        System.out.println("\n[INFO] Initializing tables and seed data...");
        boolean initialized = initializeDatabase();
        if (initialized) {
            System.out.println("\n[SUCCESS] Phase 1 Database Setup Complete!");
            System.out.println(" Default Admin: admin@skillbridge.com / admin123");
        } else {
            System.err.println("\n[ERROR] Schema initialization failed. Check logs above.");
        }
    }
}
