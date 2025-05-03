package com.example.t3mobile_h071231080;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<Post> postList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public FeedAdapter(List<Post> postList, OnItemClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        Context context = holder.itemView.getContext();
        holder.profileImage.setImageResource(post.getProfilePicture());
        holder.username.setText(post.getUsername());

        if (post.hasImageUri()) {
            try {
                Uri imageUri = Uri.parse(post.getImageUri());
                if (imageUri.toString().startsWith("file:")) {
                    File imageFile = new File(imageUri.getPath());
                    if (imageFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        holder.postImage.setImageBitmap(bitmap);
                    } else {
                        holder.postImage.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                } else {
                    holder.postImage.setImageURI(imageUri);
                }

                if (holder.postImage.getDrawable() == null) {
                    holder.postImage.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            } catch (Exception e) {
                holder.postImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.postImage.setImageResource(post.getImageResource());
        }

        holder.caption.setText(post.getCaption());
        holder.likes.setText(post.getLikes() + " likes");
        holder.comments.setText("View all " + post.getComments() + " comments");

        View.OnClickListener profileClickListener = v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        };

        holder.profileImage.setOnClickListener(profileClickListener);
        holder.username.setOnClickListener(profileClickListener);

        holder.postImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("postId", post.getId());
            intent.putExtra("username", post.getUsername());
            intent.putExtra("profilePic", post.getProfilePicture());

            if (post.hasImageUri()) {
                intent.putExtra("imageUri", post.getImageUri());
                intent.putExtra("usingUri", true);
            } else {
                intent.putExtra("postImage", post.getImageResource());
                intent.putExtra("usingUri", false);
            }

            intent.putExtra("caption", post.getCaption());
            intent.putExtra("likes", post.getLikes());
            intent.putExtra("comments", post.getComments());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView username;
        ImageView postImage;
        TextView caption;
        TextView likes;
        TextView comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            postImage = itemView.findViewById(R.id.postImage);
            caption = itemView.findViewById(R.id.caption);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
        }
    }
}
