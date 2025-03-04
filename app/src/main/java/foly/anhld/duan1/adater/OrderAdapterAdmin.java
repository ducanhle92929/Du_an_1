package foly.anhld.duan1.adater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import foly.anhld.duan1.Modol.Order;
import foly.anhld.duan1.OrderDetailActivityUser;
import foly.anhld.duan1.R;

public class OrderAdapterAdmin extends RecyclerView.Adapter<OrderAdapterAdmin.OrderViewHolder> {
    private List<Order> orderList;
    private Context context;

    public OrderAdapterAdmin(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_chi_tiet_san_pham_admin, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderIdTextView.setText("Mã đơn hàng: " + order.getOrderId());
        holder.orderDateTextView.setText("Ngày đặt: " + order.getOrderDate());
        holder.statusTextView.setText("Trạng thái: " + order.getStatus());
        holder.totalPriceTextView.setText("Tổng tiền: " + order.getTotalPrice() + " VND");

        holder.txtOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy context từ view
                Context context = v.getContext();
                // Cập nhật trạng thái đơn hàng thành "Transit" và truyền vào position

                // Mở OrderDetailActivityUser
                Intent intent = new Intent(context, OrderDetailActivityUser.class);
                intent.putExtra("ORDER_ID", order.getOrderId()); // Gửi mã đơn hàng
                intent.putExtra("userId", order.getUserId());
                context.startActivity(intent);
            }
        });
        holder.txtOder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy context từ view
                Context context = v.getContext();
                // Cập nhật trạng thái đơn hàng thành "Transit" và truyền vào position
                updateOrderStatusToTransit(order.getOrderId(), position);
                // Mở OrderDetailActivityUser
                Intent intent = new Intent(context, OrderDetailActivityUser.class);
                intent.putExtra("ORDER_ID", order.getOrderId()); // Gửi mã đơn hàng
                intent.putExtra("userId", order.getUserId());

            }
        });
        holder.txtOder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy context từ view
                Context context = v.getContext();

                // Cập nhật trạng thái đơn hàng thành "Delivered" và truyền vào position
                updateOrderStatusToDelivered(order.getOrderId(), position);

                // Mở OrderDetailActivityUser nếu cần
                Intent intent = new Intent(context, OrderDetailActivityUser.class);
                intent.putExtra("ORDER_ID", order.getOrderId()); // Gửi mã đơn hàng
                intent.putExtra("userId", order.getUserId());
            }
        });

    }



    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public void updateOrders(List<Order> newOrderList) {
        this.orderList = new ArrayList<>(newOrderList);
        notifyDataSetChanged();  // Cập nhật giao diện RecyclerView
    }
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView orderDateTextView;
        TextView statusTextView;
        TextView totalPriceTextView;
        TextView txtOder, txtOder1, txtOder2;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.tvOrderIdadmin);
            orderDateTextView = itemView.findViewById(R.id.tvOrderDateadmin);
            statusTextView = itemView.findViewById(R.id.tvStatusadmin);
            totalPriceTextView = itemView.findViewById(R.id.tvTotalPriceadmin);
            txtOder = itemView.findViewById(R.id.txtOderadmin);
            txtOder1 = itemView.findViewById(R.id.txtOderadmin1);
            txtOder2 = itemView.findViewById(R.id.txtOderadmin2);
        }
    }
    private void updateOrderStatusToTransit(String orderId, int position) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("orders")
                .document(orderId)
                .update("status", "Transit")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Cập nhật trạng thái đơn hàng trong danh sách và làm mới item
                        orderList.get(position).setStatus("Transit");
                        notifyItemChanged(position); // Cập nhật lại chỉ một item trong RecyclerView
                        Toast.makeText(context, "Trạng thái đơn hàng đã được cập nhật thành 'Transit'.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Lỗi khi cập nhật
                        Toast.makeText(context, "Cập nhật trạng thái thất bại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateOrderStatusToDelivered(String orderId, int position) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("orders")
                .document(orderId)
                .update("status", "Delivered")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Cập nhật trạng thái đơn hàng trong danh sách và làm mới item
                        orderList.get(position).setStatus("Delivered");
                        notifyItemChanged(position); // Cập nhật lại chỉ một item trong RecyclerView
                        Toast.makeText(context, "Trạng thái đơn hàng đã được cập nhật thành 'Delivered'.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Lỗi khi cập nhật
                        Toast.makeText(context, "Cập nhật trạng thái thất bại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
