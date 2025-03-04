package foly.anhld.duan1.adater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import foly.anhld.duan1.Modol.Creator;
import foly.anhld.duan1.R;

public class CreatorAdapter extends RecyclerView.Adapter<CreatorAdapter.CreatorViewHolder> {

    private List<Creator> creatorList;

    public CreatorAdapter(List<Creator> creatorList) {
        this.creatorList = creatorList;
    }

    @NonNull
    @Override
    public CreatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_creator, parent, false);
        return new CreatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatorViewHolder holder, int position) {
        Creator creator = creatorList.get(position);
        holder.txtName.setText(creator.getName());
        holder.imgAvatar.setImageResource(creator.getAvatar());
    }

    @Override
    public int getItemCount() {
        return creatorList.size();
    }

    public static class CreatorViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtName;

        public CreatorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(foly.anhld.duan1.R.id.imgAvatar);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}

