package crud.clinica.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import crud.clinica.config.DatabaseConfig;

public class DatabaseManager {

    private static Connection connection;
    private static DatabaseConfig config;

    public static void setupDatabase() throws Exception {
        config = new DatabaseConfig("src/dbconfig.txt");
        
        String urlSemDb = String.format("jdbc:mysql://%s:%s/?useSSL=false&allowPublicKeyRetrieval=true", 
                                        config.getDbAddress(), config.getDbPort());
        
        try (Connection conn = DriverManager.getConnection(urlSemDb, config.getDbUser(), config.getDbPassword());
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + config.getDbName());
        }

        String urlComDb = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true", 
                                        config.getDbAddress(), config.getDbPort(), config.getDbName());
        
        try (Connection conn = DriverManager.getConnection(urlComDb, config.getDbUser(), config.getDbPassword());
             Statement stmt = conn.createStatement()) {
            
            String sqlPacientes = "CREATE TABLE IF NOT EXISTS pacientes ( id BIGINT PRIMARY KEY AUTO_INCREMENT, nome VARCHAR(255) NOT NULL, cpf VARCHAR(14) NOT NULL UNIQUE, data_nascimento DATETIME NOT NULL )";
            stmt.executeUpdate(sqlPacientes);

            String sqlExames = "CREATE TABLE IF NOT EXISTS exames ( id BIGINT PRIMARY KEY AUTO_INCREMENT, descricao VARCHAR(255) NOT NULL, data_exame DATE NOT NULL, paciente_id BIGINT NOT NULL, FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE )";
            stmt.executeUpdate(sqlExames);
        }
    }

    public static class MySQLConnection implements IConnection {
        @Override
        public Connection getConnection() throws SQLException {
            if (connection == null || connection.isClosed()) {
                String urlComDb = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true", 
                                                config.getDbAddress(), config.getDbPort(), config.getDbName());
                connection = DriverManager.getConnection(urlComDb, config.getDbUser(), config.getDbPassword());
            }
            return connection;
        }

        @Override
        public void closeConnection() throws SQLException {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
}