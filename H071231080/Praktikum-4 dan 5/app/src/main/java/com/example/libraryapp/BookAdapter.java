package com.example.libraryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> bookList;
    private Context context;

    public BookAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvYear.setText(String.valueOf(book.getPublishYear()));
        holder.tvGenre.setText(book.getGenre());

        // Gambar sampul buku
        setBookCover(holder.ivCover, book.getCoverImageUri());

        // Tampilkan ikon favorit jika buku difavoritkan
        holder.ivFavorite.setVisibility(book.isFavorite() ? View.VISIBLE : View.GONE);

        // Set listener untuk berpindah ke halaman detail
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void updateData(List<Book> newBooks) {
        this.bookList.clear();
        this.bookList.addAll(newBooks);
        notifyDataSetChanged();
    }

    private void setBookCover(ImageView imageView, String imageUri) {
        if (imageUri == null || imageUri.isEmpty()) {
            imageView.setImageResource(R.drawable.book_default);
            return;
        }

        if (imageUri.startsWith("content://") || imageUri.startsWith("file://")) {
            imageView.setImageURI(android.net.Uri.parse(imageUri));
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

    static class BookViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivCover, ivFavorite;
        TextView tvTitle, tvAuthor, tvYear, tvGenre;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            ivCover = itemView.findViewById(R.id.ivCover);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvGenre = itemView.findViewById(R.id.tvGenre);
        }
    }
}