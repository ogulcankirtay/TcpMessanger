package com.ogulcankirtay.tcpmessanger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PastDataAdapter extends RecyclerView.Adapter<PastDataAdapter.PastDataHolder> {
   ArrayList<String> data;

    public PastDataAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public PastDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row,parent,false);
        return new PastDataHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PastDataHolder holder, int position) {
        holder.t.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.toArray().length;
    }

    public class PastDataHolder extends RecyclerView.ViewHolder{
        TextView t;
        public PastDataHolder(@NonNull View itemView) {
            super(itemView);
            t=itemView.findViewById(R.id.textitem);
        }
    }
}
