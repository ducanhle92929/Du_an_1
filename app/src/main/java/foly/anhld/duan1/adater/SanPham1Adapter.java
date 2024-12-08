package foly.anhld.duan1.adater;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import foly.anhld.duan1.Modol.SizeWithQuantity;
import foly.anhld.duan1.R;

public class SanPham1Adapter extends RecyclerView.Adapter<SanPham1Adapter.SizeViewHolder> {

    private List<SizeWithQuantity> sizes;  // Danh sách kích thước với số lượng
    private OnSizeClickListener onSizeClickListener;
    private int selectedPosition = -1;

    public interface OnSizeClickListener {
        void onSizeClick(SizeWithQuantity sizeWithQuantity);  // Sử dụng đối tượng SizeWithQuantity
    }

    public SanPham1Adapter(List<SizeWithQuantity> sizes, OnSizeClickListener onSizeClickListener) {
        this.sizes = sizes;
        this.onSizeClickListener = onSizeClickListener;
    }

    @Override
    public SizeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item (mỗi kích thước)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_size, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SizeViewHolder holder, int position) {
        SizeWithQuantity sizeWithQuantity = sizes.get(position);
        holder.sizeTextView.setText(sizeWithQuantity.getSize());  // Hiển thị kích thước
        Log.d("SanPham1Adapter", "Size: " + sizeWithQuantity.getSize());

        // Sử dụng holder.getAdapterPosition() để lấy vị trí item hiện tại
        int adapterPosition = holder.getAdapterPosition();

        // Thay đổi kiểu dáng của TextView khi kích thước được chọn
        if (adapterPosition == selectedPosition) {
            holder.sizeTextView.setTypeface(null, Typeface.BOLD);  // Tô đậm
            holder.sizeTextView.setTextColor(holder.sizeTextView.getContext().getResources().getColor(R.color.colorSelected));  // Đổi màu khi chọn
        } else {
            holder.sizeTextView.setTypeface(null, Typeface.NORMAL);  // Bỏ tô đậm
            holder.sizeTextView.setTextColor(holder.sizeTextView.getContext().getResources().getColor(R.color.colorDefault));  // Màu mặc định
        }

        holder.itemView.setOnClickListener(v -> {
            // Lấy adapter position trong onClick để xử lý sự kiện click
            int clickedPosition = holder.getAdapterPosition();

            // Kiểm tra nếu click vào cùng một item thì bỏ chọn
            if (clickedPosition == selectedPosition) {
                selectedPosition = -1;  // Bỏ chọn nếu item đã được chọn
            } else {
                selectedPosition = clickedPosition;  // Cập nhật vị trí item được chọn
            }

            notifyDataSetChanged();  // Cập nhật RecyclerView để hiển thị thay đổi

            if (onSizeClickListener != null) {
                onSizeClickListener.onSizeClick(sizeWithQuantity);  // Gửi đối tượng SizeWithQuantity khi click
            }
        });
    }

    @Override
    public int getItemCount() {
        return sizes.size();  // Trả về số lượng phần tử trong danh sách
    }

    public static class SizeViewHolder extends RecyclerView.ViewHolder {

        TextView sizeTextView;

        public SizeViewHolder(View itemView) {
            super(itemView);
            sizeTextView = itemView.findViewById(R.id.sizeOption); // TextView để hiển thị kích thước
        }
    }
}
