package foly.anhld.duan1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import foly.anhld.duan1.Modol.Order;

public class ThongKe_Activyti extends AppCompatActivity {
    private FirebaseFirestore db;
    private BarChart  barChart;
    private Toolbar toolbar;
    private ImageView ivBack;

//    private BarChart barChart;
//    private List<String> xValue = Arrays.asList("nike","adidas","puma");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_ke_activyti);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        BarChart barChart = findViewById(R.id.chart);
//        barChart.getAxisRight().setDrawGridLines(false);
//
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(0,45f));
//        entries.add(new BarEntry(1,80f));
//        entries.add(new BarEntry(2,65f));
//        entries.add(new BarEntry(3,38f));
//
//        YAxis yAxis = barChart.getAxisLeft();
//        yAxis.setAxisMaximum(0f);
//        yAxis.setAxisMaximum(100f);
//        yAxis.setAxisLineWidth(2f);
//        yAxis.setAxisLineColor(Color.BLACK);
//        yAxis.setLabelCount(10);
//
//        BarDataSet DataSet = new BarDataSet(entries,"Subjects");
//        DataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//
//        BarData barData = new BarData(DataSet);
//        barChart.setData(barData);
//
//        barChart.getDescription().setEnabled(false);
//        barChart.invalidate();
//
//        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValue));
//        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        barChart.getXAxis().setGranularity(1f);
//        barChart.getXAxis().setGranularityEnabled(true);
        db = FirebaseFirestore.getInstance();
        barChart  = findViewById(R.id.barChart);
        ivBack = findViewById(R.id.ivBack);
         toolbar = findViewById(R.id.tooBar);
        setSupportActionBar(toolbar);


        getOrdersByDate(); // gọi hàm getOrdersByDate của tk ngay

        // nút chuyển
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin màn hình trước từ Intent
                Intent intent = getIntent();
                String source = intent.getStringExtra("source");
                if ("QuanLySanPham".equals(source)) {
                    startActivity(new Intent(ThongKe_Activyti.this, QuanLySanPhamAdmin.class));
                } else if ("".equals(source)) {
//                    startActivity(new Intent(ThongKe_Activyti.this, ));
                }

                finish(); // Kết thúc Activity hiện tại
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Lấy inflater của menu
        MenuInflater inflater = getMenuInflater();
        // Inflate menu từ tệp menu_thongke.xml vào menu
        inflater.inflate(R.menu.menu_thongke, menu);
        return true; // Trả về true để menu được hiển thị
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tkthang) { // Kiểm tra id có khớp với R.id.tkthang hay không
            getTkThang();
            return true;
        }
        if (id == R.id.tkSpBanChay){
            gettkSpBanChay();
            return true ;
        }
        if (id == R.id.tkTheoNgay){
            getOrdersByDate();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gettkSpBanChay() {
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documentSnapshots = task.getResult();
                        Map<String, Integer> productSales = new HashMap<>();
                        List<String> productNames = new ArrayList<>(); // Danh sách các sản phẩm

                        // Duyệt qua tất cả đơn hàng
                        for (QueryDocumentSnapshot document : documentSnapshots) {
                            List<Map<String, Object>> products = (List<Map<String, Object>>) document.get("cartItems"); // Assuming 'cartItems' is the field
                            if (products != null) {
                                for (Map<String, Object> product : products) {
                                    String productName = (String) product.get("productName");
                                    Long quantity = (Long) product.get("quantity");

                                    if (productSales.containsKey(productName)) {
                                        productSales.put(productName, productSales.get(productName) + quantity.intValue());
                                    } else {
                                        productSales.put(productName, quantity.intValue());
                                        productNames.add(productName); // Thêm sản phẩm vào danh sách
                                    }
                                }
                            }
                        }

                        // Tạo dữ liệu cho biểu đồ
                        List<BarEntry> entries = new ArrayList<>();
                        int index = 0;
                        for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
                            entries.add(new BarEntry(index++, entry.getValue()));
                        }

                        // Tạo BarDataSet từ các dữ liệu đã thu thập
                        BarDataSet dataSet = new BarDataSet(entries, "Sản phẩm bán chạy");
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                        // Tạo BarData từ BarDataSet
                        BarData data = new BarData(dataSet);

                        // Cập nhật nhãn trục X với tên sản phẩm
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(productNames)); // Cập nhật trục X
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        xAxis.setGranularityEnabled(true);

                        // Cập nhật dữ liệu vào biểu đồ
                        barChart.setData(data);
                        barChart.invalidate();  // Refresh biểu đồ
                    } else {
                        Toast.makeText(ThongKe_Activyti.this, "Lỗi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Thống kê doanh thu theo ngày
    private void getOrdersByDate() {
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documentSnapshots = task.getResult();
                        Map<String, Double> dailySales = new HashMap<>();
                        List<String> dates = new ArrayList<>(); // Danh sách các ngày

                        for (QueryDocumentSnapshot document : documentSnapshots) {
                            String orderDate = document.getString("orderDate");
                            Double totalPrice = document.getDouble("totalPrice");

                            if (dailySales.containsKey(orderDate)) {
                                dailySales.put(orderDate, dailySales.get(orderDate) + totalPrice);
                            } else {
                                dailySales.put(orderDate, totalPrice);
                                dates.add(orderDate);  // Thêm ngày vào danh sách
                            }
                        }

                        // Tạo dữ liệu cho biểu đồ
                        List<BarEntry> entries = new ArrayList<>();
                        int index = 0;
                        for (Map.Entry<String, Double> entry : dailySales.entrySet()) {
                            entries.add(new BarEntry(index++, entry.getValue().floatValue()));
                        }

                        // Tạo BarDataSet từ dữ liệu thu thập
                        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo ngày");
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                        // Tạo BarData từ BarDataSet
                        BarData data = new BarData(dataSet);

                        // Cập nhật nhãn trục X với ngày thực tế
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates)); // Cập nhật trục X
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        xAxis.setGranularityEnabled(true);

                        // Cập nhật dữ liệu vào biểu đồ
                        barChart.setData(data);
                        barChart.invalidate();  // Refresh biểu đồ
                    } else {
                        Toast.makeText(ThongKe_Activyti.this, "Lỗi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Thống kê doanh thu theo tháng
    private void getTkThang() {
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documentSnapshots = task.getResult();
                        Map<String, Double> monthlySales = new HashMap<>();
                        List<String> months = new ArrayList<>();

                        // Duyệt qua tất cả đơn hàng
                        for (QueryDocumentSnapshot document : documentSnapshots) {
                            String orderDate = document.getString("orderDate");
                            Double totalPrice = document.getDouble("totalPrice");

                            String month = getMonthFromDate(orderDate);  // Lấy tháng từ ngày

                            // Cộng tổng doanh thu theo tháng
                            if (monthlySales.containsKey(month)) {
                                monthlySales.put(month, monthlySales.get(month) + totalPrice);
                            } else {
                                monthlySales.put(month, totalPrice);
                                months.add(month);  // Thêm tháng vào danh sách
                            }
                        }

                        // Tạo dữ liệu cho biểu đồ
                        List<BarEntry> entries = new ArrayList<>();
                        for (int i = 1; i <= 12; i++) {
                            String month = String.format("%02d", i);
                            if (monthlySales.containsKey(month)) {
                                entries.add(new BarEntry(i - 1, monthlySales.get(month).floatValue()));
                            } else {
                                entries.add(new BarEntry(i - 1, 0f)); // Nếu không có doanh thu cho tháng này
                            }
                        }

                        // Tạo BarDataSet từ dữ liệu
                        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo tháng");
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                        // Tạo BarData từ BarDataSet
                        BarData data = new BarData(dataSet);

                        // Cập nhật nhãn trục X với tên các tháng
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonths())); // Cập nhật trục X với các tháng
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        xAxis.setGranularityEnabled(true);

                        // Cập nhật dữ liệu vào biểu đồ
                        barChart.setData(data);
                        barChart.invalidate();  // Refresh biểu đồ
                    } else {
                        Toast.makeText(ThongKe_Activyti.this, "Lỗi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Phương thức giúp lấy tháng từ ngày
    private String getMonthFromDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            return String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        } catch (ParseException e) {
            e.printStackTrace();
            return "00"; // Trường hợp lỗi
        }
    }

    // Phương thức trả về danh sách các tháng
    private List<String> getMonths() {
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(String.format("%02d", i));
        }
        return months;
    }

}