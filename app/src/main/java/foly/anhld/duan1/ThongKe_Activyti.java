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
import java.util.TimeZone;

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
                .whereEqualTo("status", "Delivered") // Filter by "Delivered" status
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documentSnapshots = task.getResult();
                        Map<String, Integer> productSales = new HashMap<>();
                        List<String> productNames = new ArrayList<>();

                        for (QueryDocumentSnapshot document : documentSnapshots) {
                            List<Map<String, Object>> products = (List<Map<String, Object>>) document.get("cartItems");
                            if (products != null) {
                                for (Map<String, Object> product : products) {
                                    String productName = (String) product.get("productName");
                                    Long quantity = (Long) product.get("quantity");

                                    if (productSales.containsKey(productName)) {
                                        productSales.put(productName, productSales.get(productName) + quantity.intValue());
                                    } else {
                                        productSales.put(productName, quantity.intValue());
                                        productNames.add(productName); // Add product to the list
                                    }
                                }
                            }
                        }

                        // Prepare data for the chart
                        List<BarEntry> entries = new ArrayList<>();
                        int index = 0;
                        for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
                            entries.add(new BarEntry(index++, entry.getValue()));
                        }

                        // Create BarDataSet from data
                        BarDataSet dataSet = new BarDataSet(entries, "Top Selling Products");
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                        // Create BarData from BarDataSet
                        BarData data = new BarData(dataSet);

                        // Update X-axis labels with product names
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(productNames)); // Update X-axis with product names
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        xAxis.setGranularityEnabled(true);

                        // Set the data into the chart
                        barChart.setData(data);
                        barChart.invalidate();  // Refresh the chart
                    } else {
                        Toast.makeText(ThongKe_Activyti.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Thống kê doanh thu theo ngày
    private void getOrdersByDate() {
        db.collection("orders")
                .whereEqualTo("status", "Delivered") // Filter by "Delivered" status
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documentSnapshots = task.getResult();
                        Map<String, Double> dailySales = new HashMap<>();
                        List<String> dates = new ArrayList<>(); // List of dates

                        for (QueryDocumentSnapshot document : documentSnapshots) {
                            String orderDate = document.getString("orderDate");
                            Double totalPrice = document.getDouble("totalPrice");

                            if (dailySales.containsKey(orderDate)) {
                                dailySales.put(orderDate, dailySales.get(orderDate) + totalPrice);
                            } else {
                                dailySales.put(orderDate, totalPrice);
                                dates.add(orderDate);  // Add date to the list
                            }
                        }

                        // Prepare data for the chart
                        List<BarEntry> entries = new ArrayList<>();
                        int index = 0;
                        for (Map.Entry<String, Double> entry : dailySales.entrySet()) {
                            entries.add(new BarEntry(index++, entry.getValue().floatValue()));
                        }

                        // Create BarDataSet from collected data
                        BarDataSet dataSet = new BarDataSet(entries, "Revenue by Date");
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                        // Create BarData from BarDataSet
                        BarData data = new BarData(dataSet);

                        // Update X-axis labels with the actual dates
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates)); // Update X-axis
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        xAxis.setGranularityEnabled(true);

                        // Set the data into the chart
                        barChart.setData(data);
                        barChart.invalidate();  // Refresh the chart
                    } else {
                        Toast.makeText(ThongKe_Activyti.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Thống kê doanh thu theo tháng
    private void getTkThang() {
        db.collection("orders")
                .whereEqualTo("status", "Delivered") // Filter by "Delivered" status
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documentSnapshots = task.getResult();
                        Map<String, Double> monthlySales = new HashMap<>();
                        List<String> months = new ArrayList<>();

                        for (QueryDocumentSnapshot document : documentSnapshots) {
                            String orderDate = document.getString("orderDate");
                            Double totalPrice = document.getDouble("totalPrice");

                            String monthYear = getMonthYearFromDate(orderDate);  // Get month and year from date

                            if (monthlySales.containsKey(monthYear)) {
                                monthlySales.put(monthYear, monthlySales.get(monthYear) + totalPrice);
                            } else {
                                monthlySales.put(monthYear, totalPrice);
                                months.add(monthYear);  // Add monthYear to the list
                            }
                        }

                        // Prepare data for the chart
                        List<BarEntry> entries = new ArrayList<>();
                        int index = 0;
                        for (Map.Entry<String, Double> entry : monthlySales.entrySet()) {
                            entries.add(new BarEntry(index++, entry.getValue().floatValue()));
                        }

                        // Create BarDataSet from data
                        BarDataSet dataSet = new BarDataSet(entries, "Revenue by Month");
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                        // Create BarData from BarDataSet
                        BarData data = new BarData(dataSet);

                        // Update X-axis labels with month names
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(months)); // Update X-axis with months
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        xAxis.setGranularityEnabled(true);

                        // Set the data into the chart
                        barChart.setData(data);
                        barChart.invalidate();  // Refresh the chart
                    } else {
                        Toast.makeText(ThongKe_Activyti.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Phương thức giúp lấy tháng và năm từ ngày
    private String getMonthYearFromDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Use ISO 8601 format for date
            sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure the date is interpreted in UTC
            Date d = sdf.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            int month = calendar.get(Calendar.MONTH) + 1; // Extract month
            int year = calendar.get(Calendar.YEAR); // Extract year
            return String.format("%04d-%02d", year, month); // Format as YYYY-MM
        } catch (ParseException e) {
            e.printStackTrace();
            return "0000-00"; // Default return value in case of error
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