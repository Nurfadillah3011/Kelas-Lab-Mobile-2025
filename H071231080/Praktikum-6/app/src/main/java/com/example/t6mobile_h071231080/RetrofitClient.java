package com.example.t6mobile_h071231080;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//bertanggung jawab membuat dan mengatur instance retrofit
public class RetrofitClient {
    private static final String BASE_URL = "https://rickandmortyapi.com/api/";
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    public static ApiService getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}