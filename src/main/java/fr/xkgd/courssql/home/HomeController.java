package fr.xkgd.courssql.home;

import fr.xkgd.courssql.connection.ConnectionController;
import fr.xkgd.courssql.query.QueryController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.Scanner;

public class HomeController {

    @FXML
    public TableView<String> propertiesTableView;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ConnectionController connectionController;
    private QueryController queryController;

    public void initialize() {
        if (connectionController != null && queryController != null) {
        }
    }

    @FXML
    public void onMakeQueryBtn(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/xkgd/courssql/query/QueryView.fxml"));
            root = loader.load();
            queryController = loader.getController();
            queryController.setConnectionController(connectionController);
            queryController.initialize();
            stage = new Stage();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConnectionController(ConnectionController connectionController) {
        this.connectionController = connectionController;
        System.out.println(this.connectionController.isConnected());
    }
}
