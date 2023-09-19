package com.github.furkanzhlp.orbalance.database;

import com.github.furkanzhlp.orbalance.OrbalancePlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseHandler {



    public static Double getBalance(UUID uuid){
        OrbalancePlugin plugin = OrbalancePlugin.getInstance();
        try {
            PreparedStatement preparedStatement = plugin.database.getConnection().prepareStatement("SELECT * FROM "+plugin.database.getTable()+" WHERE player_uuid = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
            return 0.0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void saveBalance(UUID uuid, double balance) {
        OrbalancePlugin plugin = OrbalancePlugin.getInstance();
        try  {
            // Check if player data exists
            PreparedStatement selectStatement = plugin.database.getConnection().prepareStatement("SELECT * FROM " + plugin.database.getTable() + " WHERE player_uuid = ?");
            selectStatement.setString(1, uuid.toString());
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                // Player data exists, so update it
                PreparedStatement updateStatement = plugin.database.getConnection().prepareStatement("UPDATE " + plugin.database.getTable() + " SET balance = ? WHERE player_uuid = ?");
                updateStatement.setDouble(1, balance);
                updateStatement.setString(2, uuid.toString());
                updateStatement.executeUpdate();
            } else {
                // Player data doesn't exist, so insert it
                PreparedStatement insertStatement = plugin.database.getConnection().prepareStatement("INSERT INTO " + plugin.database.getTable() + " (player_uuid, balance) VALUES (?, ?)");
                insertStatement.setString(1, uuid.toString());
                insertStatement.setDouble(2, balance);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
