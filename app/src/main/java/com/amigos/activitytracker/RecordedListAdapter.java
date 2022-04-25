package com.amigos.activitytracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;

public class RecordedListAdapter extends RecyclerView.Adapter<RecordedListAdapter.recordedListViewHolder> {
    private File[] listOfFiles;
    private onItemClick itemClick;

    public RecordedListAdapter(File[] listOfFiles, onItemClick itemClick){
        this.listOfFiles = listOfFiles;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public recordedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recorded_list_item,parent,false);
        return new recordedListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recordedListViewHolder holder, int position) {
        holder.fileName.setText(listOfFiles[position].getName());
    }

    @Override
    public int getItemCount() {
        return listOfFiles.length;
    }

    public class recordedListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView fileName;
        public recordedListViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.filename);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onClickListner(listOfFiles[getAdapterPosition()],getAdapterPosition());
        }
    }

    public interface onItemClick{
        void onClickListner(File file, int position);
    }
}
