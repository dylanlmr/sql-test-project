package fr.xkgd.courssql;

import fr.xkgd.courssql.connection.ConnectionController;
import fr.xkgd.courssql.home.HomeController;
import fr.xkgd.courssql.query.QueryController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.ResultSet;

public class MainController {
    @FXML
    public CheckBox isLocalBtn;
    @FXML
    public Label dbTypeLabel;
    @FXML
    public ChoiceBox<String> dbTypeChoiceBox;
    @FXML
    public Label dbNameLabel;
    @FXML
    public TextField dbNameField;
    @FXML
    public Label dbUrlLabel;
    @FXML
    public TextField dbUrlField;
    @FXML
    public TextField dbUsernameField;
    @FXML
    public TextField dbPasswordField;

    private ConnectionController connectionController;

    public MainController() {
        connectionController = new ConnectionController();
    }

    public void initialize() {
        dbTypeChoiceBox.getItems().addAll("MySQL", "PostgreSQL");
        dbTypeChoiceBox.setValue("MySQL");
    }

    public void onConnectBtn(ActionEvent actionEvent) {
        connectionController.setDbName(dbNameField.getText());
        connectionController.setDbUrl(dbUrlField.getText());
        connectionController.setDbUsername(dbUsernameField.getText());
        connectionController.setDbPassword(dbPasswordField.getText());
        /*connectionController.setDbName("marieteam");
        connectionController.setDbUrl("jdbc:mysql://localhost:3306/marieteam?serverTimezone=UTC");
        connectionController.setDbUsername("root");
        connectionController.setDbPassword("");*/
        connectionController.startConnection();

        if (connectionController.isConnected()) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/xkgd/courssql/query/QueryView.fxml"));
                Parent root = loader.load();
                QueryController queryController = loader.getController();
                queryController.setConnectionController(connectionController);
                queryController.initialize();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onIsLocalBtn(ActionEvent actionEvent) {
        if (isLocalBtn.isSelected()) {
            dbTypeLabel.setDisable(false);
            dbTypeChoiceBox.setDisable(false);
            dbNameLabel.setDisable(false);
            dbNameField.setDisable(false);
            dbUrlLabel.setDisable(true);
            dbUrlField.setDisable(true);
            if (dbTypeChoiceBox.getValue().equals("MySQL")) {
                dbUrlField.setText("jdbc:mysql://localhost:3306/" + dbNameField.getText() + "?serverTimezone=UTC");
            } else if (dbTypeChoiceBox.getValue().equals("PostgreSQL")) {
                dbUrlField.setText("jdbc:postgresql://localhost:5432/" + dbNameField.getText());
            }
        } else {
            dbTypeLabel.setDisable(true);
            dbTypeChoiceBox.setDisable(true);
            dbNameLabel.setDisable(true);
            dbNameField.setDisable(true);
            dbUrlLabel.setDisable(false);
            dbUrlField.setDisable(false);
            dbUrlField.setText("");
        }
    }

    public void onDbTypeSelect(ActionEvent contextMenuEvent) {
        if (isLocalBtn.isSelected()) {
            if (dbTypeChoiceBox.getValue().equals("MySQL")) {
                dbUrlField.setText("jdbc:mysql://localhost:3306/" + dbNameField.getText() + "?serverTimezone=UTC");
            } else if (dbTypeChoiceBox.getValue().equals("PostgreSQL")) {
                dbUrlField.setText("jdbc:postgresql://localhost:5432/" + dbNameField.getText());
            }
        }
    }

    public void onDbNameTyping(KeyEvent actionEvent) {
        if (isLocalBtn.isSelected()) {
            if (dbTypeChoiceBox.getValue().equals("MySQL")) {
                dbUrlField.setText("jdbc:mysql://localhost:3306/" + dbNameField.getText() + "?serverTimezone=UTC");
            } else if (dbTypeChoiceBox.getValue().equals("PostgreSQL")) {
                dbUrlField.setText("jdbc:postgresql://localhost:5432/" + dbNameField.getText());
            }
        }
    }
}