package com.example.btap08.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btap08.R;
import com.example.btap08.adapter.NewsAdapter;
import com.example.btap08.adapter.NewsPagerAdapter;
import com.example.btap08.api.ApiBuilder;
import com.example.btap08.fragment.FavoriteFragment;
import com.example.btap08.fragment.NewsFragment;
import com.example.btap08.fragment.SavedFragment;
import com.example.btap08.model.News;
import com.example.btap08.model.NewsResponse;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<NewsResponse>, SearchView.OnQueryTextListener {
    private SearchView search;
    private ArrayList<News> data;
    private Dialog dialog;
    private ViewPager pager;
    private TabLayout tab;
    private NewsPagerAdapter adpter;

    private NewsFragment newsFragment = new NewsFragment();
    private SavedFragment savedFragment = new SavedFragment();
    private FavoriteFragment favoriteFragment = new FavoriteFragment();

    public ViewPager getPager() {
        return pager;
    }

    public NewsFragment getNewsFragment() {
        return newsFragment;
    }

    public FavoriteFragment getFavoriteFragment() {
        return favoriteFragment;
    }

    public SavedFragment getSavedFragment() {
        return savedFragment;
    }
    //private TextView txtNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


    }

    private void initViews() {

        pager = findViewById(R.id.pager);
        tab = findViewById(R.id.tab);
        tab.setupWithViewPager(pager);
        adpter = new NewsPagerAdapter(getSupportFragmentManager(), newsFragment, savedFragment, favoriteFragment);
        pager.setAdapter(adpter);
        //ager.addOnPageChangeListener(this);

        dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_progress_loading);
        dialog.setCancelable(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        search = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        search.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
        dialog.dismiss();
        NewsResponse newsResponse = response.body();
        data = newsResponse.getArrNews();


        newsFragment.setData(data);

    }

    @Override
    public void onFailure(Call<NewsResponse> call, Throwable t) {
        Toast.makeText(this, "khong thay", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        dialog.show();
        pager.setCurrentItem(0);
        ApiBuilder.getInstance().getNews(query, "09f2b1cffb9849f6a22f53f742be4476").enqueue(this);
        search.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


}