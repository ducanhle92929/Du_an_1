package foly.anhld.duan1.adater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import foly.anhld.duan1.Modol.SanPham;
import foly.anhld.duan1.Modol.SizeWithQuantity;
import foly.anhld.duan1.R;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {

    private List<SanPham> productList;
    private Context context;

    public SanPhamAdapter(List<SanPham> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPham = productList.get(position);

        // Use Firestore document ID as the product ID
        String productId = sanPham.getMaSanPham(); // This should now hold the document ID

        holder.name.setText("Tên sản phẩm: " + sanPham.getTenSanPham());
        holder.price.setText("Giá: " + sanPham.getGia());
        holder.description.setText("Mô tả: " + sanPham.getMoTa());

        // Show product ID
        holder.id.setText("Mã sản phẩm: " + (productId != null ? productId : "ID không có"));

        // Hiển thị danh mục sản phẩm
        if (sanPham.getCategory() != null) {
            holder.category.setText("Danh mục: " + sanPham.getCategory().getName());
        } else {
            holder.category.setText("Danh mục: Không có");
        }
        // Show sizes and quantities if available
        StringBuilder sizesWithQuantities = new StringBuilder("Sizes: ");
        List<SizeWithQuantity> sizes = sanPham.getSizes();
        if (sizes != null && !sizes.isEmpty()) {
            for (SizeWithQuantity size : sizes) {
                sizesWithQuantities.append(size.getSize())
                        .append(": ")
                        .append(size.getQuantity())
                        .append(" Product, ");
            }
            if (sizesWithQuantities.length() > 6) {
                sizesWithQuantities.setLength(sizesWithQuantities.length() - 2);
            }
        } else {
            sizesWithQuantities.append("No sizes available");
        }
        holder.sizes.setText(sizesWithQuantities.toString());

        // Load product image using Picasso
        String imageUrl = sanPham.getHinhAnh();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Handle edit and delete actions
        holder.editButton.setOnClickListener(v -> openEditDialog(sanPham, position, holder));
        holder.deleteButton.setOnClickListener(v -> deleteProduct(sanPham, position, holder));
    }

    private void openEditDialog(SanPham sanPham, int position, ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        View dialogView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        EditText etId = dialogView.findViewById(R.id.etProductId);
        EditText etName = dialogView.findViewById(R.id.etProductName);
        EditText etPrice = dialogView.findViewById(R.id.etProductPrice);
        EditText etDescription = dialogView.findViewById(R.id.etProductDescription);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Initialize fields with current product data
        etId.setText(String.valueOf(sanPham.getMaSanPham()));
        etId.setEnabled(false);
        etName.setText(sanPham.getTenSanPham());
        etPrice.setText(String.valueOf(sanPham.getGia()));
        etDescription.setText(sanPham.getMoTa());

        AlertDialog dialog = builder.create();

        // Save changes
        btnSave.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String newPriceStr = etPrice.getText().toString().trim();
            String newDescription = etDescription.getText().toString().trim();

            if (!newName.isEmpty() && !newPriceStr.isEmpty() && !newDescription.isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(newPriceStr);

                    // Update Firestore with new data
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    HashMap<String, Object> updatedData = new HashMap<>();
                    updatedData.put("tenSanPham", newName);
                    updatedData.put("gia", newPrice);
                    updatedData.put("moTa", newDescription);

                    db.collection("sanpham").document(sanPham.getMaSanPham())  // Ensure you're using the correct product ID here
                            .update(updatedData)
                            .addOnSuccessListener(aVoid -> {
                                sanPham.setTenSanPham(newName);
                                sanPham.setGia(newPrice);
                                sanPham.setMoTa(newDescription);
                                notifyItemChanged(position);

                                Toast.makeText(holder.itemView.getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(holder.itemView.getContext(), "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
                            });
                } catch (NumberFormatException e) {
                    Toast.makeText(holder.itemView.getContext(), "Giá phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(holder.itemView.getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel button to dismiss the dialog
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void deleteProduct(SanPham sanPham, int position, ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("sanpham").document(sanPham.getMaSanPham())  // Ensure you're using the correct product ID here
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                productList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(holder.itemView.getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(holder.itemView.getContext(), "Lỗi khi xóa sản phẩm!", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                .show();
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price, id, sizes, category;
        ImageView productImage;
        Button editButton, deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.product_id);
            name = itemView.findViewById(R.id.product_name);
            description = itemView.findViewById(R.id.product_description);
            price = itemView.findViewById(R.id.product_price);
            sizes = itemView.findViewById(R.id.product_sizes);
            productImage = itemView.findViewById(R.id.product_image);
            category = itemView.findViewById(R.id.product_category);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
