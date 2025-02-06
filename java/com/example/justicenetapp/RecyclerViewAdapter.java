package com.example.justicenetapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class WebActivityAdapter extends RecyclerView.Adapter<WebActivityAdapter.ViewHolder> {

    private final List<WebActivityLog> webActivityLogs;

    public WebActivityAdapter(List<WebActivityLog> webActivityLogs) {
        this.webActivityLogs = webActivityLogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_web, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WebActivityLog log = webActivityLogs.get(position);
        holder.urlTextView.setText(log.getUrl());
        holder.timestampTextView.setText(log.getTimestamp());
        holder.locationTextView.setText(log.getLocation());
    }

    @Override
    public int getItemCount() {
        return webActivityLogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView urlTextView, timestampTextView, locationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            urlTextView = itemView.findViewById(R.id.urlTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
        }
    }

}
