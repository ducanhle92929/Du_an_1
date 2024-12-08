package foly.anhld.duan1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import foly.anhld.duan1.Modol.Category;
import foly.anhld.duan1.Modol.SanPham;
import foly.anhld.duan1.Modol.SizeWithQuantity;

public class QuanLySanPhamAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private foly.anhld.duan1.adater.SanPhamAdapter adapter;
    private List<SanPham> productList;
    private FirebaseFirestore db;
    private List<Category> categoryList;  // List to store fetched categories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham_admin);

        // Initialize views
        recyclerView = findViewById(R.id.rcvProduct);
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new foly.anhld.duan1.adater.SanPhamAdapter(productList);
        recyclerView.setAdapter(adapter);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);

        // Initialize category list
        categoryList = new ArrayList<>();

        // Connect to Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch existing products from Firestore
        fetchProducts();

        // Fetch categories from Firestore
        fetchCategories();

        // OnClickListener for add button
        findViewById(R.id.btnadd).setOnClickListener(v -> showAddProductDialog());

        navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                // Mở màn hình thống kê
                startActivity(new Intent(QuanLySanPhamAdmin.this, QuanLySanPhamAdmin.class));
                return true;
            } else if (itemId == R.id.navigation_thongKe) {
                // Quay về màn hình chính
                startActivity(new Intent(QuanLySanPhamAdmin.this, ThongKe_Activyti.class));
                return true;
            }
//    else if (itemId == R.id.navigation_notification) {
//        // Mở thông báo
//        startActivity(new Intent(QuanLySanPhamAdmin.this, NotificationActivity.class));
//        return true;
//    }
            return false;
        });
    }


    private void fetchProducts() {
        db.collection("sanpham")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        SanPham sanPham = document.toObject(SanPham.class);
                        if (sanPham != null) {
                            sanPham.setMaSanPham(document.getId());
                            productList.add(sanPham);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching products", e));
    }

    private void fetchCategories() {
        db.collection("danhmuc")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categoryList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Category category = document.toObject(Category.class);
                        if (category != null) {
                            categoryList.add(category);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching categories", e));
    }

    private void showAddProductDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_item);

        // Link views
        EditText edtName = dialog.findViewById(R.id.etProductName);
        EditText edtDescription = dialog.findViewById(R.id.etProductDescription);
        EditText edtPrice = dialog.findViewById(R.id.etProductPrice);
        EditText edtImage = dialog.findViewById(R.id.etImage);
        Spinner spinnerCategory = dialog.findViewById(R.id.spinnerCategory);
        Button btnAddProduct = dialog.findViewById(R.id.btnAddProduct);
        Button btnAddSize = dialog.findViewById(R.id.btnAddSize);
        Button btnAddCategory = dialog.findViewById(R.id.btnAddCategory);
        LinearLayout layoutSizes = dialog.findViewById(R.id.layoutSizes);

        List<SizeWithQuantity> sizesWithQuantities = new ArrayList<>();

        // Add size fields dynamically
        btnAddSize.setOnClickListener(v -> {
            LinearLayout sizeLayout = new LinearLayout(this);
            sizeLayout.setOrientation(LinearLayout.HORIZONTAL);

            EditText sizeField = new EditText(this);
            sizeField.setHint("Size");
            sizeField.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            EditText quantityField = new EditText(this);
            quantityField.setHint("Quantity");
            quantityField.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            quantityField.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            sizeLayout.addView(sizeField);
            sizeLayout.addView(quantityField);
            layoutSizes.addView(sizeLayout);
        });

        // Set categories into the Spinner
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        btnAddCategory.setOnClickListener(v -> showAddCategoryDialog(dialog, categoryAdapter));

        btnAddProduct.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String imageUrl = edtImage.getText().toString().trim();
            String selectedCategoryName = spinnerCategory.getSelectedItem().toString();

            sizesWithQuantities.clear();
            for (int i = 0; i < layoutSizes.getChildCount(); i++) {
                LinearLayout sizeLayout = (LinearLayout) layoutSizes.getChildAt(i);
                EditText sizeField = (EditText) sizeLayout.getChildAt(0);
                EditText quantityField = (EditText) sizeLayout.getChildAt(1);

                String size = sizeField.getText().toString().trim();
                String quantityStrForSize = quantityField.getText().toString().trim();

                if (!size.isEmpty() && !quantityStrForSize.isEmpty()) {
                    try {
                        int quantityForSize = Integer.parseInt(quantityStrForSize);
                        sizesWithQuantities.add(new SizeWithQuantity(size, quantityForSize));
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid quantity for size!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            if (!name.isEmpty() && !description.isEmpty() && !priceStr.isEmpty() && !imageUrl.isEmpty() && !sizesWithQuantities.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr);

                    // Find the Category object corresponding to the selected category name
                    Category selectedCategory = null;
                    for (Category category : categoryList) {
                        if (category.getName().equals(selectedCategoryName)) {
                            selectedCategory = category;
                            break;
                        }
                    }

                    SanPham newProduct = new SanPham(null, name, price, 0, description, imageUrl, sizesWithQuantities, selectedCategory);

                    db.collection("sanpham")
                            .add(newProduct)
                            .addOnSuccessListener(documentReference -> {
                                String documentId = documentReference.getId();
                                newProduct.setMaSanPham(documentId);
                                documentReference.update("maSanPham", documentId);

                                productList.add(newProduct);
                                adapter.notifyItemInserted(productList.size() - 1);

                                Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to add product!", Toast.LENGTH_SHORT).show());
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Price must be a valid number!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showAddCategoryDialog(Dialog parentDialog, ArrayAdapter<String> categoryAdapter) {
        Dialog categoryDialog = new Dialog(this);
        categoryDialog.setContentView(R.layout.dialog_add_category);
        EditText edtCategoryName = categoryDialog.findViewById(R.id.etCategoryName);
        Button btnSaveCategory = categoryDialog.findViewById(R.id.btnSaveAddCategory);

        btnSaveCategory.setOnClickListener(v -> {
            String categoryName = edtCategoryName.getText().toString().trim();

            if (!categoryName.isEmpty()) {
                // Create new Category object
                Category newCategory = new Category(categoryName);

                // Save into Firestore
                db.collection("danhmuc")
                        .add(newCategory)
                        .addOnSuccessListener(documentReference -> {
                            // Add new category to list and notify the adapter
                            categoryList.add(newCategory);
                            categoryAdapter.add(categoryName);  // Update the Spinner in the dialog
                            categoryAdapter.notifyDataSetChanged();  // Notify the Spinner adapter

                            // Close category dialog
                            categoryDialog.dismiss();

                            Toast.makeText(this, "Category added successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Failed to add category", e);
                            Toast.makeText(this, "Failed to add category!", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Please enter a category name!", Toast.LENGTH_SHORT).show();
            }
        });

        categoryDialog.show();
    }
}