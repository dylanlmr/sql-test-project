package fr.xkgd.courssql.query;

import fr.xkgd.courssql.connection.ConnectionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.sql.*;
import java.util.*;

public class QueryController {
    @FXML
    public ChoiceBox<String> tableChoiceBox;
    @FXML
    public Button selectAllBtn;
    @FXML
    public TableView<List<String>> selectTableView;
    @FXML
    public Pane insertHbox;

    private ConnectionController connectionController;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private List<String> columns;
    private List<List<String>> rows;

    public void initialize() {
        if (connectionController != null) {
            connection = connectionController.getConnection();
            System.out.println(connection != null ? "QueryController : Connection is not null" : "QueryController : Connection is null");

            if (connectionController.isConnected()) {
                tableChoiceBox.getItems().addAll(connectionController.getTables(connectionController.getDbName()));
                System.out.println("Connected to " + connectionController.getDbName());
                System.out.println("Tables: " + connectionController.getTables(connectionController.getDbName()));
            }
        }
    }

    @FXML
    public void onSelectTableChoiceBox(ActionEvent actionEvent) {
        if (tableChoiceBox.getValue() != null) {
            selectAllBtn.setDisable(false);

            removeAllRowsIfExist();
            removeAllColumnsIfExist();
            insertHbox.getChildren().clear();

            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM " + tableChoiceBox.getValue());
                columns = connectionController.getColumnsFromResultSetMetadata(resultSet.getMetaData());
                createColumnsFromDbColumnsList(columns);

                for (String column : columns) {
                    TextField textField = new TextField();
                    textField.setPromptText(column);
                    insertHbox.getChildren().add(textField);
                    HBox.setHgrow(textField, Priority.ALWAYS);

                    if (column.equalsIgnoreCase("id") && connectionController.isAutoIncrementColumn(tableChoiceBox.getValue(), column)) {
                        textField.setText("Auto-generated");
                        textField.setDisable(true);
                    }
                }

            } catch (SQLException ex) {
                System.out.println("Error while selecting all from table " + tableChoiceBox.getValue());
            }
        }
    }

    @FXML
    public void onSelectAllBtn(ActionEvent actionEvent) {
        rows = connectionController.getRowsFromResultSet(resultSet);

        if (tableChoiceBox.getValue() != null) {
            createObservableList(rows);
            selectAllBtn.setDisable(true);
        }
    }

    public ArrayList<String> getValuesFromInsertHbox() {
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < insertHbox.getChildren().size(); i++) {
            TextField textField = (TextField) insertHbox.getChildren().get(i);
            values.add(textField.getText());
        }
        return values;
    }

    private void addValuesToDatabase() throws SQLException {
        String tableName = tableChoiceBox.getValue();
        List<String> columns = connectionController.getColumnsFromResultSetMetadata(resultSet.getMetaData());
        List<String> values = getValuesFromInsertHbox();

        // Remove the 'id' column if it exists and is auto-generated
        boolean hasAutoIncrement = false;
        int idIndex = -1;
        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);
            if (column.equals("id") && resultSet.getMetaData().isAutoIncrement(i + 1)) {
                hasAutoIncrement = true;
                idIndex = i;
                break;
            }
        }
        if (hasAutoIncrement) {
            columns.remove(idIndex);
            values.remove(idIndex);
        }

        // create the INSERT INTO statement with placeholders for the values
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" (");
        for (String column : columns) {
            sqlBuilder.append(column);
            sqlBuilder.append(", ");
        }
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length()); // remove the last comma and space
        sqlBuilder.append(") VALUES (");
        sqlBuilder.append("?, ".repeat(columns.size()));
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length()); // remove the last comma and space
        sqlBuilder.append(")");

        String sql = sqlBuilder.toString();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < values.size(); i++) {
                statement.setString(i + 1, values.get(i)); // set the values for the placeholders
            }
            statement.executeUpdate();
            System.out.println("Values added to table " + tableName + ": " + values);
        } catch (SQLException ex) {
            System.out.println("Error while adding values to table " + tableName + ": " + values);
        }
    }

    private void createColumnsFromDbColumnsList(List<String> columns) {
        for (int i = 0; i < columns.size(); i++) {
            final int finalIdx = i;
            TableColumn<List<String>, String> column = new TableColumn<>(columns.get(i));
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(finalIdx)));
            selectTableView.getColumns().add(column);
            selectTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
    }

    private void removeAllColumnsIfExist()  {
        if (selectTableView.getColumns() != null) {
            selectTableView.getColumns().clear();
        }
    }

    private void removeAllRowsIfExist() {
        if (selectTableView.getItems() != null) {
            selectTableView.getItems().clear();
        }
    }

    private void createObservableList(List<List<String>> rows) {
        ObservableList<List<String>> observableList = FXCollections.observableArrayList(rows);
        selectTableView.setItems(observableList);
    }

    public void setConnectionController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }

    public void onInsertBtn(ActionEvent actionEvent) {
        try {
            addValuesToDatabase();
            onSelectTableChoiceBox(actionEvent);
        } catch (SQLException ex) {
            System.out.println("Error while adding values to database");
        }
    }

    public ConnectionController getConnectionController() {
        return connectionController;
    }

    public void onInputInPane(InputMethodEvent inputMethodEvent) {
        System.out.println("Input in pane");
    }
}
