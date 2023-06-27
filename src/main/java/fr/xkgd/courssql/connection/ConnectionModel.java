package fr.xkgd.courssql.connection;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionModel {

    private String dbName;
    private String url;
    private String userName;
    private String password;
    private Connection connection;

    public ConnectionModel() {
        dbName = "";
        url = "";
        userName = "";
        password = "";
    }

    public void startConnection() {
        if (isLocalWebServerAvailable()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, userName, password);
            } catch (ClassNotFoundException ex) {
                System.out.println("Error: unable to load driver class!");
            } catch (SQLException ex) {
                System.out.println("Error: unable to connect to database!");
            }
        } else {
            System.out.println("Error: unable to connect to web server!");
        }
    }

    public void stopConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed!");
            }
        } catch (SQLException ex) {
            System.out.println("Error: unable to close connection!");
        }
    }

    private boolean isLocalWebServerAvailable() {
        try(Socket ignored = new Socket("localhost", 80)) {
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public ArrayList<String> getTables(String databaseName) {
        ArrayList<String> tables = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(databaseName, null, "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
            resultSet.close();
        } catch (SQLException ex) {
            System.out.println("Error getting tables: " + ex.getMessage());
        }
        return tables;
    }

    public boolean isAutoIncrementColumn(String tableName, String columnName) {
        boolean isAutoIncrement = false;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, columnName);
            if (resultSet.next()) {
                isAutoIncrement = resultSet.getString("IS_AUTOINCREMENT").equals("YES");
            }
            resultSet.close();
        } catch (SQLException ex) {
            System.out.println("Error getting auto increment column: " + ex.getMessage());
        }
        return isAutoIncrement;
    }

    public List<String> getColumnsFromResultSetMetadata(ResultSetMetaData resultSetMetaData) {
        List<String> columns = new ArrayList<>();
        try {
            int columnCount = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(resultSetMetaData.getColumnName(i));
            }

        } catch (SQLException ex) {
            System.out.println("Error getting columns: " + ex.getMessage());
        }
        return columns;
    }

    public List<List<String>> getRowsFromResultSet(ResultSet resultSet) {
        List<List<String>> rows = new ArrayList<>();
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getString(i));
                }
                rows.add(row);
            }
        } catch (SQLException ex) {
            System.out.println("Error getting rows: " + ex.getMessage());
        }
        return rows;
    }

    public ResultSet getResultSet(String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Error getting result set: " + ex.getMessage());
        }
        return resultSet;
    }

    public ArrayList<String> getColumns(String tableName) {
        ArrayList<String> columns = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(dbName, null, tableName, null);
            while (resultSet.next()) {
                columns.add(resultSet.getString("COLUMN_NAME"));
            }
            resultSet.close();
        } catch (SQLException ex) {
            System.out.println("Error getting columns: " + ex.getMessage());
        }
        return columns;
    }

    public void setDbName(String text) {
        this.dbName = text;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbUrl(String text) {
        this.url = text;
    }

    public String getDbUrl() {
        return url;
    }

    public void setDbUsername(String text) {
        this.userName = text;
    }

    public String getDbUsername() {
        return userName;
    }

    public void setDbPassword(String text) {
        this.password = text;
    }

    public String getDbPassword() {
        return password;
    }

    public Connection getConnection() {
        return connection;
    }
}
