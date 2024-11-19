package com.example.onwheels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<CreateWheelsActivity.Place> places;
    private OnPlaceClickListener listener;

    public interface OnPlaceClickListener {
        void onPlaceClick(CreateWheelsActivity.Place place);
    }

    public PlaceAdapter(List<CreateWheelsActivity.Place> places) {
        this.places = places;
    }

    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        CreateWheelsActivity.Place place = places.get(position);
        holder.textView.setText(place.getFormatted());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlaceClick(place);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        PlaceViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}

