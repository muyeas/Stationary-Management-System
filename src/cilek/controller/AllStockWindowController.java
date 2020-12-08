package cilek.controller;

import cilek.ProductManagement;
import cilek.model.Product;
import cilek.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AllStockWindowController extends BaseController implements Initializable {
    public AllStockWindowController(ProductManagement stationaryManagement, ViewFactory viewFactory, String fxmlName) {
        super(stationaryManagement, viewFactory, fxmlName);
    }
    @FXML
    private TableView<Product> dmtableView;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> saleColumn;

    @FXML
    private TextField allStockTakingPrice;

    @FXML
    private TextField allStockPrice;


    private void loadAllStockTakingPrice() throws SQLException {
        allStockTakingPrice.setText(String.valueOf(stationaryManagement.loadAllStockPrices("takingprice")));
        allStockPrice.setText(String.valueOf(stationaryManagement.loadAllStockPrices("price")));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dmtableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dateColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 50);
        saleColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 50);

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("TimeofOrder"));
        saleColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        try {
            dmtableView.setItems(stationaryManagement.dmLoadInformation());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllStockTakingPrice();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
