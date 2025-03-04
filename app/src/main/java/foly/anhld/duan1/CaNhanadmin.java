package foly.anhld.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CaNhanadmin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_can_nhan1);
        LinearLayout txtIn = findViewById(R.id.txtLogin);
        Button btnLogout = findViewById(R.id.btnLogout);

        txtIn.setOnClickListener(v -> {
            startActivity(new Intent(  CaNhanadmin.this, Login.class));
        });
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(  CaNhanadmin.this, Login.class));
        });

        menuBottom();
    }
    private void menuBottom(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                // Chuyển đến Activity Home
                startActivity(new Intent(  CaNhanadmin.this,  QuanLySanPhamAdmin.class));
                return true;
            } else if (itemId == R.id.navigation_thongKe) {
                // Chuyển đến Activity Dashboard
                startActivity(new Intent(CaNhanadmin.this,  ThongKe_Activyti.class));
                return true;
            }
//            else if (itemId == R.id.navigation_notificatio) {
//                // Chuyển đến Activity Notification
//                startActivity(new Intent(Home.this,  OrderActivity.class));
//                return true;
//            }
            else if (itemId == R.id.navigation_notification) {
                // Giả sử userId đã có sẵn ở Home (bạn cần lấy userId từ FirebaseAuth)
//                String userId = "userId"; // Thay bằng cách lấy userId thực tế
//                Intent intent = new Intent(Home.this, OrderActivity.class);
//                intent.putExtra("userId", userId);
//                startActivity(intent);
                Intent intent = new Intent(CaNhanadmin.this, NotificationActivity.class);
                intent.putExtra("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
                return true;
            }

            else if (itemId == R.id.navigation_notification1) {
                // Chuyển đến Activity Login
                startActivity(new Intent(CaNhanadmin.this,  CaNhanadmin.class));
                return true;
            } else {
                return false;
            }
        });
    }

}