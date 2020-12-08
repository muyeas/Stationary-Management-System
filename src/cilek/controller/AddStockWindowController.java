package cilek.controller;

import cilek.ProductManagement;
import cilek.model.Product;
import cilek.view.ViewFactory;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddStockWindowController extends BaseController {

    public AddStockWindowController(ProductManagement stationaryManagement, ViewFactory viewFactory, String fxmlName) {
        super(stationaryManagement, viewFactory, fxmlName);
    }

    @FXML
    private TextField nameField;

    @FXML
    private TextField barcodeNumberField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField takingPriceField;

    @FXML
    void addButtonActions() throws SQLException {


        try{
            Product product = new Product(0,nameField.getText().toUpperCase(),barcodeNumberField.getText(),(Integer.parseInt(quantityField.getText())),(Double.parseDouble(priceField.getText())),(Double.parseDouble(takingPriceField.getText())),(Integer.parseInt(quantityField.getText())));
            stationaryManagement.addProduct(product);
            viewFactory.closeStage((Stage) priceField.getScene().getWindow());
        }
        catch (Exception e){
            stationaryManagement.showAlert(Alert.AlertType.ERROR,"AYNI ÜRÜN VAR VE YA  EKSİK BİLGİ VE YA YANLIŞ DEĞER");
        }

    }
}
