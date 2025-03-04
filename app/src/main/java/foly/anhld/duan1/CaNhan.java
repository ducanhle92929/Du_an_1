package foly.anhld.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CaNhan extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ca_nhan);
         LinearLayout txtIn = findViewById(R.id.txtLogin);
         Button btnLogout = findViewById(R.id.btnLogout);

        txtIn.setOnClickListener(v -> {
            startActivity(new Intent(  CaNhan.this, Login.class));
        });
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(  CaNhan.this, Login.class));
        });

        menuBottom();
    }
    private void menuBottom(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                // Chuyển đến Activity Home
                startActivity(new Intent(  CaNhan.this,  Home.class));
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                // Chuyển đến Activity Dashboard
                startActivity(new Intent(CaNhan.this, CartActivity.class));
                return true;
            }
//            else if (itemId == R.id.navigation_notificatio) {
//                // Chuyển đến Activity Notification
//                startActivity(new Intent(Home.this,  OrderActivity.class));
//                return true;
//            }
            else if (itemId == R.id.navigation_notificatio) {
                // Giả sử userId đã có sẵn ở Home (bạn cần lấy userId từ FirebaseAuth)
//                String userId = "userId"; // Thay bằng cách lấy userId thực tế
//                Intent intent = new Intent(Home.this, OrderActivity.class);
//                intent.putExtra("userId", userId);
//                startActivity(intent);
                Intent intent = new Intent(CaNhan.this, OrderActivity.class);
                intent.putExtra("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
                return true;
            }

            else if (itemId == R.id.dashboard) {
                // Chuyển đến Activity Login
                startActivity(new Intent(CaNhan.this,  CaNhan.class));
                return true;
            } else {
                return false;
            }
        });
    }

}