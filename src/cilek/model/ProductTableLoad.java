package cilek.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;


public class ProductTableLoad {
    private DatabaseConnection connection = new DatabaseConnection();
    private String query;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private final List<String> addedBarcodNumber = new LinkedList<>();
    private final ObservableList<Product> salesList = FXCollections.observableArrayList();
    private final ObservableList<Product> orderInformation = FXCollections.observableArrayList();
    private double sumField = 0.0;
    private Alert alert;
    private boolean OrderNameCheck=false;
    private double sumOfProduct;
    private ObservableList<Product> monthBalanceList = FXCollections.observableArrayList();


    public ObservableList loadDatabase(ObservableList<Product> List) throws SQLException {
        double allTakingPrice = 0.0;
        double allPrice = 0.0;
        query = "SELECT * FROM xproducts";
        resultSet = connection.getConnection().createStatement().executeQuery(query);
        while (resultSet.next()) {
            List.add(new Product(resultSet.getInt("no"), resultSet.getString("name"),
                    resultSet.getString("barcode_number"), resultSet.getInt("quantity"), resultSet.getDouble("price"),
                    resultSet.getDouble("takingprice"), resultSet.getInt("stock")));
            allTakingPrice += resultSet.getInt("stock") * resultSet.getInt("takingprice");
            allPrice += resultSet.getInt("stock") * resultSet.getInt("price");
        }
        resultSet.close();
        connection.getConnection().close();
        return List;

    }
    public void ControlStocksForAdd(Product product) throws SQLException {
        if(checkDatabaseForExit(product)){
            AddStock(product);
        }
        else{
            addProduct(product);
        }
    }

