package com.example.btap08.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btap08.R;
import com.example.btap08.activity.MainActivity;
import com.example.btap08.activity.WebViewActivity;
import com.example.btap08.adapter.BaseAdapter;
import com.example.btap08.adapter.SavedAdapter;
import com.example.btap08.dao.AppDatabase;
import com.example.btap08.model.News;

import java.util.ArrayList;

public class SavedFragment extends BaseFragment implements SavedAdapter.SavedListener {
    private ArrayList<News> data = new ArrayList<>();
    private RecyclerView rvSaved;
    private SavedAdapter adapter;
    private ArrayList<String>  dl=new ArrayList<String>();

//  public void setDl( String s){
////      dl.clear();
////      dl.addAll(dl);
//        if (dl!=null)
//            dl.add(s);
//
//    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvSaved = findViewByID(R.id.rv_saved);
        adapter = new SavedAdapter(getLayoutInflater());

        rvSaved.setAdapter(adapter);
        adapter.setArrNews(data);
        adapter.setListener(this);
        getData();
    }

    public void getData(){
        data.clear();
        data.addAll(AppDatabase.getInstance(getContext()).getNewsDao().getAll());
        if (adapter != null) {
            adapter.setArrNews(data);
        }
    }

    @Override
    public void onItemSavedClicked(int position) {
//        Intent intent = WebViewActivity.getInstance(getContext(), data.get(position).getUrl());
//        startActivity(intent);
//        if (dl.get(position)=="") {
//            Intent intent = WebViewActivity.getInstance(getContext(), data.get(position).getUrl());
////            Intent intent = WebViewActivity.getInstance(getContext(), "file:///storage/emulated/0/BTAP08/1593417175507.html");
//            startActivity(intent);
//        }
//        else
//        {
            Intent intent = WebViewActivity.getInstance(getContext(), "file:///storage/emulated/0/BTAP08/"+data.get(position).getTitle()+".html");
//            Intent intent = WebViewActivity.getInstance(getContext(), "file:///storage/emulated/0/BTAP08/1593417175507.html");
            startActivity(intent);
//        }
    }

    @Override
    public void onItemDeleteLongClicked(int position) {
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

                        AppDatabase.getInstance(getContext()).getNewsDao().delete(data.get(position));

                        getData();
                        MainActivity act = (MainActivity)getActivity();
                        act.getFavoriteFragment().getData();
                        Toast.makeText(getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                    }
                }).create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    @Override
    public void inItemFavoriteLongClick(int position) {
        try {
            News news = data.get(position);
            news.setFavorite(1);

//            MainActivity activity = (MainActivity) getActivity();
//            activity.getFavoriteFragment().setDll(dl.get(position));

            AppDatabase.getInstance(getContext()).getNewsDao().update(news);
            MainActivity act = (MainActivity) getActivity();
            act.getFavoriteFragment().getData();
            Toast.makeText(getContext(), "Da them", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(getContext(), "Da ton tai ", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_saved;
    }

    @Override
    public String getTitle() {
        return "SAVED";
    }
}