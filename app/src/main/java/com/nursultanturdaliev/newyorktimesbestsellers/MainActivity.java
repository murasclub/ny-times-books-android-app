package com.nursultanturdaliev.newyorktimesbestsellers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.nursultanturdaliev.newyorktimesbestsellers.adapter.MyAdapter;
import com.nursultanturdaliev.newyorktimesbestsellers.service.NewYorkTimesApiResponse;
import com.nursultanturdaliev.newyorktimesbestsellers.service.NewYorkTimesBooksApiService;

import java.util.List;

import butterknife.BindView;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.best_seller_list_recycler_view)
    RecyclerView bestSellerListRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Cache cache;
    private int cacheSize = 10 * 1024 * 1024; // 10 MB
    private Retrofit retrofit;
    public NewYorkTimesBooksApiService newYorkTimesBooksApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cache = new Cache(getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder().
                baseUrl("https://api.nytimes.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build();

        newYorkTimesBooksApiService = retrofit.create(NewYorkTimesBooksApiService.class);

        newYorkTimesBooksApiService.fetchBestSellerListNames(newYorkTimesBooksApiService.apiKey)
        .enqueue(new Callback<NewYorkTimesApiResponse>() {
            @Override
            public void onResponse(Call<NewYorkTimesApiResponse> call, Response<NewYorkTimesApiResponse> response) {
                if(response.isSuccessful()) {
                    NewYorkTimesApiResponse changesList = response.body();
                    bestSellerListRecyclerView.setAdapter(new MyAdapter(changesList.results));
                } else {
                    Toast.makeText(getApplicationContext(),"Non Successful response",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewYorkTimesApiResponse> call, Throwable t) {

            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        bestSellerListRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
         layoutManager = new LinearLayoutManager(this);
        bestSellerListRecyclerView.setLayoutManager(layoutManager);
    }
}
