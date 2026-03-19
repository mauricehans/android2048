package com.example.android2048;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TileAdapter extends ListAdapter<Integer, TileAdapter.TileViewHolder> {
    public TileAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Integer> DIFF_CALLBACK = new DiffUtil.ItemCallback<Integer>() {
        @Override
        public boolean areItemsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile, parent, false);
        return new TileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int position) {
        Integer value = getItem(position);
        holder.bind(value);
    }

    static class TileViewHolder extends RecyclerView.ViewHolder {
        TextView tile;

        public TileViewHolder(@NonNull View itemView) {
            super(itemView);
            tile = itemView.findViewById(R.id.tile);
        }

        public void bind(Integer value) {
            switch (value) {
                case 0:
                    tile.setText("");
                    tile.setBackgroundColor(0xFFCDC1B4);
                    break;
                case 2:
                    tile.setText("2");
                    tile.setBackgroundColor(0xFFCDC1B4);
                    break;
                case 4:
                    tile.setText("4");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 8:
                    tile.setText("8");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 16:
                    tile.setText("16");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 32:
                    tile.setText("32");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 64:
                    tile.setText("64");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 128:
                    tile.setText("128");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 256:
                    tile.setText("256");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 512:
                    tile.setText("512");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 1024:
                    tile.setText("1024");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 2048:
                    tile.setText("2048");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 4096:
                    tile.setText("4096");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
                case 8192:
                    tile.setText("8192");
                    tile.setBackgroundColor(0xFFF2B179);
                    break;
            }
        }
    }
}