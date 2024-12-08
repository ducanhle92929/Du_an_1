package foly.anhld.duan1.Modol;

public class CartItem {
    private String productId;  // Mã sản phẩm
    private String productName;  // Tên sản phẩm
    private double price;  // Giá sản phẩm
    private String size;  // Kích thước sản phẩm
    private int quantity;  // Số lượng trong giỏ hàng

    // Constructor mặc định
    public CartItem() {
    }

    // Constructor với đầy đủ các thông tin
    public CartItem(String productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
    }

    // Getter và Setter cho mã sản phẩm
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    // Getter và Setter cho tên sản phẩm
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getter và Setter cho giá sản phẩm
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter và Setter cho kích thước
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    // Getter và Setter cho số lượng
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
