package com.example.t6mobile_h071231080;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    private final List<Character> characters;
    private final Context context;

    public CharacterAdapter(Context context) {
        this.context = context;
        this.characters = new ArrayList<>();
    }

    public void addCharacters(List<Character> newCharacters) {
        int startPosition = characters.size();
        this.characters.addAll(newCharacters);
        notifyItemRangeInserted(startPosition, newCharacters.size());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_character, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Character character = characters.get(position);


        holder.tvCharacterName.setText(character.getName());
        holder.tvCharacterSpecies.setText(character.getSpecies());

        // Load gambar karakter menggunakan Glide
        Glide.with(context)
                .load(character.getImage())
                .into(holder.ivCharacter);


        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("CHARACTER_ID", character.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCharacter;
        TextView tvCharacterName, tvCharacterSpecies;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCharacter = itemView.findViewById(R.id.iv_character);
            tvCharacterName = itemView.findViewById(R.id.tv_character_name);
            tvCharacterSpecies = itemView.findViewById(R.id.tv_character_species);
        }
    }
}