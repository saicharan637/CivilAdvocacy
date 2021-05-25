package com.example.civiladvocacy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Adapter  extends RecyclerView.Adapter<ViewHolder> {
    private final List<OfficialDetails> officialDetailsList;
    MainActivity mainActivity;

    public Adapter(List<OfficialDetails> officialDetails, MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.officialDetailsList = officialDetails;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder,parent,false);
        view.setOnClickListener(mainActivity);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        OfficialDetails officialDetails = officialDetailsList.get(position);
        viewholder.office.setText(officialDetails.getOffice());
        viewholder.name.setText(officialDetails.getName());
        String party = officialDetails.getParty();
        if(party.equals("Democratic Party")){
            String s = "("+party+")";
            viewholder.party.setText(s);
        }else {
            String s = "("+party+")";
            viewholder.party.setText(s);
        }

    }

    @Override
    public int getItemCount() {
        return officialDetailsList.size();
    }
}
