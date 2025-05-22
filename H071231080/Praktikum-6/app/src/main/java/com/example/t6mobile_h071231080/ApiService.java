package com.example.t6mobile_h071231080;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


// mendefinisukan endpoint yg digunakan, endpoint adalah url yg digunakan untuk berinteraksi dgn API
public interface ApiService {
    // Get character list with pagination
    @GET("character")
    Call<CharacterResponse> getCharacters(@Query("page") int page);

    // Get detail of a specific character by ID
    @GET("character/{id}")
    Call<Character> getCharacterDetail(@Path("id") int characterId);
}