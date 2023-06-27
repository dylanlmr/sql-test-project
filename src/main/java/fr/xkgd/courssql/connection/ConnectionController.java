package fr.xkgd.courssql.connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionController {

    private ConnectionModel connectionModel;

    public ConnectionController() {
        connectionModel = new ConnectionModel();
    }

    public void startConnection() {
        connectionModel.startConnection();
    }

    public void stopConnection() {
        connectionModel.stopConnection();
    }

    public boolean isConnected() {
        return connectionModel.isConnected();
    }

    public ArrayList<String> getTables(String databaseName) {
        return connectionModel.getTables(databaseName);
    }

    public List<String> getColumnsFromResultSetMetadata(ResultSetMetaData resultSetMetaData) {
        return connectionModel.getColumnsFromResultSetMetadata(resultSetMetaData);
    }

    public List<List<String>> getRowsFromResultSet(ResultSet resultSet) {
        return connectionModel.getRowsFromResultSet(resultSet);
    }

    public boolean isAutoIncrementColumn(String tableName, String columnName) {
        return connectionModel.isAutoIncrementColumn(tableName, columnName);
    }

    public Connection getConnection() {
        return connectionModel.getConnection();
    }

    public void setDbName(String text) {
        connectionModel.setDbName(text);
    }

    public String getDbName() {
        return connectionModel.getDbName();
    }

    public void setDbUrl(String text) {
        connectionModel.setDbUrl(text);
    }

    public void setDbUsername(String text) {
        connectionModel.setDbUsername(text);
    }

    public void setDbPassword(String text) {
        connectionModel.setDbPassword(text);
    }
}
