package foly.anhld.duan1.adater;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import foly.anhld.duan1.Modol.SanPham;
import foly.anhld.duan1.R;
import foly.anhld.duan1.SanPham1;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private final Context context;
    private final List<SanPham> productList;

    public HomeAdapter(Context context, List<SanPham> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item_home.xml layout for each product
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the product at the current position
        SanPham product = productList.get(position);
        // Update the product name and price in the view
        holder.id.setText("Mã sản phẩm: " + product.getMaSanPham());
        holder.name.setText("Tên sản phẩm: " + product.getTenSanPham());
        holder.price.setText("Giá: " + product.getGia());

        // Load the product image using Picasso
        String imageUrl = product.getHinhAnh();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_background); // Default image
        }

        // Set an item click listener to open product details activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SanPham1.class);
            intent.putExtra("product_id", product.getMaSanPham());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of products
        return productList != null ? productList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price,id;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bind the views
            id = itemView.findViewById(R.id.tvid);
            id.setVisibility(View.GONE);
            name = itemView.findViewById(R.id.tenSP);
            price = itemView.findViewById(R.id.giaSP);
            productImage = itemView.findViewById(R.id.imgSP);
        }
    }
}
