package com.example.btap08.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btap08.R;
import com.example.btap08.activity.WebViewActivity;
import com.example.btap08.adapter.FavoriteAdapter;
import com.example.btap08.dao.AppDatabase;
import com.example.btap08.model.News;

import java.util.ArrayList;

public class FavoriteFragment extends BaseFragment implements FavoriteAdapter.FavoriteListener {

    private ArrayList<News> data = new ArrayList<>();
    private RecyclerView rvFavorite;
    private FavoriteAdapter adapter;
    private ArrayList<String>  dll=new ArrayList<String>();
    public void setDll( String s){
        dll.add(s);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvFavorite = findViewByID(R.id.rv_favorite);
        adapter = new FavoriteAdapter(getLayoutInflater());

        rvFavorite.setAdapter(adapter);
        adapter.setArrNews(data);
        adapter.setListener(this);
        getData();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_favorite;
    }

    @Override
    public String getTitle() {
        return "FAVORITE";
    }

    public void getData(){
        data.clear();
        data.addAll(AppDatabase.getInstance(getContext()).getNewsDao().getFavorite());
        if (adapter != null){
            adapter.setArrNews(data);
        }
    }

    @Override
    public void onItemFavoriteClicked(int position) {
//        Intent intent = WebViewActivity.getInstance(getContext(), data.get(position).getUrl());
//        Log.e( "Click: ", data.get(position).getUrl());
//        startActivity(intent);
//        if (dll.get(position)=="") {
//            Intent intent = WebViewActivity.getInstance(getContext(), data.get(position).getUrl());
////            Intent intent = WebViewActivity.getInstance(getContext(), "file:///storage/emulated/0/BTAP08/1593417175507.html");
//            startActivity(intent);
//        }
//        else
//        {
            Intent intent = WebViewActivity.getInstance(getContext(),"file:///storage/emulated/0/BTAP08/"+data.get(position).getTitle()+".html");
            startActivity(intent);
//        }

    }

    @Override
    public void onItemFavoriteLongClicked(int position) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("Ban co muon xoa")
                .setNegativeButton("Huy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Xoa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        News news = data.get(position);
                        news.setFavorite(0);
                        //AppDatabase.getInstance(getContext()).getNewsDao().delete(data.get(position));
                        AppDatabase.getInstance(getContext()).getNewsDao().update(news);
                        getData();
                        Toast.makeText(getContext(), "Da xoa", Toast.LENGTH_SHORT).show();
                    }
                }).create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }
}