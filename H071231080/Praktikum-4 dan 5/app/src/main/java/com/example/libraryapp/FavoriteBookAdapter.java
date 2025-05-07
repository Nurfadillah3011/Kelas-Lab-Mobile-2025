package com.example.libraryapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.FavoriteBookViewHolder> {

    private final List<Book> favoriteBookList;
    private final Context context;
    private final OnFavoriteBookClickListener listener;

    public interface OnFavoriteBookClickListener {
        void onBookClick(Book book);
    }

    public FavoriteBookAdapter(List<Book> favoriteBookList, Context context, OnFavoriteBookClickListener listener) {
        this.favoriteBookList = favoriteBookList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new FavoriteBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteBookViewHolder holder, int position) {
        Book book = favoriteBookList.get(position);

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvYear.setText(String.valueOf(book.getPublishYear()));
        holder.tvGenre.setText(book.getGenre());

        // Menggunakan metode setBookCover yang lebih lengkap seperti pada BookAdapter
        setBookCover(holder.ivCover, book.getCoverImageUri());

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteBookList.size();
    }

    // Menambahkan metode setBookCover yang sama seperti di BookAdapter
    private void setBookCover(ImageView imageView, String imageUri) {
        if (imageUri == null || imageUri.isEmpty()) {
            imageView.setImageResource(R.drawable.book_default);
            return;
        }

        if (imageUri.startsWith("content://") || imageUri.startsWith("file://")) {
            imageView.setImageURI(Uri.parse(imageUri));
        } else {
            int resId = context.getResources().getIdentifier(
                    imageUri, "drawable", context.getPackageName());
            if (resId != 0) {
                imageView.setImageResource(resId);
            } else {
                imageView.setImageResource(R.drawable.book_default);
            }
        }
    }

    public static class FavoriteBookViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivCover;
        TextView tvTitle, tvAuthor, tvYear, tvGenre;

        public FavoriteBookViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvGenre = itemView.findViewById(R.id.tvGenre);
        }
    }
}
