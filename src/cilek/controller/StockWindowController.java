package cilek.controller;

import cilek.ProductManagement;
import cilek.model.Product;
import cilek.view.ViewFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StockWindowController extends BaseController implements Initializable {
    private String barcodeNumber;
    private  String name;
    private int quantity;
    private double price;
    private int id;
    private double takingPrice;
    private int stock;

    public StockWindowController(ProductManagement stationaryManagement, ViewFactory viewFactory, String fxmlName) {
        super(stationaryManagement, viewFactory, fxmlName);
    }
    @FXML
    private TableView<Product> tableField;

    @FXML
    private TableColumn<?, ?> noColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> barcodeNumberColumn;

    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TableColumn<?, ?> takingPriceColumn;

    @FXML
    private TableColumn<?, ?> stockColumn;

    @FXML
    private TextField barcodeNumberField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField costField;

    @FXML
    private TextField stockField;

    @FXML
    private TextField takingCostField;

    @FXML
    private TextField searchWbarcode;


    @FXML
    void changeFeatrues() throws SQLException {
        try{
            stationaryManagement.changeProductFeature(id,nameField.getText(),barcodeNumberField.getText(),Integer.parseInt(quantityField.getText()),Double.parseDouble(costField.getText()),Double.parseDouble(takingCostField.getText()),Integer.parseInt(stockField.getText()));
            barcodeNumberField.setText("");
            quantityField.setText("");
            nameField.setText("");
            costField.setText("");
            takingCostField.setText("");
            stockField.setText("");

        } catch (Exception e){
            stationaryManagement.showAlert(Alert.AlertType.ERROR,"SEÇİLİ ÜRÜN BULUNMAMAKTADIR");
        }

    }
    @FXML
    void addProduct() {
        viewFactory.showAddStockWindow();
        barcodeNumberField.setText("");
        quantityField.setText("");
        nameField.setText("");
        costField.setText("");
        takingCostField.setText("");
        stockField.setText("");


    }
    @FXML
    void deleteProduct() throws SQLException {
        if(nameField.getText().equals("")){
            stationaryManagement.showAlert(Alert.AlertType.ERROR,"Silinecek ürün belirtilmedi.");
            return;
        }
        stationaryManagement.deleteProduct(barcodeNumber);
        barcodeNumberField.setText("");
        quantityField.setText("");
        nameField.setText("");
        costField.setText("");
        takingCostField.setText("");
        stockField.setText("");

    }
    public void updateTable(){
        //Set up columns in data
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        barcodeNumberColumn.setCellValueFactory(new PropertyValueFactory<>("barcodeNumber"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        takingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("takingPrice"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));


        try {
            tableField.setItems(stationaryManagement.load());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void clickProduct(){
        tableField.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(e ->{
                if(e.getClickCount() == 1 && (!row.isEmpty())){
                    Product rowData = row.getItem();
                    name = rowData.getName();
                    barcodeNumber = rowData.getBarcodeNumber();
                    quantity = rowData.getQuantity();
                    price = rowData.getPrice();
                    id = rowData.getNo();
                    takingPrice = rowData.getTakingPrice();
                    stock = rowData.getStock();

                    nameField.setText(name);
                    barcodeNumberField.setText(barcodeNumber);
                    quantityField.setText(String.valueOf(quantity));
                    costField.setText(String.valueOf(price));
                    takingCostField.setText(String.valueOf(takingPrice));
                    stockField.setText(String.valueOf(stock));
                }
            });
            return row;
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableField.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        noColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 ); // 50% width
        nameColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 33 ); // 30% width
        barcodeNumberColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 17 ); // 20% width
        quantityColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 10 ); // 20% width
        priceColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 13 ); // 20% width
        takingPriceColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 13);
        stockColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 9);

        updateTable();
        clickProduct();
        searchField();
    }
    public void searchField(){
        FilteredList<Product> filteredData = new FilteredList<>(stationaryManagement.getProductList(), b -> true);

        searchWbarcode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                filteredData.setPredicate(product -> {

                    if(t1 == null || t1.isEmpty()){
                        return true;
                    }
                    String lowercaseFilter = t1.toLowerCase();

                    if(product.getName().toLowerCase().indexOf(lowercaseFilter) != -1){
                        return true;
                    }
                    else if(product.getBarcodeNumber().toLowerCase().indexOf(lowercaseFilter) != -1){
                        return true;
                    }
                    else{
                        return false;
                    }
                });
            }
        });
        SortedList<Product> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableField.comparatorProperty());

        tableField.setItems(sortedData);
    }

}

