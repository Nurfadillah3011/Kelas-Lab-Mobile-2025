package com.example.t3mobile_h071231080;


import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {

    private int id;
    private String username;
    private int profilePicture;
    private int imageResource;
    private String imageUri;
    private String caption;
    private int likes;
    //
    private int comments;

    public Post(int id, String username, int profilePicture, int imageResource, String caption, int likes, int comments) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.imageResource = imageResource;
        this.imageUri = null;
        this.caption = caption;
        this.likes = likes;
        this.comments = comments;
    }


    public Post(int id, String username, int profilePicture, String imageUri, String caption, int likes, int comments) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.imageResource = 0;
        this.imageUri = imageUri;
        this.caption = caption;
        this.likes = likes;
        this.comments = comments;
    }


    protected Post(Parcel in) {
        id = in.readInt();
        username = in.readString();
        profilePicture = in.readInt();
        imageResource = in.readInt();
        imageUri = in.readString();
        caption = in.readString();
        likes = in.readInt();
        comments = in.readInt();
    }


    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }


        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getProfilePicture() {
        return profilePicture;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getCaption() {
        return caption;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }


    public boolean hasImageUri() {
        return imageUri != null && !imageUri.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeInt(profilePicture);
        dest.writeInt(imageResource);
        dest.writeString(imageUri);
        dest.writeString(caption);
        dest.writeInt(likes);
        dest.writeInt(comments);
    }
}