package com.example.android2048;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;

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

        holder.itemView.animate().cancel();  // On annule, pour passer outre le recyclage
        /*if (value > 0) {
            holder.itemView.setScaleX(0.8f);
            holder.itemView.setScaleY(0.8f);
            holder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
        }
        else {
            // 3. IMPORTANT : Si la case est vide (0), on s'assure qu'elle reprend sa taille normale
            // Sinon, les cases vides recyclées resteront à 0.8f
            holder.itemView.setScaleX(1f);
            holder.itemView.setScaleY(1f);
        }*/
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
                    tile.setBackgroundColor(Color.parseColor("#f3ccff"));
                    break;
                case 2:
                    tile.setText("2");
                    tile.setBackgroundColor(Color.parseColor("#F2EF8A"));
                    break;
                case 4:
                    tile.setText("4");
                    tile.setBackgroundColor(Color.parseColor("#f7f36f"));
                    break;
                case 8:
                    tile.setText("8");
                    tile.setBackgroundColor(Color.parseColor("#ffef3b"));
                    break;
                case 16:
                    tile.setText("16");
                    tile.setBackgroundColor(Color.parseColor("#ffcc00"));
                    break;
                case 32:
                    tile.setText("32");
                    tile.setBackgroundColor(Color.parseColor("#ffc757"));
                    break;
                case 64:
                    tile.setText("64");
                    tile.setBackgroundColor(Color.parseColor("#ffc757"));
                    break;
                case 128:
                    tile.setText("128");
                    tile.setBackgroundColor(Color.parseColor("#ffbc75"));
                    break;
                case 256:
                    tile.setText("256");
                    tile.setBackgroundColor(Color.parseColor("#ffa600"));
                    break;
                case 512:
                    tile.setText("512");
                    tile.setBackgroundColor(Color.parseColor("#ffba91"));
                    break;
                case 1024:
                    tile.setText("1024");
                    tile.setBackgroundColor(Color.parseColor("#ff9900"));
                    break;
                case 2048:
                    tile.setText("2048");
                    tile.setBackgroundColor(Color.parseColor("#a82ed9"));
                    break;
                case 4096:
                    tile.setText("4096");
                    tile.setBackgroundColor(Color.parseColor("#ff6e30"));
                    break;
                case 8192:
                    tile.setText("8192");
                    tile.setBackgroundColor(Color.parseColor("#ff351f"));
                    break;
            }
        }
    }
}