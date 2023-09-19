package com.github.furkanzhlp.orbalance;

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
        return this.connection;
    }

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }
    public Double getBalance(UUID uuid){
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table+" WHERE player_uuid = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public void saveBalance(UUID uuid, double balance) {
        try (Connection connection = getConnection()) {
            // Check if player data exists
            PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE player_uuid = ?");
            selectStatement.setString(1, uuid.toString());
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                // Player data exists, so update it
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE " + table + " SET balance = ? WHERE player_uuid = ?");
                updateStatement.setDouble(1, balance);
                updateStatement.setString(2, uuid.toString());
                updateStatement.executeUpdate();
            } else {
                // Player data doesn't exist, so insert it
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO " + table + " (player_uuid, balance) VALUES (?, ?)");
                insertStatement.setString(1, uuid.toString());
                insertStatement.setDouble(2, balance);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
