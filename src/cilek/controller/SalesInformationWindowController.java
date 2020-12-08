package cilek.controller;

import cilek.ProductManagement;
import cilek.model.Product;
import cilek.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SalesInformationWindowController extends BaseController implements Initializable {
    public SalesInformationWindowController(ProductManagement stationaryManagement, ViewFactory viewFactory, String fxmlName) {
        super(stationaryManagement, viewFactory, fxmlName);
    }

    @FXML
    private TableView<Product> orderTable;

    @FXML
    private TableColumn<Product, Integer> orderIdColumn;

    @FXML
    private TableColumn<Product, String> DateColumn;

    @FXML
    private TableColumn<Product, Void> orderDetails; // FOR BUTTON

    @FXML
    private TableColumn<?, ?> TotalPriceColumn;

    @FXML
    private TextField totalSales;

    @FXML
    void resetSales() throws SQLException {
        stationaryManagement.loadDailySaleInformation();
        stationaryManagement.resetSalesList();
        totalSales.setText("");

    }


    private void fillTableWData() throws SQLException {
        orderTable.setItems(stationaryManagement.loadSalesInformation());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            totalSales.setText(stationaryManagement.LoadTotalSales());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        orderIdColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 25);
        DateColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 35);
        orderDetails.setMaxWidth( 1f * Integer.MAX_VALUE * 25); // BUTTON
        TotalPriceColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 15);

        try {
            fillTableWData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("TimeofOrder"));
        TotalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> productVoidTableColumn) {
                final TableCell<Product, Void> cell = new TableCell<Product, Void>(){
                    private final Button btn = new Button("SİPARİŞ DETAYI");
                    {



                        btn.setOnAction((ActionEvent event) -> {

                            int orderId = getTableView().getItems().get(getIndex()).getNo();
                            try {
                                stationaryManagement.loadDetails(orderId);
                                System.out.println(stationaryManagement.LoadDetails().toString());
                                viewFactory.showOrderDetailsWindow();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        orderDetails.setCellFactory(cellFactory);


    }
}