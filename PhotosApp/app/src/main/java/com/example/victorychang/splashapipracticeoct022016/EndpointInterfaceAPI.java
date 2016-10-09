package com.example.victorychang.splashapipracticeoct022016;

import com.example.victorychang.splashapipracticeoct022016.model.OneImage;
import com.example.victorychang.splashapipracticeoct022016.model2.SearchImage;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Path;

/**
 * Created by VictoryChang on 10/5/2016.
 */
public interface EndpointInterfaceAPI {

    @GET("photos?client_id=4f29bddef2e2207e51d5c5a100a9051ea6b4e366f07e456ae06980b264d0dcd4")
    Call<List<OneImage>> getListOneImageObjects();

    @GET("search/photos?client_id=4f29bddef2e2207e51d5c5a100a9051ea6b4e366f07e456ae06980b264d0dcd4&")
    Call<SearchImage> getSearchListOneImageObjects(@Query("query") String searchQuery);


    class Factory {
        public static EndpointInterfaceAPI getInstance() {
            EndpointInterfaceAPI service;
            String UNSPLASH_BASE_URL = "https://api.unsplash.com/";
            /*
            Interceptor interceptor = new Interceptor() {
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder().addHeader("client_id", "4f29bddef2e2207e51d5c5a100a9051ea6b4e366f07e456ae06980b264d0dcd4\"").build();
                    return chain.proceed(newRequest);
                }
            };

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.interceptors().add(interceptor);
            OkHttpClient client = builder.build();
            */

            // .client(client)
            Retrofit retrofit = new Retrofit.Builder().baseUrl(UNSPLASH_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            service = retrofit.create(EndpointInterfaceAPI.class);
            return service;
        }
    }
}