    private boolean checkDatabaseForExit(Product product) throws SQLException {
        query = "SELECT * FROM xproducts WHERE name =? AND barcode_number =? ";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, product.getName().toUpperCase());
        preparedStatement.setString(2,product.getBarcodeNumber());
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            connection.getConnection().close();
            preparedStatement.close();
            return true;

        }
        preparedStatement.close();
        connection.getConnection().close();
        return false;
    }

    private void AddStock(Product product) throws SQLException {
        query = "UPDATE xproducts SET stock = stock + ? , quantity = quantity + ? WHERE name =? AND barcode_number =? ";
        try{

            preparedStatement = connection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,product.getStock());
            preparedStatement.setInt(2,product.getStock());
            preparedStatement.setString(3,product.getName().toUpperCase());
            preparedStatement.setString(4, product.getBarcodeNumber());
            preparedStatement.executeUpdate();
            connection.getConnection().close();
            preparedStatement.close();
            alert = new Alert(Alert.AlertType.INFORMATION,"ÜRÜN STOĞU BAŞARIYLA ARTIRILDI");
            alert.show();
        }

        catch(Exception e){
            alert = new Alert(Alert.AlertType.ERROR, "HATA");
            alert.show();
            System.out.println(e);
        }
    }

    public void addProduct(Product product) throws SQLException {
        query = "INSERT INTO xproducts(name,barcode_number,quantity,price,takingprice,stock) VALUES(?,?,?,?,?,?)";
        preparedStatement = connection.getConnection().prepareStatement(query);
        if (product.getName().equals("") || product.getBarcodeNumber().equals("")) {
            alert = new Alert(Alert.AlertType.ERROR, "EKSİK BİLGİ.ÜRÜN EKLENEMEDİ");
            alert.show();

        } else {
            preparedStatement.setString(1, product.getName().toUpperCase());
            preparedStatement.setString(2, product.getBarcodeNumber());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setDouble(5, product.getTakingPrice());
            preparedStatement.setInt(6, product.getStock());
            preparedStatement.executeUpdate();
            alert = new Alert(Alert.AlertType.INFORMATION, "ÜRÜN BAŞARIYLA EKLENDİ");
            alert.show();
        }
        preparedStatement.close();
        connection.getConnection().close();

    }


    public void deleteProduct(String barcodeNumber) throws SQLException {
        query = "DELETE FROM xproducts WHERE barcode_number = ?";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, barcodeNumber);
        preparedStatement.executeUpdate();
        System.out.println("Succesfull");
        preparedStatement.close();
        connection.getConnection().close();

    }


    public void changeProductFeature(Product product) throws SQLException {
       try{query = "UPDATE xproducts SET name =? , barcode_number =? , quantity =? , price =? ,takingprice =? , stock =? WHERE no =? ";
           preparedStatement = connection.getConnection().prepareStatement(query);
           preparedStatement.setString(1, product.getName().toUpperCase());
           preparedStatement.setString(2, product.getBarcodeNumber());
           preparedStatement.setInt(3,product.getQuantity());
           preparedStatement.setDouble(4, product.getPrice());
           preparedStatement.setDouble(5, product.getTakingPrice());
           preparedStatement.setInt(6, product.getStock());
           preparedStatement.setInt(7, product.getNo());
           preparedStatement.executeUpdate();
           alert = new Alert(Alert.AlertType.INFORMATION,"ÜRÜN BİLGİLERİ DEĞİŞTİRİLDİ");
           alert.show();
           preparedStatement.close();
           connection.getConnection().close();}
        catch(Exception e){
            alert = new Alert(Alert.AlertType.ERROR,"ÜRÜN BİLGİLERİNE DİKKAT EDİNİZ.(BARKOD NUMARASI VS...");
            alert.show();

        }

    }

    public ObservableList loadOrderDatabase(ObservableList<Product> orderList, String barcode) throws SQLException {
        query = "SELECT * FROM xproducts WHERE barcode_number =?";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, barcode);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            if (addedBarcodNumber.contains(barcode)) {
                for (int i = 0; i < orderList.size(); i++) {
                    if (orderList.get(i).getBarcodeNumber().equals(barcode)) {
                        if ((orderList.get(i).getQuantity() == orderList.get(i).getAmount())) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Bu üründen daha fazla stokta bulunmamaktadır...");
                            alert.show();
                        } else {
                            orderList.set(i, orderList.get(i)).setAmount(orderList.get(i).getAmount() + 1);
                            sumField += orderList.get(i).getPrice();
                        }

                    }
                }
            } else {

                orderList.add(new Product(resultSet.getInt("no"), resultSet.getString("name"),
                        resultSet.getString("barcode_number"), resultSet.getInt("quantity")
                        , resultSet.getDouble("price"), 1));
                addedBarcodNumber.add(barcode);
                String deleteBarcode = null;
                for (int i = 0; i < orderList.size(); i++) {
                    if (orderList.get(i).getQuantity() <= 0) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Bu üründen bulunmamaktadır");
                        alert.show();
                        deleteBarcode = orderList.get(i).getBarcodeNumber();
                        orderList.remove(orderList.get(i));

                    }
                }
                if (resultSet.getString("barcode_number").equals(deleteBarcode)) {
                    continue;
                } else {
                    sumField += resultSet.getDouble("price");
                }


            }
        }
        preparedStatement.close();
        connection.getConnection().close();
        resultSet.close();
        return orderList;
    }

    public void decreaseOrder(ObservableList<Product> orderList, String barcode) throws SQLException {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getBarcodeNumber().equals(barcode)) {
                if (orderList.get(i).getAmount() == 1) {
                    sumField -= orderList.get(i).getPrice();
                    addedBarcodNumber.remove(orderList.get(i).getBarcodeNumber());
                    orderList.remove(orderList.get(i));

                } else {
                    sumField -= orderList.get(i).getPrice();
                    orderList.set(i, orderList.get(i)).setAmount(orderList.get(i).getAmount() - 1);

                }
            }

        }
    }

    public void takeOutProduct(ObservableList<Product> orderList, String barcode) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getBarcodeNumber().equals(barcode)) {
                int number = orderList.get(i).getAmount();
                sumField -= (orderList.get(i).getPrice()) * number;
                addedBarcodNumber.remove(orderList.get(i).getBarcodeNumber());
                orderList.remove(orderList.get(i));

            }
        }
    }

    public double getSumField() {
        return sumField;
    }

    public void calculateRemainingStock(ObservableList<Product> orderList) throws SQLException {

        sumOfProduct = 0.0;
        String salesTime = null;

        for (int i = 0; i < orderList.size(); i++) {
            sumOfProduct += orderList.get(i).getAmount() * orderList.get(i).getPrice() ;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            if(OrderNameCheck == false){
                salesTime = dtf.format(now);
                loadOrderScreen(salesTime);
                OrderNameCheck = true;
            }

            LoadOrderInformation(findIdFromTime(salesTime),orderList.get(i).getName(),String.valueOf(orderList.get(i).getPrice()),orderList.get(i).getAmount(),String.valueOf(orderList.get(i).getPrice()*orderList.get(i).getAmount()));
            orderList.get(i).setQuantity(orderList.get(i).getQuantity() - orderList.get(i).getAmount());
            query = "UPDATE xproducts SET quantity =? WHERE barcode_number =?";
            preparedStatement = connection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, orderList.get(i).getQuantity());
            preparedStatement.setString(2, orderList.get(i).getBarcodeNumber());
            preparedStatement.executeUpdate();
        }
        LoadSumInformation(String.valueOf(sumOfProduct),findIdFromTime(salesTime));
        OrderNameCheck = false;
        System.out.println("Succesfulll");
        orderList.removeAll(orderList);
        salesTime = null;
        addedBarcodNumber.clear();
        sumOfProduct = 0.0;
        sumField = 0.0;
        connection.getConnection().close();
        preparedStatement.close();

    }

    private int findIdFromTime(String time) throws SQLException {
        int id = 0;
        query = "SELECT * FROM orderScreen WHERE Date =?";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, time);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
                id = resultSet.getInt("OrderId");
        }
        connection.getConnection().close();
        preparedStatement.close();
        resultSet.close();
        return id;
    }

    public void loadOrderScreen(String time) throws SQLException {
        query= "INSERT INTO orderScreen (Date) VALUES(?)";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setString(1,time);
        preparedStatement.executeUpdate();
        connection.getConnection().close();
        preparedStatement.close();
    }

    public void LoadOrderInformation(int id,String productName,String price,int amount,String SumOfProduct) throws SQLException {
        query = "INSERT INTO OrderInformation (id,ProductName,Price,Amount,SumOfProduct) VALUES(?,?,?,?,?)";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,productName.toUpperCase());
        preparedStatement.setString(3,price);
        preparedStatement.setInt(4,amount);
        preparedStatement.setString(5,SumOfProduct);
        preparedStatement.executeUpdate();
        connection.getConnection().close();
        preparedStatement.close();
    }
    public void LoadSumInformation(String sumOfProduct,int orderId) throws SQLException {
        query = "UPDATE orderScreen SET SumOfProduct =? WHERE OrderId =? ";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setString(1,sumOfProduct);
        preparedStatement.setInt(2,orderId);
        preparedStatement.executeUpdate();
        connection.getConnection().close();
        preparedStatement.close();
    }

    public ObservableList<Product>  LoadOrderScreenFromDatabase() throws SQLException {
        salesList.removeAll(salesList);
        query = "SELECT * FROM orderScreen";
        resultSet = connection.getConnection().createStatement().executeQuery(query);
        while (resultSet.next()){
            salesList.add(new Product(resultSet.getInt("OrderId"),resultSet.getString("Date"),resultSet.getString("SumOfProduct")));
        }
        connection.getConnection().close();
        resultSet.close();
        return salesList;

    }

    public ObservableList<Product> loadDetailsFromDatabase(int orderId) throws SQLException {
        orderInformation.removeAll(orderInformation);
        query = "SELECT * FROM OrderInformation WHERE id =?";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, orderId);
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            orderInformation.add(new Product(resultSet.getString("ProductName"),Double.parseDouble(resultSet.getString("Price")),
                    resultSet.getInt("Amount"),resultSet.getString("SumOfProduct")));

        }
        connection.getConnection().close();
        preparedStatement.close();
        resultSet.close();
          return  orderInformation;
    }

    public ObservableList<Product> getOrderInformation() {

        return orderInformation;
    }
    public String LoadSumOfProductFromDatabase() throws SQLException {
        query = "SELECT * FROM orderScreen";
        resultSet = connection.getConnection().createStatement().executeQuery(query);
        double result = 0.0;
        while(resultSet.next()){
            result += Double.parseDouble(resultSet.getString("SumOfProduct"));
        }
        connection.getConnection().close();
        resultSet.close();
        return String.valueOf(result);
    }

    public void resetSalesListFor() throws SQLException {
        salesList.removeAll(salesList);
        deleteOrderInformation();
        deleteOrderScreen();

    }
    public void deleteOrderScreen() throws SQLException {
        query = "DELETE FROM orderScreen";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.executeUpdate();
        connection.getConnection().close();
        preparedStatement.close();
    }
    public void deleteOrderInformation() throws SQLException {
        query = "DELETE FROM OrderInformation";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.executeUpdate();
        connection.getConnection().close();
        preparedStatement.close();
    }


    public double loadAllPrice(String option) throws SQLException {
            double price = 0.0;
            query = "SELECT * FROM xproducts";
            resultSet = connection.getConnection().createStatement().executeQuery(query);
            while (resultSet.next()){
                price += resultSet.getInt(option) * resultSet.getInt("stock");
            }
            connection.getConnection().close();
            resultSet.close();
            return price;



    }

    public void LoadDailySaleInfo() throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        query = "INSERT INTO dailySales (Date , Sale) VALUES(?,?)";
        preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setString(1,dtf.format(now));
        preparedStatement.setString(2,LoadSumOfProductFromDatabase());
        preparedStatement.executeUpdate();
        connection.getConnection().close();
        preparedStatement.close();
    }


    public ObservableList<Product> dmLoadInfo() throws SQLException {
        monthBalanceList.removeAll(monthBalanceList);
        query = "SELECT * FROM dailySales";
        resultSet = connection.getConnection().createStatement().executeQuery(query);
        while (resultSet.next()){
            monthBalanceList.add(new Product(resultSet.getString("Date"),resultSet.getString("Sale")));
        }
        connection.getConnection().close();
        resultSet.close();
        return monthBalanceList;
    }
}
