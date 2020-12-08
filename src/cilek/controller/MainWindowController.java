package cilek.controller;

import cilek.ProductManagement;
import cilek.model.Product;
import cilek.view.ViewFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


import java.net.URL;
import java.sql.SQLException;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {
    private int amount;
    private String name;
    private int id;
    private double price;
    private String barcodeNumber;
    private int quantity;
    private ArrayList<String> barcodeScanner = new ArrayList<>();

    public MainWindowController(ProductManagement stationaryManagement, ViewFactory viewFactory, String fxmlName) {
        super(stationaryManagement, viewFactory, fxmlName);
    }

    @FXML
    private TableView<Product> tableFieldS;
    @FXML
    private TableColumn<?, ?> noColumn;
    @FXML
    private TableColumn<?,?> nameColumn;
    @FXML
    private TableColumn<?, ?> barcodeNumberColumn;
    @FXML
    private TableColumn<?, ?> priceColumn;
    @FXML
    private TableColumn<?, ?> amountColumn;
    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private MenuBar menuField;

    @FXML
    private ListView<String> listField;

    @FXML
    private TextField sumField;

    @FXML
    private TextField paymentAmountField;

    @FXML
    private TextField changeField;

    @FXML
    private TextField scanBarcode;

    @FXML
    void allStockWindowField() {
        viewFactory.showAllStockWindow();
    }

    @FXML
    void quitSystem() {
        System.exit(1);
    }

    @FXML
    void cashButtonAction() throws SQLException, ParseException {
        if((paymentAmountField.getText().equals(changeField.getText())) && (paymentAmountField.getText().equals(""))){
            stationaryManagement.showAlert(Alert.AlertType.ERROR,"ÖDEME TUTARI GİRİNİZ");
        }
        else {
            if (Double.valueOf(paymentAmountField.getText()) < Double.valueOf(sumField.getText())) {
                stationaryManagement.showAlert(Alert.AlertType.ERROR, "ÖDEME MİKTARI TUTARDAN AZ");

            } else  {
                stationaryManagement.RemainingStock();
                stationaryManagement.showAlert(Alert.AlertType.INFORMATION, "ÖDEME TAMAMLANDI");
                paymentAmountField.setText("0.0");
                sumField.setText("0.0");
                changeField.setText("0.0");
            }
        }
    }
    @FXML
    void stockWindowField( ) {
        viewFactory.showStockWindow();
    }
    @FXML
    void quantityAction() throws SQLException {
        stationaryManagement.loadOrder(barcodeNumber);
        calculateOrder();
    }
    @FXML
    void decreaseQuantity() throws SQLException {
        stationaryManagement.decreaseOrder(barcodeNumber);
        calculateOrder();
    }
    @FXML
    void takeOutProduct() {
        stationaryManagement.takeOutProduct(barcodeNumber);
        calculateOrder();
    }
    public void calculateOrder(){
        sumField.setText(String.valueOf(stationaryManagement.sum()));

    }
    public void update(String barcode) throws SQLException, InterruptedException {
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        barcodeNumberColumn.setCellValueFactory(new PropertyValueFactory<>("barcodeNumber"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableFieldS.setItems(stationaryManagement.loadOrder(barcode));



    }

    public void clickProduct(){
        tableFieldS.setRowFactory( e ->{
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(tv ->{
                Product rowData = row.getItem();
                name = rowData.getName();
                barcodeNumber = rowData.getBarcodeNumber();
                amount = rowData.getAmount();
                price = rowData.getPrice();
                id = rowData.getNo();
                quantity = rowData.getQuantity();
            });
            return row;
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableFieldS.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        noColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 4);
        nameColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 62);
        barcodeNumberColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        priceColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 9 );
        amountColumn.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );

        clickProduct();

        paymentAmountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try{ double change = Double.parseDouble(observableValue.getValue()) - Double.parseDouble(sumField.getText());
                    if (change >= 0) {
                        changeField.setText(String.valueOf(change));
                    }} catch(Exception e){
                    stationaryManagement.showAlert(Alert.AlertType.ERROR,"HATALI GİRİŞ.LÜTFEN KONTROL EDİNİZ.");
                }


            }
        });

    }
    @FXML
    void keyPressedAction(KeyEvent event) throws SQLException, InterruptedException {
       if(event.getCode() == KeyCode.ENTER){
          update(scanBarcode.getText());
          scanBarcode.setText("");
          calculateOrder();
       }

    }

    @FXML
    void salesInformation() {
        viewFactory.showSalesInformationWindow();
    }


}