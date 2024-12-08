package foly.anhld.duan1.Modol;

import java.util.List;

public class Order {
    private String orderId;      // Mã đơn hàng (Firestore auto-generated)
    private String userId;       // Mã người dùng
    private List<CartItem> cartItems; // Danh sách sản phẩm trong đơn hàng
    private String orderDate;    // Ngày đặt hàng
    private String status;       // Trạng thái đơn hàng (Pending, Completed)
    private double totalPrice;   // Tổng tiền đơn hàng
    public Order(){
    }
    public Order(String orderId, String userId, List<CartItem> cartItems, String orderDate, String status, double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.cartItems = cartItems;
        this.orderDate = orderDate;
        this.status = "Pending";
        this.totalPrice = totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
