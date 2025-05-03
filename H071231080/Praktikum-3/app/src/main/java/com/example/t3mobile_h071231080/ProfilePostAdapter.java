package com.example.t3mobile_h071231080;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {
    private List<Post> postList;

    public ProfilePostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);

        if (post.hasImageUri()) {
            try {
                Uri imageUri = Uri.parse(post.getImageUri());
                if (imageUri.toString().startsWith("file:")) {
                    File imageFile = new File(imageUri.getPath());
                    if (imageFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        holder.postImage.setImageBitmap(bitmap);
                    } else {
                        Log.e("ProfilePostAdapter", "Image file does not exist: " + imageUri.getPath());
                        holder.postImage.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                } else {
                    holder.postImage.setImageURI(imageUri);
                }

                if (holder.postImage.getDrawable() == null) {
                    holder.postImage.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            } catch (Exception e) {
                Log.e("ProfilePostAdapter", "Error loading image: " + e.getMessage());
                holder.postImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.postImage.setImageResource(post.getImageResource());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
            intent.putExtra("username", post.getUsername());
            intent.putExtra("profilePic", post.getProfilePicture());
            intent.putExtra("caption", post.getCaption());
            intent.putExtra("likes", post.getLikes());
            intent.putExtra("comments", post.getComments());

            if (post.hasImageUri()) {
                intent.putExtra("imageUri", post.getImageUri());
                intent.putExtra("usingUri", true);
            } else {
                intent.putExtra("postImage", post.getImageResource());
                intent.putExtra("usingUri", false);
            }

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;

        public ViewHolder(View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.profilePostImage);
        }
    }
}
