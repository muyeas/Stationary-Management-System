package cilek.model;

public class Product {
    private int no;
    private String name;
    private String barcodeNumber;
    private int quantity;
    private double price;
    private int amount;
    private double takingPrice;
    private int stock;
    private String TimeofOrder;
    private String totalPrice;


    public Product(int no, String name, String barcodeNumber, int quantity, double price,int amount) {
        this.no = no;
        this.name = name;
        this.quantity = quantity;
        this.barcodeNumber = barcodeNumber;
        this.price = price;
        this.amount = amount;
    }
    public Product(int no, String name, String barcodeNumber, int quantity, double price,double takingPrice,int stock) {
        this.no = no;
        this.name = name;
        this.quantity = quantity;
        this.barcodeNumber = barcodeNumber;
        this.price = price;
        this.takingPrice = takingPrice;
        this.stock = stock;
    }
    public Product(int no, String TimeofOrder,String totalPrice){
        this.no = no;
        this.TimeofOrder = TimeofOrder;
        this.totalPrice = totalPrice;
    }
    public Product(String name,double price,int amount,String totalPrice){
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }

    public Product(String date, String sale) {
        this.TimeofOrder = date;
        this.totalPrice = sale;
    }

    @Override
    public String toString() {
        return "---|Ürün ismi: " + name + "|       |Ürün Fiyatı: " + price + "|       |Ürün Adeti: " + amount + "|       |Toplam Ürün Tutarı: " + totalPrice +"|" ;
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTakingPrice() {
        return takingPrice;
    }


    public int getStock() {
        return stock;
    }

    public String getTimeofOrder() {
        return TimeofOrder;
    }

    public String getTotalPrice() {
        return totalPrice;
    }


}
