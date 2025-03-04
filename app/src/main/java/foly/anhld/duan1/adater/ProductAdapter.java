package foly.anhld.duan1.adater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import foly.anhld.duan1.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Map<String, Object>> productList;

    public ProductAdapter(List<Map<String, Object>> productList) {
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_order_user, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Map<String, Object> product = productList.get(position);

        String productName = (String) product.get("productName");
        Long quantity = (Long) product.get("quantity");
        Double price = (Double) product.get("productPrice");
        String size = (String) product.get("size");
        String imageUrl = (String) product.get("productImage");

        holder.productName.setText(productName);
        holder.productQuantity.setText("Quantity: " + quantity);
        holder.productPrice.setText("Price: " + price);
        Picasso.get().load(imageUrl).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProducts(List<Map<String, Object>> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productQuantity, productPrice;
        ImageView productImage;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvProductNameuser);
            productQuantity = itemView.findViewById(R.id.tvsoluonguser);
            productPrice = itemView.findViewById(R.id.tvPriceuser);
            productImage = itemView.findViewById(R.id.ivproductImageuser);
        }
    }
}
