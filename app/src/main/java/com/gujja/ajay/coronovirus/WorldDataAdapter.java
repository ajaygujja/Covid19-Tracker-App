package com.gujja.ajay.coronovirus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorldDataAdapter extends RecyclerView.Adapter<WorldDataAdapter.WorldDataViewHolder> {

    private final Context context;
    private ArrayList<CovidCountry> covidCountries;


    WorldDataAdapter(Context context, ArrayList<CovidCountry> covidCountries) {
        this.covidCountries = covidCountries;
        this.context = context;
    }

    @NonNull
    @Override
    public WorldDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_world_data_list, parent, false);

        return new WorldDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorldDataViewHolder holder, int position) {
        CovidCountry covidCountry = covidCountries.get(position);
        String countryFlagUrl = covidCountry.getmCovidCountryImages();

        holder.worldDataTitle.setText(covidCountry.getmCovidCountry());
        holder.worldDataCasesCount.setText(covidCountry.getmCovidCases());
        holder.worldDataDeathCount.setText(covidCountry.getmCovidDeath());
        Picasso.get().load(countryFlagUrl).fit().centerInside().into(holder.countryFlags);


    }

    @Override
    public int getItemCount() {
        return covidCountries.size();
    }

    void filteredList(ArrayList<CovidCountry> filteredCountries) {

        covidCountries = filteredCountries;
        notifyDataSetChanged();
    }

    static class WorldDataViewHolder extends RecyclerView.ViewHolder {
        TextView worldDataTitle, worldDataDeathCount, worldDataCasesCount;
        ImageView countryFlags;

        WorldDataViewHolder(@NonNull View itemView) {
            super(itemView);

            worldDataTitle = itemView.findViewById(R.id.WorldDataName);
            worldDataDeathCount = itemView.findViewById(R.id.WorldDataDeathCount);
            worldDataCasesCount = itemView.findViewById(R.id.WorldDataCasesCount);
            countryFlags = itemView.findViewById(R.id.covCountryFlags);

        }
    }
}
