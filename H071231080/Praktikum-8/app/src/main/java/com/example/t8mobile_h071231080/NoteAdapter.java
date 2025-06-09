package com.example.t8mobile_h071231080;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesViewHolder> {
    private final ArrayList<Note> notes = new ArrayList<>();
    private final Activity activity;
    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes.clear();
        if (notes.size() > 0) {
            this.notes.addAll(notes);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent,
                false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        final TextView tvJudul, tvDeskrispi, tvCreatedAt, tvUpdatedAt;
        final CardView cardView;

    NotesViewHolder(View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_item_judul);
            tvDeskrispi = itemView.findViewById(R.id.tv_item_deskripsi);
            tvCreatedAt = itemView.findViewById(R.id.tv_created_at);
            tvUpdatedAt = itemView.findViewById(R.id.tv_updated_at);
            cardView = itemView.findViewById(R.id.card_view);
        }

        void bind(Note note) {
            tvJudul.setText(note.getJudul());
            tvDeskrispi.setText(note.getDeskripsi());

            if (note.getCreatedAt() != null && !note.getCreatedAt().isEmpty()) {
                tvCreatedAt.setText("Created at " + note.getCreatedAt());
                tvCreatedAt.setVisibility(View.VISIBLE);
            } else {
                tvCreatedAt.setVisibility(View.GONE);
            }

            if (note.getUpdatedAt() != null && !note.getUpdatedAt().isEmpty()
                    && !note.getUpdatedAt().equals(note.getCreatedAt())) {
                tvUpdatedAt.setText("Updated at " + note.getUpdatedAt());
                tvUpdatedAt.setVisibility(View.VISIBLE);
            } else {
                tvUpdatedAt.setVisibility(View.GONE);
            }

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra(FormActivity.EXTRA_NOTES, note);
                activity.startActivityForResult(intent, FormActivity.REQUEST_UPDATE);
            });
        }
    }
}