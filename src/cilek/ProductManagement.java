package cilek;

import cilek.model.Product;
import cilek.model.ProductTableLoad;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;


import java.sql.SQLException;

public class ProductManagement {
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Product> orderList = FXCollections.observableArrayList();


    private ProductTableLoad productTableLoad = new ProductTableLoad();
    private Alert alert;

    public ObservableList load() throws SQLException {
        productList.removeAll(productList);
        productTableLoad.loadDatabase(productList);
        return productList;
    }
    public void addProduct(Product product) throws SQLException {
        productTableLoad.ControlStocksForAdd(product);
        load();
    }
    public void deleteProduct(String barcodeNumber) throws SQLException {
        if(productList.size() == 0){
            showAlert(Alert.AlertType.ERROR,"Listede ürün bulunmamaktadır.");
            return;
        }

        productTableLoad.deleteProduct(barcodeNumber);
        showAlert(Alert.AlertType.INFORMATION,"Ürün silme işlemi başarılı");
        load();

    }
    public void changeProductFeature(int id,String name,String barcodeNumber,int quantity,double price,double takingPrice,int stock) throws SQLException {
        Product product = new Product(id,name,barcodeNumber,quantity,price,takingPrice,stock);
        productTableLoad.changeProductFeature(product);
        load();
    }
    public ObservableList loadOrder(String barcode) throws SQLException {
        productTableLoad.loadOrderDatabase(orderList,barcode);
        return orderList;
    }
    public ObservableList decreaseOrder(String barcode) throws SQLException {
        productTableLoad.decreaseOrder(orderList,barcode);
        return orderList;
    }
    public ObservableList takeOutProduct(String barcode){
        productTableLoad.takeOutProduct(orderList,barcode);
        return orderList;
    }
    public double sum(){
        double sum = productTableLoad.getSumField();
        return sum;
    }
    public void RemainingStock() throws SQLException {
        productTableLoad.calculateRemainingStock(orderList);

    }

    public ObservableList<Product> getProductList() {
        return productList;
    }

    public void showAlert(Alert.AlertType alertType, String content){
         alert = new Alert(alertType);
        alert.setContentText(content);
        alert.show();
    }

    public Alert getAlert() {
        return alert;
    }


    public ObservableList<Product> loadSalesInformation() throws SQLException {
        return productTableLoad.LoadOrderScreenFromDatabase();
    }

    public ObservableList loadDetails(int orderId) throws SQLException {
      return productTableLoad.loadDetailsFromDatabase(orderId);
    }
    public ObservableList LoadDetails() throws SQLException {

        return productTableLoad.getOrderInformation();
    }

    public String LoadTotalSales() throws SQLException {
        return productTableLoad.LoadSumOfProductFromDatabase();
    }

    public void resetSalesList() throws SQLException {
        productTableLoad.resetSalesListFor();
    }

    public double loadAllStockPrices(String option) throws SQLException {
        return productTableLoad.loadAllPrice(option);
    }

    public void loadDailySaleInformation() throws SQLException {
        productTableLoad.LoadDailySaleInfo();
    }

    public ObservableList<Product> dmLoadInformation() throws SQLException {
        return productTableLoad.dmLoadInfo();
    }
}
