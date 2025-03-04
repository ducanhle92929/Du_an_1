package foly.anhld.duan1;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import foly.anhld.duan1.Modol.Order;
import foly.anhld.duan1.adater.OrderAdapter;
import foly.anhld.duan1.adater.OrderAdapterAdmin;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapterAdmin orderAdapter;
    private List<Order> orderList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.rcvorderadmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo Firestore
        firestore = FirebaseFirestore.getInstance();

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapterAdmin(orderList, this);  // Dùng constructor có context nếu cần
        recyclerView.setAdapter(orderAdapter);

        // Load các đơn hàng từ Firestore
        loadOrders();
    }

    private void loadOrders() {
        // Lấy tất cả đơn hàng từ Firestore
        firestore.collection("orders")  // Lấy collection orders từ Firestore
                .get()  // Thực hiện truy vấn để lấy tất cả đơn hàng
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();  // Lấy kết quả từ truy vấn
                        if (querySnapshot != null) {
                            // Chuyển dữ liệu từ querySnapshot thành danh sách đơn hàng
                            orderList = new ArrayList<>(querySnapshot.toObjects(Order.class));
                            if (orderList.isEmpty()) {
                                Toast.makeText(NotificationActivity.this, "Không có đơn hàng nào.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Cập nhật dữ liệu mới cho adapter
                                orderAdapter.updateOrders(orderList);
                            }
                        }
                    } else {
                        // Thông báo lỗi nếu không thể tải dữ liệu
                        Toast.makeText(NotificationActivity.this, "Không thể tải dữ liệu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
