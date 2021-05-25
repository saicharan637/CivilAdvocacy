package com.example.civiladvocacy;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView office;
    public TextView party;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name_view);
        office = itemView.findViewById(R.id.office);
        party = itemView.findViewById(R.id.party);
    }
}