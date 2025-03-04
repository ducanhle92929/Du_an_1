package foly.anhld.duan1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.firestore.FirebaseFirestore;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import foly.anhld.duan1.adater.ProductAdapter;

public class OrderDetailActivityUser extends AppCompatActivity {
    Button btnCancelOrder;
    private TextView orderStatus;
    private TextView orderTotal;
    private FirebaseFirestore db;
    private ImageView ivBack;
    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang_user);

        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        db = FirebaseFirestore.getInstance();
        orderStatus = findViewById(R.id.tvOrderStatususer);
        orderTotal = findViewById(R.id.tvFinalPrice);
        ivBack = findViewById(R.id.ivBack);
        rvProducts = findViewById(R.id.rvProducts);

        // Initialize RecyclerView with layout manager
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(new ArrayList<>());
        rvProducts.setAdapter(productAdapter);

        String orderId = getIntent().getStringExtra("ORDER_ID");
        if (orderId != null) {
            Log.d("OrderDetailActivity", "Received ORDER_ID: " + orderId);
            loadOrderDetails(orderId);
        } else {
            Log.e("OrderDetailActivity", "ORDER_ID is null.");
        }

        ivBack.setOnClickListener(view -> {
            Intent intent = new Intent(OrderDetailActivityUser.this, OrderActivity.class);
            startActivity(intent);
            finish();
        });

        btnCancelOrder.setOnClickListener(view -> {
            String currentStatus = orderStatus.getText().toString();
            if ("Pending".equalsIgnoreCase(currentStatus)) {
                showCancelOrderDialog(orderId);
            } else if ("Transit".equalsIgnoreCase(currentStatus)) {
                showCannotCancelDialog();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Không thể hủy đơn hàng")
                        .setMessage("Trạng thái đơn hàng không hợp lệ để hủy.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    private void loadOrderDetails(String orderId) {
        db.collection("orders")
                .document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String status = documentSnapshot.getString("status");
                        Double total = documentSnapshot.getDouble("totalPrice");
                        String orderDate = documentSnapshot.getString("orderDate");
                        String orderIdString = documentSnapshot.getString("orderId");
                        String recipientName = documentSnapshot.getString("recipientName");
                        String phoneNumber = documentSnapshot.getString("phoneNumber");
                        String address = documentSnapshot.getString("address");
                        List<Map<String, Object>> products = (List<Map<String, Object>>) documentSnapshot.get("cartItems");

                        orderStatus.setText(status != null ? status : "Status unavailable");
                        orderTotal.setText(total != null ? String.format("Total: %.2f", total) : "Total unavailable");

                        TextView orderIdTextView = findViewById(R.id.tvOrderIduser);
                        TextView recipientNameTextView = findViewById(R.id.tvReceiverName);
                        TextView phoneNumberTextView = findViewById(R.id.tvReceiverPhone);
                        TextView addressTextView = findViewById(R.id.tvReceiverAddress);
                        TextView orderDateTextView = findViewById(R.id.tvOrderDateuser);

                        orderIdTextView.setText(orderIdString != null ? "Order ID: " + orderIdString : "Order ID unavailable");
                        recipientNameTextView.setText(recipientName != null ? "Recipient: " + recipientName : "Recipient unavailable");
                        phoneNumberTextView.setText(phoneNumber != null ? "Phone: " + phoneNumber : "Phone unavailable");
                        addressTextView.setText(address != null ? "Address: " + address : "Address unavailable");
                        orderDateTextView.setText(orderDate != null ? "Order Date: " + orderDate : "Order Date unavailable");

                        if (products != null) {
                            productAdapter.updateProducts(products); // Update adapter with products data
                        } else {
                            Log.e("OrderDetailActivity", "No products found in this order.");
                        }

                        // Nếu trạng thái là "Delivered", ẩn nút hủy
                        if ("Delivered".equalsIgnoreCase(status)) {
                            btnCancelOrder.setVisibility(View.GONE); // Ẩn nút hủy
                        } else {
                            btnCancelOrder.setVisibility(View.VISIBLE); // Hiển thị nút hủy nếu trạng thái khác
                        }
                    } else {
                        Log.e("OrderDetailActivity", "Order document not found.");
                    }
                })
                .addOnFailureListener(e -> Log.e("OrderDetailActivity", "Error fetching order details.", e));
    }

    private void showCancelOrderDialog(String orderId) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy đơn hàng")
                .setMessage("Bạn có chắc chắn muốn xóa đơn hàng này?")
                .setPositiveButton("Có", (dialog, which) -> cancelOrder(orderId))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void cancelOrder(String orderId) {
        db.collection("orders")
                .document(orderId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("OrderDetailActivity", "Order successfully deleted.");

                    // Retrieve the userId here (assuming it's already available, e.g., from Firebase or shared preferences)
                    String userId;  // Replace this with actual userId value
                    userId = getIntent().getStringExtra("userId");
                    if (userId == null) {
                        Log.e("OrderActivity", "User ID is null");
                        Toast.makeText(this, "Không thể lấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
                        return; // Avoid further processing if userId is not available
                    }
                    // Create an Intent and pass the userId to the OrderActivity
                    Intent intent = new Intent(OrderDetailActivityUser.this, OrderActivity.class);
                    intent.putExtra("userId", userId);  // Pass the userId
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Log.e("OrderDetailActivity", "Error deleting order.", e));
    }


    private void showCannotCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Không thể hủy đơn hàng")
                .setMessage("Đơn hàng đang trong trạng thái Transit và không thể hủy.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
