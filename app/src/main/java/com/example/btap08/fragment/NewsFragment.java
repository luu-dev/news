package com.example.btap08.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btap08.R;
import com.example.btap08.activity.MainActivity;
import com.example.btap08.activity.WebViewActivity;
import com.example.btap08.adapter.NewsAdapter;
import com.example.btap08.dao.AppDatabase;
import com.example.btap08.download.FileDownloadManager;
import com.example.btap08.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment implements NewsAdapter.NewsListener, Runnable {
    TextView txtNoData;
    private RecyclerView lvNews;
    private NewsAdapter adapter;
    private ArrayList<News> data = new ArrayList<>();
    private String link;
    private ArrayList<String> chuoi = new ArrayList<String>();
    ;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_news;
    }

    @Override
    public String getTitle() {
        return "NEWS";
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (checkPermission()) {
            lvNews = findViewByID(R.id.lv_news);
            txtNoData = findViewByID(R.id.txt_nodata);
            adapter = new NewsAdapter(getLayoutInflater());
            lvNews.setAdapter(adapter);
            adapter.setArrNews(data);

            adapter.setListener(this);


        } else {
            requestPermission();
        }

    }

    public void setData(List<News> data) {
        chuoi.clear();
        this.data.clear();
        this.data.addAll(data);
        for (int i = 0; i < data.size(); i++)
            chuoi.add("");
        Log.e("size data", "onActivityCreated: " + data.size());
        if (adapter != null) {
            adapter.setArrNews(this.data);
            txtNoData.setVisibility(View.GONE);

        }
    }

    @Override
    public void onItemNewsClicked(int posititon) {


        if (chuoi.get(posititon) == "") {
            Intent intent = WebViewActivity.getInstance(getContext(), data.get(posititon).getUrl());
            startActivity(intent);
        } else {
            Intent intent = WebViewActivity.getInstance(getContext(),  "file:///storage/emulated/0/BTAP08/"+data.get(posititon).getTitle()+".html");
//            Intent intent = WebViewActivity.getInstance(getContext(),  chuoi.get(posititon));

            startActivity(intent);
        }


        Log.e("Click: ", data.get(posititon).getUrl());

    }

    private static int vitri;
    private boolean ktra = false;

    @Override
    public void onItemNewsLongClicked(int position) {
        try {
            AppDatabase.getInstance(getContext()).getNewsDao().insert(data.get(position));
            vitri = position;
            MainActivity act = (MainActivity) getActivity();
            act.getSavedFragment().getData();
            Toast.makeText(getContext(), "Da them", Toast.LENGTH_SHORT).show();
            link = data.get(position).getUrl();
            Log.e("zzzz ", link);

            ktra = true;
            Thread t = new Thread(this);
            t.start();


        } catch (Exception ex) {
            Toast.makeText(getContext(), "Da ton tai", Toast.LENGTH_SHORT).show();
            ktra = false;
        }

    }

    private String path;

    private static String ss;
    @Override
    public void run() {
        String dl;
        if (ktra == true) {
            path = FileDownloadManager.download(link,data.get(vitri).getTitle());
            chuoi.set(vitri, "file://" + path);

            Log.e("cc","file:///storage/emulated/0/BTAP08/"+data.get(vitri).getTitle()+".html" );
            dl = chuoi.get(vitri);
            Log.e("chuoi", chuoi.get(vitri));
            MainActivity activity = (MainActivity) getActivity();


            Message msg = new Message();
            msg.what = WHAT_FINISH_DOWNLOAD;
            msg.obj = path;
            handler.sendMessage(msg);
        } else
            return;
    }


    private static final int WHAT_FINISH_DOWNLOAD = 1;
    private String[] PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean checkPermission() {
        if (android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.M) {
            for (String p : PERMISSION) {
                int status = getActivity().checkSelfPermission(p);
                if (status == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSION, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission()) {
            lvNews = findViewByID(R.id.lv_news);
            txtNoData = findViewByID(R.id.txt_nodata);
            adapter = new NewsAdapter(getLayoutInflater());
            lvNews.setAdapter(adapter);
            adapter.setArrNews(data);
            adapter.setListener(this);
        } else {
            getActivity().finish();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_FINISH_DOWNLOAD) {
                String path = msg.obj.toString();
                Toast.makeText(getContext(), path, Toast.LENGTH_SHORT).show();
                Log.d("dd", "handleMessage: " + path);
            }
        }
    };

}