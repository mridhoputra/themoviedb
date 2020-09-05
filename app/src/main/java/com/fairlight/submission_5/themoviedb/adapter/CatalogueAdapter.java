package com.fairlight.submission_5.themoviedb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fairlight.submission_5.themoviedb.R;
import com.fairlight.submission_5.themoviedb.model.Catalogue;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.CatalogueViewHolder> {
    private ArrayList<Catalogue> listCatalogue = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public ArrayList<Catalogue> getData() {
        return listCatalogue;
    }

    public void setData(ArrayList<Catalogue> items) {
        listCatalogue.clear();
        listCatalogue.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CatalogueViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_catalogue_list, viewGroup, false);
        return new CatalogueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogueViewHolder viewHolder, int position) {
        viewHolder.bind(listCatalogue.get(position));
    }

    @Override
    public int getItemCount() {
        return listCatalogue.size();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Catalogue catalogue);
    }

    class CatalogueViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBackdrop;
        TextView tvTitle;
        TextView tvReleased_date;
        TextView tvUser_score;

        CatalogueViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBackdrop = itemView.findViewById(R.id.img_catalogue_backdrop);
            tvTitle = itemView.findViewById(R.id.tv_catalogue_title);
            tvReleased_date = itemView.findViewById(R.id.tv_catalogue_released_date);
            tvUser_score = itemView.findViewById(R.id.tv_catalogue_user_score);
        }

        void bind(final Catalogue items) {
            if (items.getBackdrop_url().contains("null")) {
                Glide.with(itemView.getContext())
                        .load(R.drawable.backdrop_not_yet_available)
                        .into(imgBackdrop);
            } else {
                Glide.with(itemView.getContext())
                        .load(items.getBackdrop_url())
                        .into(imgBackdrop);
            }
            tvTitle.setText(items.getTitle());
            tvReleased_date.setText(items.getReleased_date());
            tvUser_score.setText(items.getUser_score());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(items);
                }
            });
        }
    }
}
