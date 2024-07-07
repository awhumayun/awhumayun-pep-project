package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public Account addAccount(Account account) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO Account(username, password) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int accountId = generatedKeys.getInt(1);
                return new Account(accountId, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println("Error in addAccount: " + e.getMessage());
        } finally {
            closeResources(connection, preparedStatement, generatedKeys);
        }

        return null;
    }

    public List<Account> getAllAccounts() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Account> accounts = new ArrayList<>();

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Account";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Account account = new Account(accountId, username, password);
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllAccounts: " + e.getMessage());
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }

        return accounts;
    }

    public Account login(Account account) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account.getAccount_id());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                
                return new Account(accountId, username, password);
            }
        } catch (SQLException e) {
            System.out.println("Error in login: " + e.getMessage());
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }

        return null;
    }

    private void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            } 

            if (preparedStatement != null) {
                preparedStatement.close();
            }
                
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error in closing resources: " + e.getMessage());
        }
    }
}
