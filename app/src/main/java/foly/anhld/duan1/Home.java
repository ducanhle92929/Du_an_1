package foly.anhld.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import foly.anhld.duan1.Modol.SanPham;
import foly.anhld.duan1.adater.HomeAdapter;
import foly.anhld.duan1.adater.ImageSliderAdapter;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<SanPham> productList;
    private FirebaseFirestore db;
    private ViewPager2 imageSlider;
    private TabLayout tabLayout;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Sử dụng GridLayoutManager để hiển thị dạng lưới
        int numberOfColumns = 2; // Số cột trong lưới
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        // Khởi tạo danh sách sản phẩm và adapter
        productList = new ArrayList<>();
        adapter = new HomeAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        // Kết nối Firestore
        db = FirebaseFirestore.getInstance();

        // Lấy thông tin sản phẩm theo ID từ Firebase
        fetchAllProductsAndUpdateUI();
        slideImg();
        menuBottom();
    }
    private void menuBottom(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                // Chuyển đến Activity Home
                startActivity(new Intent(Home.this,  Home.class));
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                // Chuyển đến Activity Dashboard
                startActivity(new Intent(Home.this, CartActivity.class));
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
                Intent intent = new Intent(Home.this, OrderActivity.class);
                intent.putExtra("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
                return true;
            }

            else if (itemId == R.id.dashboard) {
                // Chuyển đến Activity Login
                startActivity(new Intent(Home.this,  CaNhan.class));
                return true;
            } else {
                return false;
            }
        });
    }
    private void slideImg(){
        imageSlider = findViewById(R.id.imageSlider);
        tabLayout = findViewById(R.id.sliderIndicator);

        // Danh sách ảnh
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.banner_giay3); // Thay bằng tên ảnh thực tế
        images.add(R.drawable.bnanner_giay2); // Thay bằng ảnh khác nếu cần
        images.add(R.drawable.banner_giay3); // Thay bằng ảnh khác nếu cần
        images.add(R.drawable.banner_giay3); // Thay bằng ảnh khác nếu cần

        // Gán Adapter
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, images);
        imageSlider.setAdapter(adapter);

        // Thêm chỉ báo (indicator)
        new TabLayoutMediator(tabLayout, imageSlider, (tab, position) -> {
            // Không cần làm gì
        }).attach();

        // Tự động chuyển ảnh
        autoSlideImages();
    }
    private void autoSlideImages() {
        sliderHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = imageSlider.getCurrentItem();
                int totalItems = imageSlider.getAdapter().getItemCount();
                imageSlider.setCurrentItem((currentItem + 1) % totalItems, true); // Chuyển ảnh tiếp theo
                sliderHandler.postDelayed(this, 2500); // Lặp lại sau giây
            }
        }, 2500);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacksAndMessages(null); // Ngừng Handler khi Activity bị phá hủy
    }

    // Lấy tất cả sản phẩm theo ID và cập nhật RecyclerView
    private void fetchAllProductsAndUpdateUI() {
        db.collection("sanpham")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear(); // Xóa danh sách cũ

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        SanPham sanPham = document.toObject(SanPham.class); // Chuyển đổi dữ liệu
                        if (sanPham != null) {
                            sanPham.setMaSanPham(document.getId()); // Lấy ID của sản phẩm
                            productList.add(sanPham); // Thêm sản phẩm vào danh sách
                        }
                    }

                    // Cập nhật dữ liệu lên RecyclerView
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching products", e));
    }
}
