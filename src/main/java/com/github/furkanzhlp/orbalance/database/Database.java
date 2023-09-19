package com.github.furkanzhlp.orbalance.database;

import org.bukkit.configuration.ConfigurationSection;

import java.sql.*;
import java.util.UUID;

public class Database {
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final String table;
    private final int port;
    private Connection connection;

    public Database(ConfigurationSection databaseConfig) {
        this.host = databaseConfig.getString("host");
        this.database = databaseConfig.getString("database");
        this.username = databaseConfig.getString("username");
        this.password = databaseConfig.getString("password");
        this.port = databaseConfig.getInt("port");
        this.table = databaseConfig.getString("table");
    }

    public void checkTables(){
        try (Connection connection = getConnection()) {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS orbalance_balances (" +
                    "player_uuid VARCHAR(255) PRIMARY KEY," +
                    "balance DOUBLE" +
                    ")").execute();
        }catch (SQLException ignored){}
    }
    public boolean isConnected() {
        return this.connection != null;
    }

    public void openConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database+"?useUnicode=true&characterEncoding=utf-8", this.username, this.password);
    }

    public void refreshConnect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database+"?useUnicode=true&characterEncoding=utf-8", this.username, this.password);
    }

    public void disconnect() {
        if (this.isConnected()) {
            try {
                this.connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        try {
            if (!this.connection.isValid(1)) {
                try {
                    this.openConnection();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (SQLException e2) {
            e2.printStackTrace();
        }
        try (PreparedStatement stmt = this.connection.prepareStatement("SELECT 1")) {
            stmt.executeQuery();
        }
        catch (SQLException e2) {
            try {
                this.openConnection();
            }
            catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return this.connection;
    }

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }

    public String getTable() {
        return table;
    }
}
