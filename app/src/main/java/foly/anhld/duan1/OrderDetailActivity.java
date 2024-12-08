package foly.anhld.duan1;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView orderStatus;
    private TextView orderTotal;
    private FirebaseFirestore db;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Initialize Firestore and UI components
        db = FirebaseFirestore.getInstance();
        orderStatus = findViewById(R.id.orderStatus);
        orderTotal = findViewById(R.id.orderTotal);
        ivBack = findViewById(R.id.ivBack);

        // Load order details
        String orderId = getIntent().getStringExtra("ORDER_ID"); // Get the order ID from the intent
        if (orderId != null) {
            loadOrderDetails(orderId);
        } else {
            Log.e("OrderDetailActivity", "Order ID is null.");
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrderDetailActivity.this, OrderActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void loadOrderDetails(String orderId) {
        db.collection("orders")
                .document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Safely retrieve values
                        String status = documentSnapshot.getString("status");
                        Double total = documentSnapshot.getDouble("total");

                        // Set status and total values safely
                        if (status != null) {
                            orderStatus.setText(status);
                        } else {
                            orderStatus.setText("Status unavailable");
                        }

                        if (total != null) {
                            orderTotal.setText(String.format("Total: %.2f", total));
                        } else {
                            orderTotal.setText("Total unavailable");
                        }
                    } else {
                        Log.e("OrderDetailActivity", "Order document not found.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("OrderDetailActivity", "Error fetching order details.", e);
                });
    }
}
