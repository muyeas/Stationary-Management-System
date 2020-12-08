package cilek.controller;

import cilek.ProductManagement;
import cilek.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrderDetailsWindowController extends BaseController implements Initializable {
    public OrderDetailsWindowController(ProductManagement stationaryManagement, ViewFactory viewFactory, String fxmlName) {
        super(stationaryManagement, viewFactory, fxmlName);
    }

    @FXML
    private ListView<?> orderDetailsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            orderDetailsList.getItems().addAll(stationaryManagement.LoadDetails());
        } catch (SQLException e) {
            e.printStackTrace();
        };
    }
}
